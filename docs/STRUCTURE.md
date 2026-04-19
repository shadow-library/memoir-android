# Shadow Memoir — Android Project Structure

**Version:** 1.0
**Package:** `com.shadowlibrary.shadowmemoir`

---

## Overview

The project follows **Clean Architecture** with three layers: `data`, `domain`, and `ui`. Each feature lives in a self-contained folder within its layer. Dependencies only flow inward — `ui` knows about `domain`, `domain` knows nothing above it, `data` implements `domain` interfaces.

```
ui  →  domain  ←  data
```

---

## Full Package Tree

```
com.shadowlibrary.shadowmemoir/
│
├── data/
│   ├── firebase/
│   │   ├── FirebaseManager.kt
│   │   ├── ExpenseRepository.kt
│   │   ├── SubscriptionRepository.kt
│   │   ├── ActionRepository.kt
│   │   ├── DiaryRepository.kt
│   │   ├── HealthRepository.kt
│   │   ├── AnalyticsRepository.kt
│   │   └── ConflictRepository.kt
│   ├── health/
│   │   └── HealthConnectManager.kt
│   ├── ai/
│   │   └── GeminiReceiptParser.kt
│   ├── fx/
│   │   └── FxRateService.kt
│   ├── export/
│   │   └── ExportManager.kt
│   └── preferences/
│       └── EncryptedConfigStore.kt
│
├── domain/
│   ├── model/
│   │   ├── Expense.kt
│   │   ├── Subscription.kt
│   │   ├── ActionDefinition.kt
│   │   ├── ActionLog.kt
│   │   ├── DiaryEntry.kt
│   │   ├── HealthSnapshot.kt
│   │   ├── MonthlyAnalytics.kt
│   │   ├── Category.kt
│   │   └── Profile.kt
│   └── usecase/
│       ├── SaveExpenseUseCase.kt
│       ├── GetCashViewUseCase.kt
│       ├── GetBudgetViewUseCase.kt
│       ├── ComputeVirtualEntriesUseCase.kt
│       ├── LogActionUseCase.kt
│       ├── SaveDiaryEntryUseCase.kt
│       ├── SyncHealthDataUseCase.kt
│       └── ResolveConflictUseCase.kt
│
├── ui/
│   ├── theme/
│   │   ├── Theme.kt
│   │   ├── Color.kt
│   │   └── Type.kt
│   ├── navigation/
│   │   ├── AppNavHost.kt
│   │   └── Screen.kt
│   ├── onboarding/
│   │   ├── OnboardingScreen.kt
│   │   ├── OnboardingViewModel.kt
│   │   ├── OnboardingUiState.kt
│   │   └── FirebaseSetupScreen.kt
│   ├── expenses/
│   │   ├── ExpensesScreen.kt
│   │   ├── ExpensesViewModel.kt
│   │   ├── ExpensesUiState.kt
│   │   ├── AddEditExpenseScreen.kt
│   │   └── components/
│   │       ├── ExpenseCard.kt
│   │       ├── ExpenseListHeader.kt
│   │       └── BudgetViewToggle.kt
│   ├── scanner/
│   │   ├── ScannerScreen.kt
│   │   ├── ScannerViewModel.kt
│   │   └── ScannerUiState.kt
│   ├── subscriptions/
│   │   ├── SubscriptionsScreen.kt
│   │   ├── SubscriptionsViewModel.kt
│   │   ├── SubscriptionsUiState.kt
│   │   ├── AddEditSubscriptionScreen.kt
│   │   └── components/
│   │       └── SubscriptionCard.kt
│   ├── actions/
│   │   ├── ActionsScreen.kt
│   │   ├── ActionsViewModel.kt
│   │   ├── ActionsUiState.kt
│   │   ├── ActionDetailScreen.kt
│   │   └── components/
│   │       ├── ActionCard.kt
│   │       └── ActionInputControl.kt
│   ├── diary/
│   │   ├── DiaryScreen.kt
│   │   ├── DiaryViewModel.kt
│   │   ├── DiaryUiState.kt
│   │   ├── DiaryEntryScreen.kt
│   │   └── components/
│   │       ├── DiaryEntryCard.kt
│   │       └── MoodSelector.kt
│   ├── analytics/
│   │   ├── AnalyticsScreen.kt
│   │   ├── AnalyticsViewModel.kt
│   │   ├── AnalyticsUiState.kt
│   │   └── components/
│   │       ├── SpendTrendChart.kt
│   │       └── CategoryBreakdownChart.kt
│   ├── conflicts/
│   │   ├── ConflictsScreen.kt
│   │   ├── ConflictsViewModel.kt
│   │   ├── ConflictsUiState.kt
│   │   └── components/
│   │       └── ConflictDiffView.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       ├── SettingsViewModel.kt
│       ├── SettingsUiState.kt
│       └── DeviceManagementScreen.kt
│
├── workers/
│   ├── HealthSyncWorker.kt
│   ├── NudgeWorker.kt
│   ├── ActionReminderWorker.kt
│   └── SubscriptionRenewalWorker.kt
│
├── di/
│   └── AppModule.kt
│
└── MainActivity.kt
```

