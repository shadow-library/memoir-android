package com.shadow.apps.memoir.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shadow.apps.memoir.domain.model.FirebaseCredentials
import com.shadow.apps.memoir.domain.repository.FirebaseAppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/*
 * Firebase app
 *
 * Owns dynamic initialization of the user's Firebase project.
 */
@Singleton
class FirebaseManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : FirebaseAppRepository {

    private companion object {
        const val APP_NAME = "memoir"
    }

    /*
     * Initialization
     */
    override fun initialise(credentials: FirebaseCredentials) {
        if (FirebaseApp.getApps(context).any { it.name == APP_NAME }) return

        val options = FirebaseOptions.Builder()
            .setProjectId(credentials.projectId)
            .setApplicationId(credentials.appId)
            .setApiKey(credentials.apiKey)
            .setStorageBucket(credentials.storageBucket)
            .build()

        FirebaseApp.initializeApp(context, options, APP_NAME)
    }

    override fun isInitialised(): Boolean =
        FirebaseApp.getApps(context).any { it.name == APP_NAME }

    /*
     * SDK access
     */
    private fun app(): FirebaseApp = FirebaseApp.getInstance(APP_NAME)

    fun firestore(): FirebaseFirestore = FirebaseFirestore.getInstance(app())

    fun auth(): FirebaseAuth = FirebaseAuth.getInstance(app())

    fun storage(): FirebaseStorage = FirebaseStorage.getInstance(app())
}
