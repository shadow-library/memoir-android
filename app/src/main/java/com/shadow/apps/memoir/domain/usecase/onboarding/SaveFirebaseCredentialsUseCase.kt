package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import javax.inject.Inject

/*
 * Save Firebase credentials
 *
 * Persists BYOF Firebase config into encrypted local storage.
 */
class SaveFirebaseCredentialsUseCase @Inject constructor(
    private val configRepository: ConfigRepository,
) {
    /*
     * Execution
     */
    operator fun invoke(credentials: FirebaseCredentials) {
        configRepository.saveFirebaseCredentials(credentials)
    }
}
