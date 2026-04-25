package com.shadow.apps.memoir.domain.repository

/*
 * Auth repository
 *
 * Firebase Auth contract used by onboarding and startup decisions.
 */
interface AuthRepository {
    fun currentUserId(): String?
    fun isSignedIn(): Boolean
    suspend fun signInWithGoogleIdToken(idToken: String)
}
