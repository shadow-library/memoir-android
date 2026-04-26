package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.data.firebase.FirebaseAuthApi
import com.shadow.apps.memoir.data.firebase.GoogleOAuthApi
import com.shadow.apps.memoir.data.firebase.TokenLookupRequest
import javax.inject.Inject

sealed class VerificationResult {
    data object Valid : VerificationResult()
    data object InvalidApiKey : VerificationResult()
    data object InvalidWebClientId : VerificationResult()
    data object NetworkError : VerificationResult()
}

/*
 * Verify Firebase credentials via two REST probes:
 *
 * 1. API key — POST accounts:lookup with a dummy token. A 400 containing
 *    "INVALID_ID_TOKEN" proves the key is accepted by a real Firebase project;
 *    anything else (e.g. "API_KEY_INVALID") means the key is wrong.
 *
 * 2. Web client ID — POST to Google's OAuth token endpoint with a dummy code.
 *    A 400 containing "The OAuth client was not found" means the client ID
 *    doesn't exist; any other error (invalid_grant, redirect_uri_mismatch…)
 *    means the client ID was recognised.
 */
class VerifyFirebaseCredentialsUseCase @Inject constructor(
    private val firebaseAuthApi: FirebaseAuthApi,
    private val googleOAuthApi: GoogleOAuthApi,
) {

    suspend operator fun invoke(apiKey: String, webClientId: String): VerificationResult = runCatching {
        val authResponse = firebaseAuthApi.lookupAccount(
            apiKey = apiKey,
            body = TokenLookupRequest(idToken = "dummy"),
        )
        val authErrorBody = authResponse.errorBody()?.string() ?: ""
        if (!authErrorBody.contains("INVALID_ID_TOKEN")) return@runCatching VerificationResult.InvalidApiKey

        val oauthResponse = googleOAuthApi.exchangeCode(
            grantType = "authorization_code",
            code = "dummy_code",
            clientId = webClientId,
            redirectUri = "http://localhost",
        )
        val oauthErrorBody = oauthResponse.errorBody()?.string() ?: ""
        if (oauthErrorBody.contains("The OAuth client was not found")) {
            VerificationResult.InvalidWebClientId
        } else {
            VerificationResult.Valid
        }
    }.getOrElse { VerificationResult.NetworkError }
}
