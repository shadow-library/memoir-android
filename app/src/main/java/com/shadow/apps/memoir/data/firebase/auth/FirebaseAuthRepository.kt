package com.shadow.apps.memoir.data.firebase.auth

import com.google.firebase.auth.GoogleAuthProvider
import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.repository.AuthRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/*
 * Firebase Auth
 *
 * Adapts Firebase Auth to the domain auth contract.
 */
@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
) : AuthRepository {

    /*
     * Reads
     */
    override fun currentUserId(): String? =
        if (firebaseManager.isInitialised()) firebaseManager.auth().currentUser?.uid else null

    override fun isSignedIn(): Boolean = currentUserId() != null

    /*
     * Writes
     */
    override suspend fun signInWithGoogleIdToken(idToken: String): Unit =
        suspendCancellableCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseManager.auth().signInWithCredential(credential)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
}
