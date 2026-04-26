package com.shadow.apps.memoir.ui.onboarding.scanqr

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
class ScanQrViewModel @Inject constructor(
    private val parseFirebaseConfig: ParseFirebaseConfigUseCase,
    private val saveFirebaseCredentials: SaveFirebaseCredentialsUseCase,
    private val verifyFirebaseCredentials: VerifyFirebaseCredentialsUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(ScanQrUiState())
    val uiState: StateFlow<ScanQrUiState> = _uiState.asStateFlow()

    /*
     * Events
     */
    /** Called when the barcode scanner decodes a QR code. Idempotent after first success. */
    fun onQrScanned(rawValue: String) {
        if (_uiState.value.scanState == ScanState.Success) return
        val creds = parseFirebaseConfig.fromQrPayload(rawValue)
        if (creds != null) {
            _uiState.update {
                it.copy(
                    scanState = ScanState.Success,
                    projectId = creds.projectId,
                    apiKey = creds.apiKey,
                    appId = creds.appId,
                    storageBucket = creds.storageBucket,
                    webClientId = creds.webClientId,
                    error = null,
                )
            }
        } else {
            _uiState.update { it.copy(error = "Invalid QR code — expected a Memoir config") }
        }
    }

    fun saveAndContinue(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isConfigReceived || state.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, verificationError = null) }
            val result = verifyFirebaseCredentials(state.apiKey.trim(), state.webClientId.trim())
            when (result) {
                is VerificationResult.Valid -> {
                    saveFirebaseCredentials(state.toCredentials())
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is VerificationResult.InvalidApiKey -> _uiState.update {
                    it.copy(isSaving = false, verificationError = "API key is invalid — check your Firebase project settings")
                }
                is VerificationResult.InvalidWebClientId -> _uiState.update {
                    it.copy(isSaving = false, verificationError = "Web Client ID not found — ensure Google Sign-In is enabled in Firebase Console")
                }
                is VerificationResult.NetworkError -> _uiState.update {
                    it.copy(isSaving = false, verificationError = "Could not reach Firebase — check your internet connection")
                }
            }
        }
    }

    /*
     * Private helpers
     */
    private fun ScanQrUiState.toCredentials(): FirebaseCredentials =
        FirebaseCredentials(
            projectId = projectId.trim(),
            appId = appId.trim(),
            apiKey = apiKey.trim(),
            storageBucket = storageBucket.trim(),
            webClientId = webClientId.trim(),
        )
}
