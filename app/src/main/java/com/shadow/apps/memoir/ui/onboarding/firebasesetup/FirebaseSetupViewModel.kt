package com.shadow.apps.memoir.ui.onboarding.firebasesetup

import androidx.lifecycle.ViewModel
import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.usecase.onboarding.ParseFirebaseConfigUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.SaveFirebaseCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FirebaseSetupViewModel @Inject constructor(
    private val parseFirebaseConfig: ParseFirebaseConfigUseCase,
    private val saveFirebaseCredentials: SaveFirebaseCredentialsUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(FirebaseSetupUiState())
    val uiState: StateFlow<FirebaseSetupUiState> = _uiState.asStateFlow()

    /*
     * Events
     */
    fun onProjectIdChanged(value: String) = _uiState.update { it.copy(projectId = value) }
    fun onApiKeyChanged(value: String) = _uiState.update { it.copy(apiKey = value) }
    fun onAppIdChanged(value: String) = _uiState.update { it.copy(appId = value) }
    fun onStorageBucketChanged(value: String) = _uiState.update { it.copy(storageBucket = value) }
    fun onDatabaseUrlChanged(value: String) = _uiState.update { it.copy(databaseUrl = value) }

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

        saveFirebaseCredentials(state.toCredentials())
        _uiState.update { it.copy(isSaving = false) }
        onSuccess()
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
            databaseUrl = databaseUrl.trim().takeIf { it.isNotBlank() },
            webClientId = webClientId.trim().takeIf { it.isNotBlank() },
        )
}
