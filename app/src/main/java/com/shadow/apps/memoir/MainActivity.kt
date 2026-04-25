package com.shadow.apps.memoir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.shadow.apps.memoir.ui.navigation.AppNavHost
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*
     * Activity setup
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShadowMemoirTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
