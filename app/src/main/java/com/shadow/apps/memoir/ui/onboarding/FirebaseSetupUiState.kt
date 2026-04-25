package com.shadow.apps.memoir.ui.onboarding

data class FirebaseSetupUiState(
    val projectId: String = "",
    val apiKey: String = "",
    val appId: String = "",
    val storageBucket: String = "",
    val databaseUrl: String = "",
    val webClientId: String = "",
    val uploadError: String? = null,
    val isSaving: Boolean = false,
) {
    val isValid: Boolean
        get() = projectId.isNotBlank() && apiKey.isNotBlank() && appId.isNotBlank() && storageBucket.isNotBlank()
}
