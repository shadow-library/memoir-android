package com.shadow.apps.memoir.ui.onboarding.scanqr

enum class ScanState { Scanning, Success }

data class ScanQrUiState(
    val scanState: ScanState = ScanState.Scanning,
    val projectId: String = "",
    val apiKey: String = "",
    val appId: String = "",
    val storageBucket: String = "",
    val webClientId: String = "",
    val error: String? = null,
    val verificationError: String? = null,
    val isSaving: Boolean = false,
) {
    val isConfigReceived: Boolean get() = scanState == ScanState.Success
}
