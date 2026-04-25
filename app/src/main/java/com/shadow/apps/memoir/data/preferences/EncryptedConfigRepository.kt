package com.shadow.apps.memoir.data.preferences

import com.shadow.apps.memoir.domain.model.DeviceIdentity
import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Config repository
 *
 * Domain-facing adapter for secure local setup state.
 */
@Singleton
class EncryptedConfigRepository @Inject constructor(
    private val configStore: EncryptedConfigStore,
) : ConfigRepository {

    /*
     * Firebase credentials
     */
    override fun saveFirebaseCredentials(credentials: FirebaseCredentials) =
        configStore.saveFirebaseCredentials(credentials)

    override fun loadFirebaseCredentials(): FirebaseCredentials? =
        configStore.loadFirebaseCredentials()

    override fun clearFirebaseCredentials() =
        configStore.clearFirebaseCredentials()

    override fun hasFirebaseCredentials(): Boolean =
        configStore.hasFirebaseCredentials()

    /*
     * Device identity
     */
    override fun saveDeviceIdentity(identity: DeviceIdentity) =
        configStore.saveDeviceIdentity(identity)

    override fun loadDeviceId(): String? =
        configStore.loadDeviceId()

    override fun loadDeviceIsPrimary(): Boolean =
        configStore.loadDeviceIsPrimary()

    override fun hasCompletedSetup(): Boolean =
        configStore.hasCompletedSetup()
}
