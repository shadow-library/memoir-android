package com.shadow.apps.memoir.data.preferences

import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import com.shadow.apps.memoir.domain.model.DeviceIdentity
import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Encrypted config store
 *
 * Encrypts setup values with Android Keystore AES-GCM before writing them to
 * app-private SharedPreferences.
 */
@Singleton
class EncryptedConfigStore @Inject constructor(
    private val prefs: SharedPreferences,
) {

    private companion object {
        const val KEYSTORE_ALIAS = "shadow_memoir_config_key"
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
        const val KEY_PROJECT_ID = "firebase_project_id"
        const val KEY_APP_ID = "firebase_app_id"
        const val KEY_API_KEY = "firebase_api_key"
        const val KEY_STORAGE_BUCKET = "firebase_storage_bucket"
        const val KEY_WEB_CLIENT_ID = "firebase_web_client_id"
        const val KEY_DEVICE_ID = "device_id"
        const val KEY_DEVICE_NAME = "device_name"
        const val KEY_IS_PRIMARY = "device_is_primary"
        const val KEY_HAS_COMPLETED_SETUP = "has_completed_setup"
    }

    /*
     * Key management
     */
    private val secretKey: SecretKey
        get() {
            val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            (keyStore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let {
                return it.secretKey
            }

            return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                .apply {
                    init(
                        KeyGenParameterSpec.Builder(
                            KEYSTORE_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                        )
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            .build(),
                    )
                }
                .generateKey()
        }

    /*
     * Encryption
     */
    private fun encrypt(plaintext: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(cipher.iv + ciphertext, Base64.NO_WRAP)
    }

    private fun decrypt(encoded: String): String? = runCatching {
        val raw = Base64.decode(encoded, Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(GCM_TAG_BITS, raw, 0, GCM_IV_BYTES),
        )
        String(cipher.doFinal(raw, GCM_IV_BYTES, raw.size - GCM_IV_BYTES), Charsets.UTF_8)
    }.getOrNull()

    private fun SharedPreferences.Editor.putEncryptedString(key: String, value: String) {
        putString(key, encrypt(value))
    }

    private fun getEncryptedString(key: String): String? =
        prefs.getString(key, null)?.let { decrypt(it) }

    /*
     * Firebase credentials
     */
    fun saveFirebaseCredentials(credentials: FirebaseCredentials) {
        prefs.edit {
            putEncryptedString(KEY_PROJECT_ID, credentials.projectId)
            putEncryptedString(KEY_APP_ID, credentials.appId)
            putEncryptedString(KEY_API_KEY, credentials.apiKey)
            putEncryptedString(KEY_STORAGE_BUCKET, credentials.storageBucket)
            putEncryptedString(KEY_WEB_CLIENT_ID, credentials.webClientId)
        }
    }

    fun loadFirebaseCredentials(): FirebaseCredentials? {
        val projectId = getEncryptedString(KEY_PROJECT_ID) ?: return null
        val appId = getEncryptedString(KEY_APP_ID) ?: return null
        val apiKey = getEncryptedString(KEY_API_KEY) ?: return null
        val bucket = getEncryptedString(KEY_STORAGE_BUCKET) ?: return null
        val webClientId = getEncryptedString(KEY_WEB_CLIENT_ID) ?: return null
        return FirebaseCredentials(
            projectId = projectId,
            appId = appId,
            apiKey = apiKey,
            storageBucket = bucket,
            webClientId = webClientId,
        )
    }

    fun clearFirebaseCredentials() {
        prefs.edit {
            remove(KEY_PROJECT_ID)
            remove(KEY_APP_ID)
            remove(KEY_API_KEY)
            remove(KEY_STORAGE_BUCKET)
            remove(KEY_WEB_CLIENT_ID)
        }
    }

    fun hasFirebaseCredentials(): Boolean = prefs.getString(KEY_PROJECT_ID, null) != null

    /*
     * Device identity
     */
    fun saveDeviceIdentity(identity: DeviceIdentity) {
        prefs.edit {
            putEncryptedString(KEY_DEVICE_ID, identity.deviceId)
            putEncryptedString(KEY_DEVICE_NAME, identity.deviceName)
            putEncryptedString(KEY_IS_PRIMARY, identity.isPrimary.toString())
            putEncryptedString(KEY_HAS_COMPLETED_SETUP, identity.hasCompletedSetup.toString())
        }
    }

    fun loadDeviceId(): String? = getEncryptedString(KEY_DEVICE_ID)

    fun loadDeviceIsPrimary(): Boolean = getEncryptedString(KEY_IS_PRIMARY) == "true"

    fun hasCompletedSetup(): Boolean = getEncryptedString(KEY_HAS_COMPLETED_SETUP) == "true"
}
