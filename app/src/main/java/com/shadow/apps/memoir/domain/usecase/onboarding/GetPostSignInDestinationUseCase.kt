package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import javax.inject.Inject

/*
 * Post sign-in destination
 *
 * Decides whether a signed-in device still needs setup.
 */
class GetPostSignInDestinationUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
) {
    /*
     * Execution
     */
    operator fun invoke(): StartupDestination =
        if (configRepository.hasCompletedSetup()) StartupDestination.Home else StartupDestination.DeviceSetup
}
