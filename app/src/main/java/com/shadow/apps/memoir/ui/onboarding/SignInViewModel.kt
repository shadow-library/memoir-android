package com.shadow.apps.memoir.ui.onboarding

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.shadow.apps.memoir.data.EncryptedConfigStore
import com.shadow.apps.memoir.data.FirebaseManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val configStore: EncryptedConfigStore,
    private val firebaseManager: FirebaseManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    init {
        val creds = configStore.loadFirebaseCredentials()
        if (creds != null) {
            if (!firebaseManager.isInitialised()) {
                firebaseManager.initialise(creds)
            }
            _uiState.update {
                it.copy(
                    projectId = creds.projectId,
                    webClientId = creds.webClientId ?: "",
                )
            }
        }
    }

    fun signIn(activityContext: Context, onSuccess: () -> Unit) {
        val webClientId = _uiState.value.webClientId
        if (webClientId.isBlank()) {
            _uiState.update { it.copy(error = "Web client ID missing \u2014 re-upload google-services.json") }
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
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                suspendCancellableCoroutine { cont ->
                    firebaseManager.auth().signInWithCredential(firebaseCredential)
                        .addOnSuccessListener { cont.resume(Unit) }
                        .addOnFailureListener { cont.resumeWithException(it) }
                }

                _uiState.update { it.copy(isSigningIn = false) }
                onSuccess()
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
