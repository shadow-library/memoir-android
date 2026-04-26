package com.shadow.apps.memoir.ui.onboarding.accountsetup

data class AccountSetupUiState(
    val phase: Phase = Phase.Loading,
    val error: String? = null,
) {
    enum class Phase { Loading, Success }
}
