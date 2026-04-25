package com.shadow.apps.memoir.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shadow.apps.memoir.ui.home.HomeScreen
import com.shadow.apps.memoir.ui.onboarding.DeviceTypeScreen
import com.shadow.apps.memoir.ui.onboarding.FirebaseSetupScreen
import com.shadow.apps.memoir.ui.onboarding.GettingStartedScreen
import com.shadow.apps.memoir.ui.onboarding.ScanQrScreen
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
                onSetupNewVault = { navController.navigate(FirebaseSetup) },
                onPairExistingVault = { navController.navigate(ScanQr) },
            )
        }

        composable<FirebaseSetup> {
            FirebaseSetupScreen(
                onBack = { navController.popBackStack() },
                /** TODO: navigate to SignIn once that screen is implemented. */
                onContinue = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
            )
        }

        composable<ScanQr> {
            ScanQrScreen(
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onEnterManually = { navController.navigate(FirebaseSetup) },
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