---

## Layer Rules

### `data/`

Talks to external systems — Firestore, Health Connect, Gemini, Frankfurter API, EncryptedSharedPreferences.

- Repositories expose `suspend fun` or `Flow` — never raw Firestore types to callers
- All Firestore-to-domain mapping happens inside repositories
- `FirebaseManager.kt` handles dynamic Firebase initialisation; injected into every repository
- `EncryptedConfigStore.kt` is the only class that reads/writes `EncryptedSharedPreferences`
- FX rates are cached in memory inside `FxRateService` — never persisted to disk

### `domain/`

Pure Kotlin — zero Android or Firebase imports allowed.

- `model/` — plain data classes, no logic, no annotations
- `usecase/` — one class per use case, one public `suspend operator fun invoke(...)` per class
- Use cases receive repository interfaces as constructor parameters (injected via Hilt)
- All business logic lives here: budget view computation, FX conversion, virtual entry generation, conflict detection

### `ui/`

Jetpack Compose screens and ViewModels.

- One folder per feature containing exactly: `Screen.kt`, `ViewModel.kt`, `UiState.kt`
- A `components/` sub-folder holds composables used only within that feature
- Composables shared across two or more features go in `ui/components/` (create this folder when the second use case appears)
- ViewModels expose a single `StateFlow<UiState>` and `fun` handlers for user events
- No repository or Firestore references inside any composable — all data flows through the ViewModel

---

## Feature Folder Convention

Every feature follows this exact shape:

```
expenses/
├── ExpensesScreen.kt       ← @Composable, collects uiState, calls vm functions
├── ExpensesViewModel.kt    ← holds StateFlow<ExpensesUiState>, calls use cases
├── ExpensesUiState.kt      ← data class describing all possible screen states
└── components/
    └── ExpenseCard.kt      ← composables used only by this feature
```

**UiState pattern:**

```kotlin
data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val viewMode: ViewMode = ViewMode.BUDGET,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class ViewMode { CASH, BUDGET }
```

**ViewModel pattern:**

```kotlin
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getCashView: GetCashViewUseCase,
    private val getBudgetView: GetBudgetViewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    fun switchView(mode: ViewMode) { ... }
}
```

**Screen pattern:**

```kotlin
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // render uiState, call viewModel functions on user events
}
```

---

## Naming Conventions

| Type | Pattern | Example |
|------|---------|---------|
| Screen | `<Feature>Screen.kt` | `ExpensesScreen.kt` |
| ViewModel | `<Feature>ViewModel.kt` | `ExpensesViewModel.kt` |
| UiState | `<Feature>UiState.kt` | `ExpensesUiState.kt` |
| Sub-screen | `<Action><Feature>Screen.kt` | `AddEditExpenseScreen.kt` |
| Repository | `<Entity>Repository.kt` | `ExpenseRepository.kt` |
| Use case | `<Verb><Noun>UseCase.kt` | `SaveExpenseUseCase.kt` |
| Worker | `<Task>Worker.kt` | `HealthSyncWorker.kt` |
| Feature component | `<Thing>.kt` inside `components/` | `ExpenseCard.kt` |

---

## Navigation

`ui/navigation/Screen.kt` defines all routes as a sealed class:

```kotlin
sealed class Screen(val route: String) {
    data object Expenses : Screen("expenses")
    data object AddExpense : Screen("expenses/add")
    data object Subscriptions : Screen("subscriptions")
    data object Actions : Screen("actions")
    data object Diary : Screen("diary")
    data object Analytics : Screen("analytics")
    data object Conflicts : Screen("conflicts")
    data object Settings : Screen("settings")
}
```

`ui/navigation/AppNavHost.kt` is the single `NavHost` composable. Every `composable(route)` entry lives here — navigation calls are never scattered across screens.

---

## Dependency Injection

All wiring lives in `di/AppModule.kt` using Hilt. The chain is:

```
ViewModel  ←  UseCase  ←  Repository  ←  FirebaseManager
```

- `Application` class: `@HiltAndroidApp`
- `MainActivity`: `@AndroidEntryPoint`
- ViewModels: `@HiltViewModel`
- Repositories and services: `@Inject constructor` or provided via `@Module @InstallIn(SingletonComponent::class)`

---

## Quick Reference

| Question | Answer |
|---------|--------|
| Where does Firestore query code go? | `data/firebase/<Entity>Repository.kt` |
| Where does FX conversion logic go? | `domain/usecase/` — calls `FxRateService` from data |
| Where does virtual entry computation go? | `domain/usecase/ComputeVirtualEntriesUseCase.kt` |
| Where do WorkManager workers go? | `workers/` |
| Where do feature-specific composables go? | `ui/<feature>/components/` |
| Where do shared composables go? | `ui/components/` (only create when 2+ features need it) |
| Where does Firebase init happen? | `data/firebase/FirebaseManager.kt`, called from `Application` |
| Where do encrypted credentials live? | `data/preferences/EncryptedConfigStore.kt` |
| Where does the nav graph live? | `ui/navigation/AppNavHost.kt` |
| Where are route constants defined? | `ui/navigation/Screen.kt` |
