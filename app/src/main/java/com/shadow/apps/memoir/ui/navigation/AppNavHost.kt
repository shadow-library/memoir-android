package com.shadow.apps.memoir.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shadow.apps.memoir.ui.home.HomeScreen
import com.shadow.apps.memoir.ui.onboarding.DeviceTypeScreen
import com.shadow.apps.memoir.ui.onboarding.GettingStartedScreen
import com.shadow.apps.memoir.ui.splash.SplashScreen

/**
 * Single navigation graph for the entire app.
 */
@Composable
fun AppNavHost(navController: NavHostController, onboardingRequired: Boolean) {

    NavHost(navController = navController, startDestination = Splash) {

        /**
         * Splash
         * Always the entry point. Pops itself so the user can never navigate back
         * to the splash screen after it completes.
         */
        composable<Splash> {
            SplashScreen(onSplashComplete = {
                val dest: Any = if (onboardingRequired) GettingStarted else Home
                navController.navigate(dest) {
                    popUpTo<Splash> { inclusive = true }
                }
            })
        }

        /**
         * Getting Started flow
         */

        composable<GettingStarted> {
            GettingStartedScreen(onGetStarted = { navController.navigate(DeviceType) })
        }

        composable<DeviceType> {
            DeviceTypeScreen(
                onBack = { navController.popBackStack() },
                /** TODO: swap stubs for FirebaseSetup / ScanQr once those screens exist. */
                onSetupNewVault = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onPairExistingVault = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
            )
        }

        /**
         * Main App Screens
         */

        composable<Home> {
            HomeScreen()
        }
    }
}
