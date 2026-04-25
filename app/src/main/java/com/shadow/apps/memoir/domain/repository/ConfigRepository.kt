package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.DeviceIdentity
import com.shadow.apps.memoir.domain.model.FirebaseCredentials

/*
 * Config repository
 *
 * Secure local storage contract for Firebase configuration and device setup.
 */
interface ConfigRepository {
    fun saveFirebaseCredentials(credentials: FirebaseCredentials)
    fun loadFirebaseCredentials(): FirebaseCredentials?
    fun clearFirebaseCredentials()
    fun hasFirebaseCredentials(): Boolean
    fun saveDeviceIdentity(identity: DeviceIdentity)
    fun loadDeviceId(): String?
    fun loadDeviceIsPrimary(): Boolean
    fun hasCompletedSetup(): Boolean
}
