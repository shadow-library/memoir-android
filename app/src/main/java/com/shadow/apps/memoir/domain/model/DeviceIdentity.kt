package com.shadow.apps.memoir.domain.model

/*
 * Device identity
 *
 * Local Android device registration details saved after onboarding.
 */
data class DeviceIdentity(
    val deviceId: String,
    val deviceName: String,
    val isPrimary: Boolean,
    val hasCompletedSetup: Boolean,
)
