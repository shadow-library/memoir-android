package com.shadow.apps.memoir.ui.onboarding

import androidx.lifecycle.ViewModel
import com.shadow.apps.memoir.data.EncryptedConfigStore
import com.shadow.apps.memoir.data.FirebaseCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class FirebaseSetupViewModel @Inject constructor(private val configStore: EncryptedConfigStore) : ViewModel() {
    private val _uiState = MutableStateFlow(FirebaseSetupUiState())
    val uiState: StateFlow<FirebaseSetupUiState> = _uiState.asStateFlow()

    fun onProjectIdChanged(value: String) = _uiState.update { it.copy(projectId = value) }
    fun onApiKeyChanged(value: String) = _uiState.update { it.copy(apiKey = value) }
    fun onAppIdChanged(value: String) = _uiState.update { it.copy(appId = value) }
    fun onStorageBucketChanged(value: String) = _uiState.update { it.copy(storageBucket = value) }
    fun onDatabaseUrlChanged(value: String) = _uiState.update { it.copy(databaseUrl = value) }

    /** Parses uploaded file content and fills all fields. Sets [uploadError] on failure. */
    fun onFileUploaded(content: String) {
        val creds = parseGoogleServicesJson(content)
        if (creds != null) {
            _uiState.update {
                it.copy(
                    projectId = creds.projectId,
                    apiKey = creds.apiKey,
                    appId = creds.appId,
                    storageBucket = creds.storageBucket,
                    databaseUrl = creds.databaseUrl ?: "",
                    webClientId = creds.webClientId ?: "",
                    uploadError = null,
                )
            }
        } else {
            _uiState.update { it.copy(uploadError = "Invalid google-services.json — check the file") }
        }
    }

    fun dismissUploadError() = _uiState.update { it.copy(uploadError = null) }

    fun saveAndContinue(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isValid) return
        _uiState.update { it.copy(isSaving = true) }

        configStore.saveFirebaseCredentials(
            projectId = state.projectId.trim(),
            appId = state.appId.trim(),
            apiKey = state.apiKey.trim(),
            storageBucket = state.storageBucket.trim(),
            databaseUrl = state.databaseUrl.trim().takeIf { it.isNotBlank() },
            webClientId = state.webClientId.trim().takeIf { it.isNotBlank() },
        )
        _uiState.update { it.copy(isSaving = false) }
        onSuccess()
    }

    /**
     * Parses a `google-services.json` file and extracts the fields required by
     * [FirebaseCredentials]. Returns null if the JSON is malformed or missing any
     * required key.
     *
     * Expected structure:
     * ```json
     * {
     *   "project_info": {
     *     "project_id": "…",
     *     "storage_bucket": "…",
     *     "firebase_url": "…"   // optional
     *   },
     *   "client": [{
     *     "client_info": { "mobilesdk_app_id": "…" },
     *     "api_key": [{ "current_key": "…" }],
     *     "oauth_client": [{ "client_id": "…", "client_type": 3 }]   // web client
     *   }]
     * }
     * ```
     */
    private fun parseGoogleServicesJson(json: String): FirebaseCredentials? = runCatching {
        val root = JSONObject(json)
        val projectInfo = root.getJSONObject("project_info")
        val client = root.getJSONArray("client").getJSONObject(0)
        val clientInfo = client.getJSONObject("client_info")
        val apiKey = client.getJSONArray("api_key").getJSONObject(0).getString("current_key")

        val storageBucket = projectInfo.optString("storage_bucket").takeIf { it.isNotBlank() }
            ?: return@runCatching null

        // Extract the web OAuth client ID (client_type 3) for Google Sign-In
        var webClientId: String? = null
        val oauthClients = client.optJSONArray("oauth_client")
        if (oauthClients != null) {
            for (i in 0 until oauthClients.length()) {
                val oauthClient = oauthClients.getJSONObject(i)
                if (oauthClient.optInt("client_type") == 3) {
                    webClientId = oauthClient.getString("client_id")
                    break
                }
            }
        }

        FirebaseCredentials(
            projectId = projectInfo.getString("project_id"),
            appId = clientInfo.getString("mobilesdk_app_id"),
            apiKey = apiKey,
            storageBucket = storageBucket,
            databaseUrl = projectInfo.optString("firebase_url").takeIf { it.isNotBlank() },
            webClientId = webClientId,
        )
    }.getOrNull()
}
