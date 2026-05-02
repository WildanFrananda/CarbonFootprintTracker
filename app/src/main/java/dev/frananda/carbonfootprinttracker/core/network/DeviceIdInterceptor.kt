package dev.frananda.carbonfootprinttracker.core.network

import dev.frananda.carbonfootprinttracker.core.utils.AuthEventBus
import dev.frananda.carbonfootprinttracker.core.utils.DeviceIdProvider
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceIdInterceptor @Inject constructor(
    private val deviceIdProvider: DeviceIdProvider,
    private val authEventBus: AuthEventBus
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()

        val requestWithHeader = originalRequest
            .newBuilder()
            .header("X-Device-ID", deviceIdProvider.getDeviceId())
            .build()

        val response = chain.proceed(requestWithHeader)

        if (response.code == 429) {
            authEventBus.emitRateLimitEvent()
        }

        return response
    }
}