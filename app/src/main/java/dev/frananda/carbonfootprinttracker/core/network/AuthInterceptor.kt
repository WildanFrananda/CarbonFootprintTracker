package dev.frananda.carbonfootprinttracker.core.network

import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val secureStorage: SecureStorage
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        secureStorage.getAccessToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}