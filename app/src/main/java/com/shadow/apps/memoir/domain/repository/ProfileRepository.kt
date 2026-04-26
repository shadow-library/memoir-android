package com.shadow.apps.memoir.domain.repository

/*
 * Profile repository
 *
 * User profile and registered-device Firestore contract.
 */
interface ProfileRepository {
    suspend fun profileExists(uid: String): Boolean

    suspend fun saveProfile(
        uid: String,
        deviceId: String,
        deviceName: String,
        isPrimary: Boolean,
        defaultCurrency: String,
    )

    suspend fun getAppliedSeeds(uid: String): Set<String>

    suspend fun markSeedApplied(uid: String, seedId: String)
}
