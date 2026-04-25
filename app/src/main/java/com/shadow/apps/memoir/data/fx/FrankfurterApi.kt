package com.shadow.apps.memoir.data.fx

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
 * Frankfurter API
 *
 * Retrofit endpoints for currency metadata and exchange rates.
 */
interface FrankfurterApi {
    @GET("currencies")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest")
    suspend fun getLatestRate(
        @Query("from") base: String,
        @Query("to") quote: String,
    ): FrankfurterRateResponse

    @GET("{date}")
    suspend fun getHistoricalRate(
        @Path("date") date: String,
        @Query("from") base: String,
        @Query("to") quote: String,
    ): FrankfurterRateResponse
}
