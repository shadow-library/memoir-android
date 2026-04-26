package com.shadow.apps.memoir.data.firebase.expense

import com.google.firebase.Timestamp
import com.shadow.apps.memoir.data.firebase.FirebaseManager
import com.shadow.apps.memoir.domain.model.Expense
import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ExpenseRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseExpenseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val authRepository: AuthRepository,
) : ExpenseRepository {

    private fun expensesRef(uid: String) =
        firebaseManager.firestore().collection("users").document(uid).collection("expenses")

    override suspend fun add(expense: Expense): Result<Unit> {
        val uid = authRepository.currentUserId()
            ?: return Result.failure(IllegalStateException("Not signed in"))

        return suspendCancellableCoroutine { cont ->
            val data = mapOf(
                "id" to expense.id,
                "amount" to expense.amount,
                "currency" to expense.currency,
                "amountINR" to expense.amountINR,
                "fxRateUsed" to expense.fxRateUsed,
                "fxDate" to expense.fxDate,
                "merchant" to expense.merchant,
                "categoryId" to expense.categoryId,
                "note" to expense.note,
                "date" to expense.date.toString(),
                "isSubscriptionPayment" to expense.isSubscriptionPayment,
                "subscriptionId" to expense.subscriptionId,
                "createdAt" to Timestamp(expense.createdAt / 1000, ((expense.createdAt % 1000) * 1_000_000).toInt()),
                "updatedAt" to Timestamp(expense.updatedAt / 1000, ((expense.updatedAt % 1000) * 1_000_000).toInt()),
                "deviceId" to expense.deviceId,
                "version" to expense.version,
                "schemaVersion" to expense.schemaVersion,
            )

            expensesRef(uid).document(expense.id).set(data)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}
