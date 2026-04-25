package com.shadow.apps.memoir.ui.onboarding.devicesetup

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.shadow.apps.memoir.domain.usecase.onboarding.CompleteDeviceSetupUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.DetectDeviceSetupPathUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.LoadCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceSetupViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val detectDeviceSetupPath: DetectDeviceSetupPathUseCase,
    private val completeDeviceSetup: CompleteDeviceSetupUseCase,
    private val loadCurrenciesUseCase: LoadCurrenciesUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(DeviceSetupUiState())
    val uiState: StateFlow<DeviceSetupUiState> = _uiState.asStateFlow()

    /*
     * Initialization
     */
    private val deviceId: String = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID,
    )

    init {
        _uiState.update { it.copy(deviceName = Build.MODEL) }
        detectPath()
        loadCurrencies()
    }

    private fun detectPath() {
        viewModelScope.launch {
            try {
                val shouldDefaultToPrimary = detectDeviceSetupPath()
                _uiState.update { it.copy(isPrimary = shouldDefaultToPrimary, isLoadingProfile = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isPrimary = true, isLoadingProfile = false) }
            }
        }
    }

    /*
     * Events
     */
    fun onDeviceNameChange(name: String) {
        _uiState.update { it.copy(deviceName = name, error = null) }
    }

    fun onIsPrimaryChange(isPrimary: Boolean) {
        _uiState.update { it.copy(isPrimary = isPrimary) }
    }

    fun onCurrencySelect(currency: String) {
        _uiState.update { it.copy(selectedCurrency = currency) }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCurrencies = true, currencyError = null) }
            val result = loadCurrenciesUseCase()
            _uiState.update {
                it.copy(
                    currencies = result.currencies,
                    isLoadingCurrencies = false,
                    currencyError = if (result.isFallback) {
                        "Could not load currencies. Using fallback options."
                    } else {
                        null
                    },
                )
            }
        }
    }

    fun finishSetup(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.deviceName.isBlank()) {
            _uiState.update { it.copy(error = "Device name cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            var lastError: Exception? = null

            for (attempt in 0..2) {
                try {
                    completeDeviceSetup(
                        deviceId = deviceId,
                        deviceName = state.deviceName.trim(),
                        isPrimary = state.isPrimary,
                        defaultCurrency = state.selectedCurrency,
                    )
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                    return@launch
                } catch (e: Exception) {
                    lastError = e
                    if (attempt < 2) delay(1000L * (attempt + 1))
                }
            }

            _uiState.update {
                it.copy(
                    isSaving = false,
                    error = lastError?.localizedMessage ?: "Setup failed. Please try again.",
                )
            }
        }
    }
}
