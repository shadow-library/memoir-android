package com.shadow.apps.memoir.ui.onboarding.firebasesetup

data class FirebaseSetupUiState(
    val projectId: String = "",
    val apiKey: String = "",
    val appId: String = "",
    val storageBucket: String = "",
    val webClientId: String = "",
    val projectIdError: String? = null,
    val apiKeyError: String? = null,
    val appIdError: String? = null,
    val storageBucketError: String? = null,
    val webClientIdError: String? = null,
    val uploadError: String? = null,
    val verificationError: String? = null,
    val isSaving: Boolean = false,
) {
    val isValid: Boolean
        get() = projectId.isNotBlank() && apiKey.isNotBlank() && appId.isNotBlank() && storageBucket.isNotBlank() && webClientId.isNotBlank()

    val hasNoErrors: Boolean
        get() = projectIdError == null && apiKeyError == null && appIdError == null && storageBucketError == null && webClientIdError == null
}
