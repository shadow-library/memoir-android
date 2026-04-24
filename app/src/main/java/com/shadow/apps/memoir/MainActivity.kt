package com.shadow.apps.memoir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.shadow.apps.memoir.data.EncryptedConfigStore
import com.shadow.apps.memoir.data.FirebaseManager
import com.shadow.apps.memoir.ui.home.HomeScreen
import com.shadow.apps.memoir.ui.onboarding.OnboardingScreen
import com.shadow.apps.memoir.ui.splash.SplashScreen
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var configStore: EncryptedConfigStore
    @Inject lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configStore.loadFirebaseCredentials()?.let { firebaseManager.initialise(it) }
        enableEdgeToEdge()
        setContent {
            ShadowMemoirTheme {
                // Splash is always the entry point; routing happens in onSplashComplete.
                var screen by rememberSaveable { mutableStateOf(AppScreen.Splash) }

                when (screen) {
                    AppScreen.Splash ->
                        SplashScreen(onSplashComplete = {
                            screen = if (shouldShowOnboarding()) {
                                AppScreen.Onboarding
                            } else {
                                AppScreen.Home
                            }
                        })

                    AppScreen.Onboarding ->
                        OnboardingScreen(onGetStarted = { screen = AppScreen.Home })

                    AppScreen.Home ->
                        HomeScreen()
                }
            }
        }
    }

    private fun shouldShowOnboarding() = !configStore.hasFirebaseCredentials()
}

private enum class AppScreen { Splash, Onboarding, Home }
