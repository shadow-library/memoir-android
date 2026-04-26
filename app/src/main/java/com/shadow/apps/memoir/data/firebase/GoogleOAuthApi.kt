package com.shadow.apps.memoir.data.firebase

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleOAuthApi {

    @POST("token")
    @FormUrlEncoded
    suspend fun exchangeCode(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String,
    ): Response<Unit>
}
