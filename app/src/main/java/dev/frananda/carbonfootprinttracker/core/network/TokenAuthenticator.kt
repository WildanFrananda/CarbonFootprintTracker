package dev.frananda.carbonfootprinttracker.core.network

import dev.frananda.carbonfootprinttracker.core.utils.AuthEventBus
import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import dev.frananda.carbonfootprinttracker.data.remote.AuthApi
import dev.frananda.carbonfootprinttracker.data.remote.RefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val secureStorage: SecureStorage,
    private val authApiProvider: Provider<AuthApi>,
    private val authEventBus: AuthEventBus
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val currentRefreshToken = secureStorage.getRefreshToken() ?: return null

        try {
            val refreshResponse = authApiProvider.get()
                .refreshTokenSync(RefreshRequest(currentRefreshToken))
                .execute()

            if (refreshResponse.isSuccessful && refreshResponse.body()?.data != null) {
                val newTokens = refreshResponse.body()!!.data!!

                secureStorage.saveTokens(newTokens.access_token, newTokens.refresh_token)

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.access_token}")
                    .build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        secureStorage.clearTokens()
        authEventBus.emitLogoutEvent()

        return null
    }
}