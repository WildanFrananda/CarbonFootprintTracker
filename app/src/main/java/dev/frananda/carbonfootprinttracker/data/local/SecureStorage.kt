package dev.frananda.carbonfootprinttracker.data.local

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import java.security.GeneralSecurityException

interface SecureStorage {
    fun saveTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens(): Unit
    fun getDeviceId(): String?
    fun saveDeviceId(deviceId: String): Unit
}

@Singleton
class SecureStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecureStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    private val keyAlias = "carbon_tracker_key_alias"
    private val provider = "AndroidKeyStore"
    private val transformation = "AES/GCM/NoPadding"

    init {
        generateKeyStoreKey()
    }

    private fun generateKeyStoreKey() {
        try {
            val keyStore = KeyStore.getInstance(provider)
            keyStore.load(null)

            if (!keyStore.containsAlias(keyAlias)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, provider)
                val spec = KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()

                keyGenerator.init(spec)
                keyGenerator.generateKey()
            }
        } catch (e: GeneralSecurityException) {
            context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
                .edit(commit = true) {
                    clear()
                }
            generateKeyStoreKey()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(provider)
        keyStore.load(null)
        return (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
    }

    private fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        
        val combined = ByteArray(iv.size + encryptedData.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encryptedData, 0, combined, iv.size, encryptedData.size)
        
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    private fun decrypt(encryptedBase64: String?): String? {
        if (encryptedBase64 == null) return null
        return try {
            val combined = Base64.decode(encryptedBase64, Base64.NO_WRAP)
            val iv = combined.sliceArray(0 until 12)
            val encryptedData = combined.sliceArray(12 until combined.size)

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            
            val decryptedData = cipher.doFinal(encryptedData)
            String(decryptedData, Charsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", encrypt(accessToken))
            putString("refresh_token", encrypt(refreshToken))
            apply()
        }
    }

    override fun getAccessToken(): String? {
        return decrypt(sharedPreferences.getString("access_token", null))
    }

    override fun getRefreshToken(): String? {
        return decrypt(sharedPreferences.getString("refresh_token", null))
    }

    override fun clearTokens(): Unit {
        sharedPreferences.edit { clear() }
    }

    override fun getDeviceId(): String? {
        return decrypt(sharedPreferences.getString("device_id", null))
    }

    override fun saveDeviceId(deviceId: String) {
        sharedPreferences.edit {
            putString("device_id", encrypt(deviceId))
        }
    }
}
