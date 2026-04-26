package com.shadow.apps.memoir.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shadow.apps.memoir.domain.model.StartupDestination
import com.shadow.apps.memoir.ui.home.HomeScreen
import com.shadow.apps.memoir.ui.onboarding.devicesetup.DeviceSetupScreen
import com.shadow.apps.memoir.ui.onboarding.devicetype.DeviceTypeScreen
import com.shadow.apps.memoir.ui.onboarding.firebasesetup.FirebaseSetupScreen
import com.shadow.apps.memoir.ui.onboarding.gettingstarted.GettingStartedScreen
import com.shadow.apps.memoir.ui.onboarding.scanqr.ScanQrScreen
import com.shadow.apps.memoir.ui.onboarding.setupcomplete.SetupCompleteScreen
import com.shadow.apps.memoir.ui.onboarding.signin.SignInScreen
import com.shadow.apps.memoir.ui.splash.SplashScreen

/**
 * Single navigation graph for the entire app.
 */
@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Splash) {

        /**
         * Splash
         * Always the entry point. Pops itself so the user can never navigate back
         * to the splash screen after it completes.
         */
        composable<Splash> {
            SplashScreen(onSplashComplete = { destination ->
                val dest: Any = when (destination) {
                    StartupDestination.GettingStarted -> GettingStarted
                    StartupDestination.SignIn -> SignIn
                    StartupDestination.DeviceSetup -> DeviceSetup
                    StartupDestination.Home -> Home
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
                onContinue = { destination ->
                    val dest: Any = when (destination) {
                        StartupDestination.GettingStarted -> GettingStarted
                        StartupDestination.SignIn -> SignIn
                        StartupDestination.DeviceSetup -> DeviceSetup
                        StartupDestination.Home -> Home
                    }
                    navController.navigate(dest) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },
            )
        }

        composable<DeviceSetup> {
            DeviceSetupScreen(
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(SignIn) {
                            popUpTo<DeviceSetup> { inclusive = true }
                        }
                    }
                },
                onContinue = {
                    navController.navigate(SetupComplete) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
            )
        }

        composable<SetupComplete> {
            SetupCompleteScreen(
                onContinue = {
                    navController.navigate(Home) {
                        popUpTo<SetupComplete> { inclusive = true }
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
