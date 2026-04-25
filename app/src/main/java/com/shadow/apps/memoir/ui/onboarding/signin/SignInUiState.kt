package com.shadow.apps.memoir.ui.onboarding.signin

data class SignInUiState(
    val projectId: String = "",
    val webClientId: String = "",
    val isSigningIn: Boolean = false,
    val error: String? = null,
)
