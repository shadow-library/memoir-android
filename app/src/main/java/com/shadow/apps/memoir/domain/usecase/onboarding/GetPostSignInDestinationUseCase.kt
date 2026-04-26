package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.usecase.appstartup.HasPendingSeedsUseCase
import javax.inject.Inject

/*
 * Post sign-in destination
 *
 * Decides whether a signed-in device still needs setup.
 */
class GetPostSignInDestinationUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
    private val hasPendingSeeds: HasPendingSeedsUseCase,
) {
    suspend operator fun invoke(): StartupDestination {
        if (!configRepository.hasCompletedSetup()) return StartupDestination.DeviceSetup
        return if (hasPendingSeeds()) StartupDestination.AccountSetup else StartupDestination.Home
    }
}
