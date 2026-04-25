package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.CurrencyListResult
import com.shadow.apps.memoir.domain.model.ExchangeRate

/*
 * FX repository
 *
 * Currency list and exchange-rate contract.
 */
interface FxRepository {
    suspend fun getCurrencies(): CurrencyListResult
    suspend fun getRate(base: String, quote: String = "INR"): ExchangeRate
}
