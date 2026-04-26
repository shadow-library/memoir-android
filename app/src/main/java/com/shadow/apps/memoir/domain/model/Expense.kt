package com.shadow.apps.memoir.domain.model

import java.time.LocalDate

data class Expense(
    val id: String,
    val amount: Double,
    val currency: String,
    val amountINR: Double,
    val fxRateUsed: Double?,
    val fxDate: String?,
    val merchant: String,
    val categoryId: String,
    val note: String,
    val date: LocalDate,
    val isSubscriptionPayment: Boolean = false,
    val subscriptionId: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val deviceId: String,
    val version: Int = 1,
    val schemaVersion: Int = 1,
)
