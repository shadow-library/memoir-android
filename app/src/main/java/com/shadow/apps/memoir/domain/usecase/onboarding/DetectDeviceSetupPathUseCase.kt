package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import javax.inject.Inject

/*
 * Device setup path
 *
 * Determines whether this Android device should default to primary setup.
 */
class DetectDeviceSetupPathUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) {
    /*
     * Execution
     */
    suspend operator fun invoke(): Boolean {
        val uid = authRepository.currentUserId() ?: return true
        return !profileRepository.profileExists(uid)
    }
}
