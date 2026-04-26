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
    private val hasPendingSeeds: HasPendingSeedsUseCase,
) {
    suspend operator fun invoke(): StartupDestination {
        val base = when {
            !configRepository.hasFirebaseCredentials() -> StartupDestination.GettingStarted
            !authRepository.isSignedIn() -> StartupDestination.SignIn
            !configRepository.hasCompletedSetup() -> StartupDestination.DeviceSetup
            else -> StartupDestination.Home
        }
        return if (base == StartupDestination.Home && hasPendingSeeds()) {
            StartupDestination.AccountSetup
        } else base
    }
}
