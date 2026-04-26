package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.EnabledCurrency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun observe(): Flow<List<EnabledCurrency>>

    suspend fun seedDefaults(defaultCurrencyCode: String, defaultCurrencyName: String)
}
