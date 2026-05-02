package dev.frananda.carbonfootprinttracker.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceIdProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secureStorage: SecureStorage
) {
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        val saveId = secureStorage.getDeviceId()
        if (!saveId.isNullOrBlank()) return saveId

        var androidId: String? = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        val isInvalid = androidId.isNullOrBlank() ||
                androidId == "9774d56d682e549c" ||
                androidId.length < 8 ||
                androidId.length > 64

        if (isInvalid) {
            androidId = UUID.randomUUID().toString().replace("-", "").take(32)
        }

        secureStorage.saveDeviceId(androidId)

        return androidId
    }
}