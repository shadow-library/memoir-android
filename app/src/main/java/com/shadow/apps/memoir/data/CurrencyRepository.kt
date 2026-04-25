package com.shadow.apps.memoir.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class CurrencyOption(
    val code: String,
    val name: String,
) {
    val label: String = "$code — $name"
}

data class ExchangeRate(
    val base: String,
    val quote: String,
    val rate: Double,
    val date: String,
)

data class CurrencyListResult(
    val currencies: List<CurrencyOption>,
    val isFallback: Boolean,
)

@Singleton
class CurrencyRepository @Inject constructor() {

    private val preferredCurrencyCodes = listOf("INR", "AED")
    private val fallbackCurrencies = listOf(
        CurrencyOption("INR", "Indian Rupee"),
        CurrencyOption("AED", "United Arab Emirates Dirham"),
    )
    private val rateCache = mutableMapOf<RateCacheKey, ExchangeRate>()

    suspend fun getCurrencies(): CurrencyListResult = runCatching {
        CurrencyListResult(currencies = fetchCurrencies(), isFallback = false)
    }.getOrDefault(CurrencyListResult(currencies = fallbackCurrencies, isFallback = true))

    suspend fun getRate(base: String, quote: String = "INR"): ExchangeRate {
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

    private suspend fun fetchCurrencies(): List<CurrencyOption> = withContext(Dispatchers.IO) {
        val response = getJson("https://api.frankfurter.dev/v2/currencies")
        val currencies = JSONArray(response)
        val options = buildList {
            for (index in 0 until currencies.length()) {
                val currency = currencies.getJSONObject(index)
                val code = currency.getString("iso_code")
                val name = currency.getString("name")
                add(CurrencyOption(code = code, name = name))
            }
        }

        options.sortedWith(
            compareBy<CurrencyOption> { option ->
                val preferredIndex = preferredCurrencyCodes.indexOf(option.code)
                if (preferredIndex == -1) preferredCurrencyCodes.size else preferredIndex
            }.thenBy { it.code },
        )
    }

    private suspend fun fetchRate(base: String, quote: String): ExchangeRate = withContext(Dispatchers.IO) {
        val response = getJson("https://api.frankfurter.dev/v2/rate/$base/$quote")
        val rate = JSONObject(response)
        ExchangeRate(
            base = rate.getString("base"),
            quote = rate.getString("quote"),
            rate = rate.getDouble("rate"),
            date = rate.getString("date"),
        )
    }

    private fun getJson(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000

        return try {
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                error("Frankfurter API request failed with HTTP $responseCode")
            }
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private data class RateCacheKey(
        val base: String,
        val quote: String,
        val date: String,
    )
}
