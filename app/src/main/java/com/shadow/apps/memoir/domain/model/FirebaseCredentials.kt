package com.shadow.apps.memoir.domain.model

/*
 * Firebase credentials
 *
 * User-owned Firebase project configuration collected during onboarding.
 */
data class FirebaseCredentials(
    val projectId: String,
    val appId: String,
    val apiKey: String,
    val storageBucket: String,
    val webClientId: String,
)
