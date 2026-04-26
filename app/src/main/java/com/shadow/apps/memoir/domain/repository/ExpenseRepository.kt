package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.Expense

interface ExpenseRepository {
    suspend fun add(expense: Expense): Result<Unit>
}
