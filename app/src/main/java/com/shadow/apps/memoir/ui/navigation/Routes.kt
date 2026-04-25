package com.shadow.apps.memoir.ui.navigation

import kotlinx.serialization.Serializable

/** ── Splash ──────────────────────────────────────────────────────────────── */

@Serializable object Splash

/** ── Getting Started flow ────────────────────────────────────────────────── */

@Serializable object GettingStarted
@Serializable object DeviceType
@Serializable object FirebaseSetup
@Serializable object SignIn

/** ── Device setup ────────────────────────────────────────────────────────── */

@Serializable object GenerateQr
@Serializable object ScanQr

/** ── Main app ────────────────────────────────────────────────────────────── */

@Serializable object Home

@Serializable object ExpenseList
@Serializable object AddExpense
@Serializable data class EditExpense(val expenseId: String)
@Serializable object ReceiptScan

@Serializable object SubscriptionList
@Serializable object AddSubscription
@Serializable data class EditSubscription(val subscriptionId: String)

@Serializable object HealthDashboard

@Serializable object DiaryList
@Serializable object AddDiaryEntry
@Serializable object ActionList
@Serializable data class LogAction(val actionId: String)

@Serializable object PatternsScreen

/** ── Utility ─────────────────────────────────────────────────────────────── */

@Serializable object Conflicts
@Serializable object Settings
