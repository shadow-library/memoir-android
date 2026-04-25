package com.shadow.apps.memoir.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Named app so repeated initialise() calls (process restart, config change) are safe no-ops.
@Singleton
class FirebaseManager @Inject constructor(@param:ApplicationContext private val context: Context) {

    private companion object {
        const val APP_NAME = "memoir"
    }

    fun initialise(credentials: FirebaseCredentials) {
        if (FirebaseApp.getApps(context).any { it.name == APP_NAME }) return

        val options = FirebaseOptions.Builder()
            .setProjectId(credentials.projectId)
            .setApplicationId(credentials.appId)
            .setApiKey(credentials.apiKey)
            .setStorageBucket(credentials.storageBucket)
            .apply { credentials.databaseUrl?.let { setDatabaseUrl(it) } }
            .build()

        FirebaseApp.initializeApp(context, options, APP_NAME)
    }

    fun isInitialised(): Boolean =
        FirebaseApp.getApps(context).any { it.name == APP_NAME }

    private fun app(): FirebaseApp = FirebaseApp.getInstance(APP_NAME)

    fun firestore(): FirebaseFirestore = FirebaseFirestore.getInstance(app())

    fun auth(): FirebaseAuth = FirebaseAuth.getInstance(app())

    fun storage(): FirebaseStorage = FirebaseStorage.getInstance(app())
}
