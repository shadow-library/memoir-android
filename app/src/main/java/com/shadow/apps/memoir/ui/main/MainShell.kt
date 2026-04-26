package com.shadow.apps.memoir.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shadow.apps.memoir.ui.main.components.MainBottomBar
import com.shadow.apps.memoir.ui.main.diary.DiaryScreen
import com.shadow.apps.memoir.ui.main.home.HomeScreen
import com.shadow.apps.memoir.ui.main.money.MoneyScreen
import com.shadow.apps.memoir.ui.main.more.MoreScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainShell(
    onAddExpense: () -> Unit,
    onAddDiary: () -> Unit,
) {
    val tabNavController = rememberNavController()
    var selectedTab by rememberSaveable(
        stateSaver = Saver(
            save = { it.ordinal },
            restore = { TabDestination.entries[it] },
        ),
    ) { mutableStateOf(TabDestination.TODAY) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            MainBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    navigateToTab(tabNavController, tab)
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = TodayRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<TodayRoute> {
                HomeScreen(onAddExpense = onAddExpense, onAddDiary = onAddDiary)
            }
            composable<MoneyRoute> { MoneyScreen() }
            composable<DiaryRoute> { DiaryScreen() }
            composable<MoreRoute> { MoreScreen() }
        }
    }
}

private fun navigateToTab(navController: NavHostController, tab: TabDestination) {
    navController.navigate(tab.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
