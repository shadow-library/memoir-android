package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.model.DeviceIdentity
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import javax.inject.Inject

/*
 * Complete device setup
 *
 * Registers the current Android device and stores local setup completion.
 */
class CompleteDeviceSetupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val configRepository: ConfigRepository,
    private val profileRepository: ProfileRepository,
) {
    /*
     * Execution
     */
    suspend operator fun invoke(
        deviceId: String,
        deviceName: String,
        isPrimary: Boolean,
        defaultCurrency: String,
    ) {
        val uid = authRepository.currentUserId() ?: error("Not signed in — please go back and sign in again")
        val trimmedDeviceName = deviceName.trim()

        profileRepository.saveProfile(
            uid = uid,
            deviceId = deviceId,
            deviceName = trimmedDeviceName,
            isPrimary = isPrimary,
            defaultCurrency = defaultCurrency,
        )
        configRepository.saveDeviceIdentity(
            DeviceIdentity(
                deviceId = deviceId,
                deviceName = trimmedDeviceName,
                isPrimary = isPrimary,
                hasCompletedSetup = true,
            ),
        )
    }
}
