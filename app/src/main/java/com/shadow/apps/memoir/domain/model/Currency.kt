package com.shadow.apps.memoir.domain.model

/*
 * Currency models
 *
 * Domain values used by setup and future expense conversion flows.
 */
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

data class EnabledCurrency(
    val code: String,
    val name: String,
    val isDefault: Boolean,
) {
    val label: String = "$code — $name"
}
