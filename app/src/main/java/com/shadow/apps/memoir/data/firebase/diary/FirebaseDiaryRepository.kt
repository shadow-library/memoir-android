package com.shadow.apps.memoir.data.firebase.diary

import com.google.firebase.Timestamp
import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.model.DiaryEntry
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.DiaryRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseDiaryRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val authRepository: AuthRepository,
) : DiaryRepository {

    private fun diaryRef(uid: String) =
        firebaseManager.firestore().collection("users").document(uid).collection("diary")

    override suspend fun add(entry: DiaryEntry): Result<Unit> {
        val uid = authRepository.currentUserId()
            ?: return Result.failure(IllegalStateException("Not signed in"))

        return suspendCancellableCoroutine { cont ->
            val data = mapOf(
                "id" to entry.id,
                "content" to entry.content,
                "tags" to entry.tags,
                "date" to entry.date.toString(),
                "createdAt" to Timestamp(entry.createdAt / 1000, ((entry.createdAt % 1000) * 1_000_000).toInt()),
                "updatedAt" to Timestamp(entry.updatedAt / 1000, ((entry.updatedAt % 1000) * 1_000_000).toInt()),
                "deviceId" to entry.deviceId,
                "version" to entry.version,
                "schemaVersion" to entry.schemaVersion,
            )

            diaryRef(uid).document(entry.id).set(data)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}
