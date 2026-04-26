package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import org.json.JSONObject
import javax.inject.Inject

/*
 * Parse Firebase config
 *
 * Extracts required app config from google-services JSON and QR payloads.
 */
class ParseFirebaseConfigUseCase @Inject constructor() {

    /*
     * Google services JSON
     */
    fun fromGoogleServicesJson(json: String): FirebaseCredentials? = runCatching {
        val root = JSONObject(json)
        val projectInfo = root.getJSONObject("project_info")
        val client = root.getJSONArray("client").getJSONObject(0)
        val clientInfo = client.getJSONObject("client_info")
        val apiKey = client.getJSONArray("api_key").getJSONObject(0).getString("current_key")

        val storageBucket = projectInfo.optString("storage_bucket").takeIf { it.isNotBlank() }
            ?: return@runCatching null

        var webClientId: String? = null
        val oauthClients = client.optJSONArray("oauth_client")
        if (oauthClients != null) {
            for (index in 0 until oauthClients.length()) {
                val oauthClient = oauthClients.getJSONObject(index)
                if (oauthClient.optInt("client_type") == 3) {
                    webClientId = oauthClient.getString("client_id")
                    break
                }
            }
        }

        if (webClientId.isNullOrBlank()) return@runCatching null

        FirebaseCredentials(
            projectId = projectInfo.getString("project_id"),
            appId = clientInfo.getString("mobilesdk_app_id"),
            apiKey = apiKey,
            storageBucket = storageBucket,
            webClientId = webClientId,
        )
    }.getOrNull()

    /*
     * QR payload
     */
    fun fromQrPayload(json: String): FirebaseCredentials? = runCatching {
        val root = JSONObject(json)
        val projectId = root.getString("p")
        val apiKey = root.getString("k")
        val appId = root.getString("a")
        val storageBucket = root.getString("s")
        val webClientId = root.getString("w")
        if (projectId.isBlank() || apiKey.isBlank() || appId.isBlank() || storageBucket.isBlank() || webClientId.isBlank()) {
            return@runCatching null
        }
        FirebaseCredentials(
            projectId = projectId,
            appId = appId,
            apiKey = apiKey,
            storageBucket = storageBucket,
            webClientId = webClientId,
        )
    }.getOrNull()
}
