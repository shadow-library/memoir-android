package com.shadow.apps.memoir.data.firebase.currency

import com.google.firebase.Timestamp
import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.model.EnabledCurrency
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.CurrencyRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseCurrencyRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val authRepository: AuthRepository,
) : CurrencyRepository {

    private companion object {
        val ALWAYS_INCLUDED = listOf(
            "INR" to "Indian Rupee",
            "AED" to "United Arab Emirates Dirham",
        )
    }

    private fun currenciesRef(uid: String) =
        firebaseManager.firestore().collection("users").document(uid).collection("currencies")

    override fun observe(): Flow<List<EnabledCurrency>> {
        val uid = authRepository.currentUserId() ?: return emptyFlow()

        return callbackFlow {
            val listener = currenciesRef(uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    EnabledCurrency(
                        code = doc.id,
                        name = doc.getString("name") ?: return@mapNotNull null,
                        isDefault = doc.getBoolean("isDefault") ?: false,
                    )
                } ?: emptyList()
                trySend(items)
            }
            awaitClose { listener.remove() }
        }
    }

    override suspend fun seedDefaults(defaultCurrencyCode: String, defaultCurrencyName: String) {
        val uid = authRepository.currentUserId() ?: return
        val ref = currenciesRef(uid)

        val exists = suspendCancellableCoroutine { cont ->
            ref.limit(1).get()
                .addOnSuccessListener { cont.resume(!it.isEmpty) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
        if (exists) return

        val now = Timestamp.now()
        val entries = LinkedHashMap<String, String>().apply {
            ALWAYS_INCLUDED.forEach { (code, name) -> put(code, name) }
            put(defaultCurrencyCode, defaultCurrencyName)
        }

        val batch = firebaseManager.firestore().batch()
        entries.forEach { (code, name) ->
            batch.set(
                ref.document(code),
                mapOf(
                    "name" to name,
                    "isDefault" to (code == defaultCurrencyCode),
                    "addedAt" to now,
                ),
            )
        }
        suspendCancellableCoroutine { cont ->
            batch.commit()
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}
