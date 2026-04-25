package com.shadow.apps.memoir.ui.onboarding

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.shadow.apps.memoir.data.CurrencyRepository
import com.shadow.apps.memoir.data.EncryptedConfigStore
import com.shadow.apps.memoir.data.FirebaseManager
import com.shadow.apps.memoir.data.ProfileRepository
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
    private val firebaseManager: FirebaseManager,
    private val configStore: EncryptedConfigStore,
    private val profileRepository: ProfileRepository,
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeviceSetupUiState())
    val uiState: StateFlow<DeviceSetupUiState> = _uiState.asStateFlow()

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
        val uid = firebaseManager.auth().currentUser?.uid ?: run {
            _uiState.update { it.copy(isLoadingProfile = false) }
            return
        }
        viewModelScope.launch {
            try {
                val profileExists = profileRepository.profileExists(uid)
                _uiState.update { it.copy(isPrimary = !profileExists, isLoadingProfile = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isPrimary = true, isLoadingProfile = false) }
            }
        }
    }

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
            val result = currencyRepository.getCurrencies()
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
        val uid = firebaseManager.auth().currentUser?.uid ?: run {
            _uiState.update { it.copy(error = "Not signed in — please go back and sign in again") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            var lastError: Exception? = null

            for (attempt in 0..2) {
                try {
                    profileRepository.saveProfile(
                        uid = uid,
                        deviceId = deviceId,
                        deviceName = state.deviceName.trim(),
                        isPrimary = state.isPrimary,
                        defaultCurrency = state.selectedCurrency,
                    )
                    configStore.saveDeviceIdentity(
                        deviceId = deviceId,
                        deviceName = state.deviceName.trim(),
                        isPrimary = state.isPrimary,
                        hasCompletedSetup = true,
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
