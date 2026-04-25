package com.shadow.apps.memoir.domain.usecase.appstartup

import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.FirebaseAppRepository
import javax.inject.Inject

/*
 * Startup initialization
 *
 * Initializes the user's Firebase app from encrypted local config when present.
 */
class InitialiseFirebaseFromStoredConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
    private val firebaseAppRepository: FirebaseAppRepository,
) {
    /*
     * Execution
     */
    operator fun invoke() {
        val credentials = configRepository.loadFirebaseCredentials() ?: return
        if (!firebaseAppRepository.isInitialised()) {
            firebaseAppRepository.initialise(credentials)
        }
    }
}
