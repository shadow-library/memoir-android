package com.shadow.apps.memoir.ui.onboarding.accountsetup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shadow.apps.memoir.domain.seed.SeedContext
import com.shadow.apps.memoir.domain.usecase.onboarding.RunPendingSeedsUseCase
import com.shadow.apps.memoir.ui.navigation.AccountSetup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSetupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val runPendingSeeds: RunPendingSeedsUseCase,
) : ViewModel() {

    private companion object {
        const val MIN_LOADER_MS = 1000L
    }

    private val args: AccountSetup = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(AccountSetupUiState())
    val uiState: StateFlow<AccountSetupUiState> = _uiState.asStateFlow()

    init {
        run()
    }

    fun retry() = run()

    private fun run() {
        viewModelScope.launch {
            _uiState.update { AccountSetupUiState(phase = AccountSetupUiState.Phase.Loading, error = null) }
            val context = SeedContext(
                defaultCurrencyCode = args.defaultCurrencyCode,
                defaultCurrencyName = args.defaultCurrencyName,
            )
            val seedJob = async { runPendingSeeds(context) }
            val timerJob = async { delay(MIN_LOADER_MS) }
            awaitAll(seedJob, timerJob)
            seedJob.await().fold(
                onSuccess = {
                    _uiState.update { it.copy(phase = AccountSetupUiState.Phase.Success) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.localizedMessage ?: "Could not set up your account.") }
                },
            )
        }
    }
}
