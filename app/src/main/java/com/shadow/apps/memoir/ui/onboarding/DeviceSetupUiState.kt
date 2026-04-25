package com.shadow.apps.memoir.ui.onboarding

import com.shadow.apps.memoir.data.CurrencyOption

data class DeviceSetupUiState(
    val deviceName: String = "",
    val isPrimary: Boolean = true,
    val selectedCurrency: String = "INR",
    val currencies: List<CurrencyOption> = emptyList(),
    val isLoadingCurrencies: Boolean = false,
    val currencyError: String? = null,
    val isLoadingProfile: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)
