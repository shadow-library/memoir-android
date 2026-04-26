package com.shadow.apps.memoir.data.firebase.category

import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.model.Category
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.CategoryRepository
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
class FirebaseCategoryRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val authRepository: AuthRepository,
) : CategoryRepository {

    companion object {
        private val DEFAULT_CATEGORIES = listOf(
            Triple("Groceries", "emerald", true),
            Triple("Dining", "amber", true),
            Triple("Transport", "cyan", true),
            Triple("Shopping", "violet", true),
            Triple("Bills", "slate", true),
            Triple("Health", "emerald", true),
            Triple("Entertainment", "violet", true),
            Triple("Other", "slate", true),
        )
    }

    private fun categoriesRef(uid: String) =
        firebaseManager.firestore().collection("users").document(uid).collection("categories")

    override fun observe(): Flow<List<Category>> {
        val uid = authRepository.currentUserId() ?: return emptyFlow()

        return callbackFlow {
            val listener = categoriesRef(uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val categories = snapshot?.documents?.mapNotNull { doc ->
                    Category(
                        id = doc.id,
                        name = doc.getString("name") ?: return@mapNotNull null,
                        colorToken = doc.getString("colorToken") ?: "slate",
                        isDefault = doc.getBoolean("isDefault") ?: false,
                    )
                } ?: emptyList()
                trySend(categories)
            }
            awaitClose { listener.remove() }
        }
    }

    override suspend fun seedDefaultsIfMissing() {
        val uid = authRepository.currentUserId() ?: return

        val ref = categoriesRef(uid)
        val exists = suspendCancellableCoroutine { cont ->
            ref.limit(1).get()
                .addOnSuccessListener { cont.resume(!it.isEmpty) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

        if (exists) return

        val batch = firebaseManager.firestore().batch()
        DEFAULT_CATEGORIES.forEach { (name, colorToken, isDefault) ->
            val docRef = ref.document()
            batch.set(
                docRef,
                mapOf(
                    "name" to name,
                    "colorToken" to colorToken,
                    "isDefault" to isDefault,
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
