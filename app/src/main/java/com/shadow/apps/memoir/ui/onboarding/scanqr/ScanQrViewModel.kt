package com.shadow.apps.memoir.ui.onboarding.scanqr

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
class ScanQrViewModel @Inject constructor(
    private val parseFirebaseConfig: ParseFirebaseConfigUseCase,
    private val saveFirebaseCredentials: SaveFirebaseCredentialsUseCase,
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
                    databaseUrl = creds.databaseUrl ?: "",
                    webClientId = creds.webClientId ?: "",
                    error = null,
                )
            }
        } else {
            _uiState.update { it.copy(error = "Invalid QR code \u2014 expected a Memoir config") }
        }
    }

    fun saveAndContinue(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isConfigReceived) return
        _uiState.update { it.copy(isSaving = true) }
        saveFirebaseCredentials(state.toCredentials())
        _uiState.update { it.copy(isSaving = false) }
        onSuccess()
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
            databaseUrl = databaseUrl.trim().takeIf { it.isNotBlank() },
            webClientId = webClientId.trim().takeIf { it.isNotBlank() },
        )
}
