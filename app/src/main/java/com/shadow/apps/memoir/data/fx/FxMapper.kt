package com.shadow.apps.memoir.data.fx

import com.shadow.apps.memoir.domain.model.CurrencyOption
import com.shadow.apps.memoir.domain.model.ExchangeRate

/*
 * FX mapping
 *
 * Converts Frankfurter API responses into domain currency models.
 */
internal object FxMapper {
    fun currenciesFromResponse(response: Map<String, String>): List<CurrencyOption> =
        response.map { (code, name) -> CurrencyOption(code = code, name = name) }

    fun exchangeRateFromResponse(response: FrankfurterRateResponse, quote: String): ExchangeRate =
        ExchangeRate(
            base = response.base,
            quote = quote,
            rate = response.rates.getValue(quote),
            date = response.date,
        )
}
