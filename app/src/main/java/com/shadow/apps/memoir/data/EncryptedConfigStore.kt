package com.shadow.apps.memoir.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Uses Android Keystore directly for AES-256-GCM encryption of values stored
 * in a private SharedPreferences file. Keys never leave the hardware-backed
 * Keystore; all crypto operations happen inside the secure element.
 */
@Singleton
class EncryptedConfigStore @Inject constructor(@ApplicationContext context: Context) {

    private companion object {
        const val PREFS_FILE = "shadow_memoir_secure_config"
        const val KEYSTORE_ALIAS = "shadow_memoir_config_key"
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
        const val KEY_PROJECT_ID = "firebase_project_id"
        const val KEY_APP_ID = "firebase_app_id"
        const val KEY_API_KEY = "firebase_api_key"
        const val KEY_STORAGE_BUCKET = "firebase_storage_bucket"
        const val KEY_DATABASE_URL = "firebase_database_url"
        const val KEY_WEB_CLIENT_ID = "firebase_web_client_id"
    }

    private val prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    private val secretKey: SecretKey
        get() {
            val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            (ks.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }

            return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
                init(
                    KeyGenParameterSpec.Builder(
                        KEYSTORE_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setKeySize(256).build()
                )
            }.generateKey()
        }

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

    fun saveFirebaseCredentials(
        projectId: String,
        appId: String,
        apiKey: String,
        storageBucket: String,
        databaseUrl: String? = null,
        webClientId: String? = null,
    ) {
        prefs.edit().putString(KEY_PROJECT_ID, encrypt(projectId)).putString(KEY_APP_ID, encrypt(appId)).putString(KEY_API_KEY, encrypt(apiKey)).putString(KEY_STORAGE_BUCKET, encrypt(storageBucket)).apply { if (databaseUrl != null) putString(KEY_DATABASE_URL, encrypt(databaseUrl)) else remove(KEY_DATABASE_URL) }.apply { if (webClientId != null) putString(KEY_WEB_CLIENT_ID, encrypt(webClientId)) else remove(KEY_WEB_CLIENT_ID) }.apply()
    }

    fun loadFirebaseCredentials(): FirebaseCredentials? {
        val projectId = decrypt(prefs.getString(KEY_PROJECT_ID, null) ?: return null) ?: return null
        val appId = decrypt(prefs.getString(KEY_APP_ID, null) ?: return null) ?: return null
        val apiKey = decrypt(prefs.getString(KEY_API_KEY, null) ?: return null) ?: return null
        val bucket = decrypt(prefs.getString(KEY_STORAGE_BUCKET, null) ?: return null) ?: return null
        return FirebaseCredentials(
            projectId = projectId,
            appId = appId,
            apiKey = apiKey,
            storageBucket = bucket,
            databaseUrl = prefs.getString(KEY_DATABASE_URL, null)?.let { decrypt(it) },
            webClientId = prefs.getString(KEY_WEB_CLIENT_ID, null)?.let { decrypt(it) },
        )
    }

    fun clearFirebaseCredentials() {
        prefs.edit {
            remove(KEY_PROJECT_ID)
            remove(KEY_APP_ID)
            remove(KEY_API_KEY)
            remove(KEY_STORAGE_BUCKET)
            remove(KEY_DATABASE_URL)
            remove(KEY_WEB_CLIENT_ID)
        }
    }

    fun hasFirebaseCredentials(): Boolean = prefs.getString(KEY_PROJECT_ID, null) != null
}
