package com.shadow.apps.memoir.data.firebase.profile

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/*
 * Firebase profile repository
 *
 * Stores the user profile document and registered Android device metadata.
 */
@Singleton
class FirebaseProfileRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
) : ProfileRepository {

    /*
     * References
     */
    private fun userRef(uid: String) =
        firebaseManager.firestore().collection("users").document(uid)

    /*
     * Reads
     */
    override suspend fun profileExists(uid: String): Boolean = suspendCancellableCoroutine { cont ->
        userRef(uid).get()
            .addOnSuccessListener { cont.resume(it.exists()) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    /*
     * Writes
     */
    override suspend fun saveProfile(
        uid: String,
        deviceId: String,
        deviceName: String,
        isPrimary: Boolean,
        defaultCurrency: String,
    ): Unit = suspendCancellableCoroutine { cont ->
        val now = Timestamp.now()
        val deviceEntry = mapOf(
            "id" to deviceId,
            "name" to deviceName,
            "platform" to "android",
            "registeredAt" to now,
        )
        val ref = userRef(uid)

        firebaseManager.firestore().runTransaction { transaction ->
            val snapshot = transaction.get(ref)

            if (!snapshot.exists()) {
                transaction.set(
                    ref,
                    hashMapOf(
                        "uid" to uid,
                        "primaryDeviceId" to deviceId,
                        "defaultCurrency" to defaultCurrency,
                        "devices" to listOf(deviceEntry),
                        "createdAt" to now,
                    ),
                )
            } else {
                @Suppress("UNCHECKED_CAST")
                val existing = (snapshot.get("devices") as? List<Map<String, Any>>) ?: emptyList()
                val updatedDevices =
                    if (existing.any { it["id"] == deviceId }) existing else existing + deviceEntry

                val updates = mutableMapOf<String, Any>(
                    "devices" to updatedDevices,
                    "defaultCurrency" to defaultCurrency,
                )
                if (isPrimary) updates["primaryDeviceId"] = deviceId
                transaction.update(ref, updates)
            }
        }
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    /*
     * Seed tracker
     */
    override suspend fun getAppliedSeeds(uid: String): Set<String> = suspendCancellableCoroutine { cont ->
        userRef(uid).get()
            .addOnSuccessListener { snapshot ->
                @Suppress("UNCHECKED_CAST")
                val list = snapshot.get("appliedSeeds") as? List<String>
                cont.resume(list?.toSet() ?: emptySet())
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun markSeedApplied(uid: String, seedId: String): Unit = suspendCancellableCoroutine { cont ->
        userRef(uid).update("appliedSeeds", FieldValue.arrayUnion(seedId))
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}
