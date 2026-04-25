package com.shadow.apps.memoir.data.fx

/*
 * FX DTOs
 *
 * Network response models returned by Frankfurter.
 */
data class FrankfurterRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
)
