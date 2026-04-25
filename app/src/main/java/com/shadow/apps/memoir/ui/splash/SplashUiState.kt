package com.shadow.apps.memoir.ui.splash

import com.shadow.apps.memoir.domain.model.StartupDestination

/*
 * UI state
 *
 * Splash render state and destination decision.
 */
data class SplashUiState(
    val destination: StartupDestination? = null,
)
