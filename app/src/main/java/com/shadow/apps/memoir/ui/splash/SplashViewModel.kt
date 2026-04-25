package com.shadow.apps.memoir.ui.splash

import androidx.lifecycle.ViewModel
import com.shadow.apps.memoir.domain.usecase.appstartup.GetStartupDestinationUseCase
import com.shadow.apps.memoir.domain.usecase.appstartup.InitialiseFirebaseFromStoredConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/*
 * Splash ViewModel
 *
 * Owns startup initialization and route decision so MainActivity stays thin.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initialiseFirebaseFromStoredConfig: InitialiseFirebaseFromStoredConfigUseCase,
    private val getStartupDestination: GetStartupDestinationUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    /*
     * Events
     */
    fun resolveStartupDestination() {
        initialiseFirebaseFromStoredConfig()
        _uiState.update { it.copy(destination = getStartupDestination()) }
    }
}
