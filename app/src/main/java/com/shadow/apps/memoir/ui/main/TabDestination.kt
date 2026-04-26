package com.shadow.apps.memoir.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/** ── Tab route objects (typed NavHost destinations) ──────────────────────── */

@Serializable object TodayRoute
@Serializable object MoneyRoute
@Serializable object DiaryRoute
@Serializable object MoreRoute

/** ── Tab descriptor (UI metadata — never serialized) ─────────────────────── */

enum class TabDestination(
    val route: Any,
    val routeClass: KClass<*>,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    TODAY(
        route = TodayRoute,
        routeClass = TodayRoute::class,
        label = "Today",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    MONEY(
        route = MoneyRoute,
        routeClass = MoneyRoute::class,
        label = "Money",
        selectedIcon = Icons.Filled.CreditCard,
        unselectedIcon = Icons.Outlined.CreditCard,
    ),
    DIARY(
        route = DiaryRoute,
        routeClass = DiaryRoute::class,
        label = "Diary",
        selectedIcon = Icons.Filled.AutoStories,
        unselectedIcon = Icons.Outlined.AutoStories,
    ),
    MORE(
        route = MoreRoute,
        routeClass = MoreRoute::class,
        label = "More",
        selectedIcon = Icons.Filled.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz,
    ),
    ;

    companion object {
        val all: List<TabDestination> = entries
    }
}
