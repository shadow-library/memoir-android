package com.shadow.apps.memoir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.shadow.apps.memoir.data.EncryptedConfigStore
import com.shadow.apps.memoir.data.FirebaseManager
import com.shadow.apps.memoir.ui.navigation.AppNavHost
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var configStore: EncryptedConfigStore

    @Inject
    lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configStore.loadFirebaseCredentials()?.let { firebaseManager.initialise(it) }
        /** Evaluated once here — stable across recompositions, no side-effects in setContent. */
        val hasCredentials = configStore.hasFirebaseCredentials()
        val isSignedIn = firebaseManager.isInitialised() && firebaseManager.auth().currentUser != null
        enableEdgeToEdge()
        setContent {
            ShadowMemoirTheme {
                val navController = rememberNavController()
                AppNavHost(navController, hasCredentials = hasCredentials, isSignedIn = isSignedIn)
            }
        }
    }
}
