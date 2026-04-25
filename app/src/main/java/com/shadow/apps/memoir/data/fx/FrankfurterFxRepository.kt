package com.shadow.apps.memoir.data.fx

import com.shadow.apps.memoir.domain.model.CurrencyListResult
import com.shadow.apps.memoir.domain.model.CurrencyOption
import com.shadow.apps.memoir.domain.model.ExchangeRate
import com.shadow.apps.memoir.domain.repository.FxRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Frankfurter FX repository
 *
 * Fetches currencies and exchange rates with an in-memory daily cache.
 */
@Singleton
class FrankfurterFxRepository @Inject constructor(
    private val frankfurterApi: FrankfurterApi,
) : FxRepository {

    private val preferredCurrencyCodes = listOf("INR", "AED")
    private val fallbackCurrencies = listOf(
        CurrencyOption("INR", "Indian Rupee"),
        CurrencyOption("AED", "United Arab Emirates Dirham"),
    )
    private val rateCache = mutableMapOf<RateCacheKey, ExchangeRate>()

    /*
     * Reads
     */
    override suspend fun getCurrencies(): CurrencyListResult = runCatching {
        CurrencyListResult(currencies = fetchCurrencies(), isFallback = false)
    }.getOrDefault(CurrencyListResult(currencies = fallbackCurrencies, isFallback = true))

    override suspend fun getRate(base: String, quote: String): ExchangeRate {
        val normalizedBase = base.uppercase()
        val normalizedQuote = quote.uppercase()
        if (normalizedBase == normalizedQuote) {
            return ExchangeRate(
                base = normalizedBase,
                quote = normalizedQuote,
                rate = 1.0,
                date = LocalDate.now().toString(),
            )
        }

        val cacheKey = RateCacheKey(
            base = normalizedBase,
            quote = normalizedQuote,
            date = LocalDate.now().toString(),
        )
        rateCache[cacheKey]?.let { return it }

        return fetchRate(normalizedBase, normalizedQuote).also { rate ->
            rateCache[cacheKey] = rate
        }
    }

    /*
     * Private helpers
     */
    private suspend fun fetchCurrencies(): List<CurrencyOption> =
        FxMapper.currenciesFromResponse(frankfurterApi.getCurrencies())
            .sortedWith(
                compareBy<CurrencyOption> { option ->
                    val preferredIndex = preferredCurrencyCodes.indexOf(option.code)
                    if (preferredIndex == -1) preferredCurrencyCodes.size else preferredIndex
                }.thenBy { it.code },
            )

    private suspend fun fetchRate(base: String, quote: String): ExchangeRate =
        FxMapper.exchangeRateFromResponse(
            response = frankfurterApi.getLatestRate(base = base, quote = quote),
            quote = quote,
        )

    private data class RateCacheKey(
        val base: String,
        val quote: String,
        val date: String,
    )
}
