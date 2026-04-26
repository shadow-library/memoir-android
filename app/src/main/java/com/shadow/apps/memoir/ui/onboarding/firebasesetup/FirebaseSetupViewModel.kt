package com.shadow.apps.memoir.ui.onboarding.firebasesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.usecase.onboarding.ParseFirebaseConfigUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.SaveFirebaseCredentialsUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.VerificationResult
import com.shadow.apps.memoir.domain.usecase.onboarding.VerifyFirebaseCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseSetupViewModel @Inject constructor(
    private val parseFirebaseConfig: ParseFirebaseConfigUseCase,
    private val saveFirebaseCredentials: SaveFirebaseCredentialsUseCase,
    private val verifyFirebaseCredentials: VerifyFirebaseCredentialsUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(FirebaseSetupUiState())
    val uiState: StateFlow<FirebaseSetupUiState> = _uiState.asStateFlow()

    /*
     * Field change events
     */
    fun onProjectIdChanged(value: String) = _uiState.update { it.copy(projectId = value, projectIdError = null) }
    fun onApiKeyChanged(value: String) = _uiState.update { it.copy(apiKey = value, apiKeyError = null) }
    fun onAppIdChanged(value: String) = _uiState.update { it.copy(appId = value, appIdError = null) }
    fun onStorageBucketChanged(value: String) = _uiState.update { it.copy(storageBucket = value, storageBucketError = null) }
    fun onWebClientIdChanged(value: String) = _uiState.update { it.copy(webClientId = value, webClientIdError = null) }

    /*
     * Blur events — validate the field that just lost focus
     */
    fun onProjectIdBlurred() = _uiState.update { it.copy(projectIdError = validateProjectId(it.projectId)) }
    fun onApiKeyBlurred() = _uiState.update { it.copy(apiKeyError = validateApiKey(it.apiKey)) }
    fun onAppIdBlurred() = _uiState.update { it.copy(appIdError = validateAppId(it.appId)) }
    fun onStorageBucketBlurred() = _uiState.update { it.copy(storageBucketError = validateStorageBucket(it.storageBucket)) }
    fun onWebClientIdBlurred() = _uiState.update { it.copy(webClientIdError = validateWebClientId(it.webClientId)) }

    /** Parses uploaded file content and fills all fields. Sets [uploadError] on failure. */
    fun onFileUploaded(content: String) {
        val creds = parseFirebaseConfig.fromGoogleServicesJson(content)
        if (creds != null) {
            _uiState.update {
                it.copy(
                    projectId = creds.projectId,
                    apiKey = creds.apiKey,
                    appId = creds.appId,
                    storageBucket = creds.storageBucket,
                    webClientId = creds.webClientId,
                    projectIdError = null,
                    apiKeyError = null,
                    appIdError = null,
                    storageBucketError = null,
                    webClientIdError = null,
                    uploadError = null,
                )
            }
        } else {
            _uiState.update {
                it.copy(uploadError = "Invalid google-services.json — ensure the file includes a Web OAuth client (client_type 3)")
            }
        }
    }

    fun dismissUploadError() = _uiState.update { it.copy(uploadError = null) }

    fun saveAndContinue(onSuccess: () -> Unit) {
        if (_uiState.value.isSaving) return

        val state = _uiState.value

        val updatedState = state.copy(
            projectIdError = validateProjectId(state.projectId),
            apiKeyError = validateApiKey(state.apiKey),
            appIdError = validateAppId(state.appId),
            storageBucketError = validateStorageBucket(state.storageBucket),
            webClientIdError = validateWebClientId(state.webClientId),
        )
        _uiState.value = updatedState
        if (!updatedState.isValid || !updatedState.hasNoErrors) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, verificationError = null) }
            val result = verifyFirebaseCredentials(updatedState.apiKey.trim(), updatedState.webClientId.trim())
            when (result) {
                is VerificationResult.Valid -> {
                    saveFirebaseCredentials(updatedState.toCredentials())
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is VerificationResult.InvalidApiKey -> _uiState.update {
                    it.copy(isSaving = false, apiKeyError = "API key is invalid — check your Firebase project settings")
                }
                is VerificationResult.InvalidWebClientId -> _uiState.update {
                    it.copy(isSaving = false, webClientIdError = "Web Client ID not found — ensure Google Sign-In is enabled in Firebase Console")
                }
                is VerificationResult.NetworkError -> _uiState.update {
                    it.copy(isSaving = false, verificationError = "Could not reach Firebase — check your internet connection")
                }
            }
        }
    }

    /*
     * Validation
     */
    private fun validateProjectId(value: String): String? {
        if (value.isBlank()) return null
        val valid = value.matches(Regex("^[a-z][a-z0-9\\-]*[a-z0-9]$"))
        return if (valid) null else "Must start with a letter; lowercase letters, digits, and hyphens only"
    }

    private fun validateApiKey(value: String): String? {
        if (value.isBlank()) return null
        val trimmed = value.trim()
        return if (trimmed.startsWith("AIza") && trimmed.length >= 35) null else "Must start with \"AIza\""
    }

    private fun validateAppId(value: String): String? {
        if (value.isBlank()) return null
        val valid = value.trim().matches(Regex("^1:\\d+:android:[0-9a-f]+$"))
        return if (valid) null else "Format: 1:{number}:android:{hex}"
    }

    private fun validateStorageBucket(value: String): String? {
        if (value.isBlank()) return null
        val trimmed = value.trim()
        val valid = trimmed.endsWith(".appspot.com") || trimmed.endsWith(".firebasestorage.app")
        return if (valid) null else "Must end with .appspot.com or .firebasestorage.app"
    }

    private fun validateWebClientId(value: String): String? {
        if (value.isBlank()) return null
        val valid = value.trim().endsWith(".apps.googleusercontent.com")
        return if (valid) null else "Must end with .apps.googleusercontent.com"
    }

    /*
     * Private helpers
     */
    private fun FirebaseSetupUiState.toCredentials(): FirebaseCredentials =
        FirebaseCredentials(
            projectId = projectId.trim(),
            appId = appId.trim(),
            apiKey = apiKey.trim(),
            storageBucket = storageBucket.trim(),
            webClientId = webClientId.trim(),
        )
}
