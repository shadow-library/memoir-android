package com.shadow.apps.memoir.domain.usecase.appstartup

import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import javax.inject.Inject

/*
 * Startup destination
 *
 * Decides the first real screen after splash.
 */
class GetStartupDestinationUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
    private val authRepository: AuthRepository,
) {
    /*
     * Execution
     */
    operator fun invoke(): StartupDestination = when {
        !configRepository.hasFirebaseCredentials() -> StartupDestination.GettingStarted
        !authRepository.isSignedIn() -> StartupDestination.SignIn
        !configRepository.hasCompletedSetup() -> StartupDestination.DeviceSetup
        else -> StartupDestination.Home
    }
}
