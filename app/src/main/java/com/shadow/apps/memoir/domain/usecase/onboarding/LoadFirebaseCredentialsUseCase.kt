package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.FirebaseAppRepository
import javax.inject.Inject

/*
 * Load Firebase credentials
 *
 * Reads stored Firebase config and initializes Firebase when needed.
 */
class LoadFirebaseCredentialsUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
    private val firebaseAppRepository: FirebaseAppRepository,
) {
    /*
     * Execution
     */
    operator fun invoke(): FirebaseCredentials? {
        val credentials = configRepository.loadFirebaseCredentials() ?: return null
        if (!firebaseAppRepository.isInitialised()) {
            firebaseAppRepository.initialise(credentials)
        }
        return credentials
    }
}
