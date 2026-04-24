package com.shadow.apps.memoir.data

data class FirebaseCredentials(
    val projectId: String,
    val appId: String,
    val apiKey: String,
    val storageBucket: String,
    val databaseUrl: String? = null,
)
