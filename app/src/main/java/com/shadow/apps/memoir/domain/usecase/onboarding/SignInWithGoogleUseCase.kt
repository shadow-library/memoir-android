package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.repository.AuthRepository
import javax.inject.Inject

/*
 * Google sign-in
 *
 * Exchanges a Google ID token for a Firebase Auth session.
 */
class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    /*
     * Execution
     */
    suspend operator fun invoke(idToken: String) {
        authRepository.signInWithGoogleIdToken(idToken)
    }
}
