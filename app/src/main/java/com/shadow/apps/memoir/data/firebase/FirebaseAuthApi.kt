package com.shadow.apps.memoir.data.firebase

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class TokenLookupRequest(val idToken: String)

interface FirebaseAuthApi {

    @POST("v1/accounts:lookup")
    suspend fun lookupAccount(
        @Query("key") apiKey: String,
        @Body body: TokenLookupRequest,
    ): Response<Unit>
}
