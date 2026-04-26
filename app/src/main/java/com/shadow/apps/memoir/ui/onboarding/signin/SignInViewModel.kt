package com.shadow.apps.memoir.ui.onboarding.signin

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.domain.usecase.onboarding.GetPostSignInDestinationUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.LoadFirebaseCredentialsUseCase
import com.shadow.apps.memoir.domain.usecase.onboarding.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loadFirebaseCredentials: LoadFirebaseCredentialsUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val getPostSignInDestination: GetPostSignInDestinationUseCase,
) : ViewModel() {

    /*
     * State
     */
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    /*
     * Initialization
     */
    init {
        val creds = loadFirebaseCredentials()
        if (creds != null) {
            _uiState.update {
                it.copy(
                    projectId = creds.projectId,
                    webClientId = creds.webClientId,
                )
            }
        }
    }

    /*
     * Events
     */
    fun signIn(activityContext: Context, onSuccess: (StartupDestination) -> Unit) {
        val webClientId = _uiState.value.webClientId
        if (webClientId.isBlank()) {
            _uiState.update { it.copy(error = "Web client ID missing — re-upload google-services.json") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSigningIn = true, error = null) }
            try {
                val credentialManager = CredentialManager.create(activityContext)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(activityContext, request)
                val credential = result.credential
                if (credential !is CustomCredential ||
                    credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    _uiState.update { it.copy(isSigningIn = false, error = "Unexpected credential type") }
                    return@launch
                }

                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                signInWithGoogle(idToken)

                _uiState.update { it.copy(isSigningIn = false) }
                onSuccess(getPostSignInDestination())
            } catch (_: GetCredentialCancellationException) {
                _uiState.update { it.copy(isSigningIn = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSigningIn = false, error = e.localizedMessage ?: "Sign-in failed")
                }
            }
        }
    }
}
