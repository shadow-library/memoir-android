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
import com.shadow.apps.memoir.ui.onboarding.SignInScreen
import com.shadow.apps.memoir.ui.splash.SplashScreen

/**
 * Single navigation graph for the entire app.
 */
@Composable
fun AppNavHost(navController: NavHostController, hasCredentials: Boolean, isSignedIn: Boolean) {

    NavHost(navController = navController, startDestination = Splash) {

        /**
         * Splash
         * Always the entry point. Pops itself so the user can never navigate back
         * to the splash screen after it completes.
         */
        composable<Splash> {
            SplashScreen(onSplashComplete = {
                val dest: Any = when {
                    !hasCredentials -> GettingStarted
                    !isSignedIn -> SignIn
                    else -> Home
                }
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
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(GettingStarted) {
                            popUpTo<DeviceType> { inclusive = true }
                        }
                    }
                },
                onSetupNewVault = { navController.navigate(FirebaseSetup) },
                onPairExistingVault = { navController.navigate(ScanQr) },
            )
        }

        composable<FirebaseSetup> {
            FirebaseSetupScreen(
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(DeviceType) {
                            popUpTo<FirebaseSetup> { inclusive = true }
                        }
                    }
                },
                onContinue = { navController.navigate(SignIn) },
            )
        }

        composable<ScanQr> {
            ScanQrScreen(
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(DeviceType) {
                            popUpTo<ScanQr> { inclusive = true }
                        }
                    }
                },
                onContinue = { navController.navigate(SignIn) },
                onEnterManually = { navController.navigate(FirebaseSetup) },
            )
        }

        composable<SignIn> {
            SignInScreen(
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(FirebaseSetup) {
                            popUpTo<SignIn> { inclusive = true }
                        }
                    }
                },
                onContinue = {
                    navController.navigate(Home) {
                        popUpTo<SignIn> { inclusive = true }
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
