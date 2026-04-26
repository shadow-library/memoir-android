package com.shadow.apps.memoir.domain.usecase.record

import com.shadow.apps.memoir.domain.model.Expense
import com.shadow.apps.memoir.domain.repository.ConfigRepository
import com.shadow.apps.memoir.domain.repository.ExpenseRepository
import com.shadow.apps.memoir.domain.repository.FxRepository
import java.util.UUID
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val fxRepository: FxRepository,
    private val configRepository: ConfigRepository,
) {
    suspend operator fun invoke(
        amount: Double,
        currency: String,
        merchant: String,
        categoryId: String,
        note: String,
        date: java.time.LocalDate,
    ): Result<Unit> {
        val now = System.currentTimeMillis()
        val deviceId = configRepository.loadDeviceId() ?: ""

        val (amountINR, fxRate, fxDate) = if (currency == "INR") {
            Triple(amount, 1.0, null)
        } else {
            runCatching { fxRepository.getRate(currency, "INR") }
                .fold(
                    onSuccess = { rate -> Triple(amount * rate.rate, rate.rate, rate.date) },
                    onFailure = { Triple(amount, null, null) },
                )
        }

        val expense = Expense(
            id = UUID.randomUUID().toString(),
            amount = amount,
            currency = currency,
            amountINR = amountINR,
            fxRateUsed = fxRate,
            fxDate = fxDate,
            merchant = merchant,
            categoryId = categoryId,
            note = note,
            date = date,
            createdAt = now,
            updatedAt = now,
            deviceId = deviceId,
        )

        return expenseRepository.add(expense)
    }
}
