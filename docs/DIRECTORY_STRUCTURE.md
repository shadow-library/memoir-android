# Shadow Memoir Android Directory Structure

**Status:** Mandatory
**Applies to:** Android app only
**Base package:** `com.shadow.apps.memoir`

This document defines the required directory structure and file ownership rules for the Android app. It must be followed strictly for all new code and for any refactor that moves existing code.

The goal is simple: after reading this document, it should be obvious where a screen, ViewModel, UI state, repository, model, use case, worker, mapper, API client, or reusable component belongs.

---

## 1. Core Rules

1. The app follows Clean Architecture.
2. The main layers are `ui`, `domain`, `data`, `di`, and `workers`.
3. Dependencies flow inward:

```text
ui -> domain <- data
```

4. `ui` may depend on `domain`.
5. `data` may depend on `domain`.
6. `domain` must not depend on `ui`, `data`, Android framework classes, Firebase, Compose, Hilt, Retrofit, OkHttp, Health Connect, or file system APIs.
7. Every user-facing screen must have its own folder.
8. A screen folder owns that screen's `Screen`, `ViewModel`, and `UiState` files.
9. Shared UI components must live in `components` folders.
10. Firestore implementation code must live under `data/firebase`.
11. External service implementation code must live under the matching `data` integration folder.
12. Business rules must live in `domain/usecase`, not in composables or repositories.
13. Repository interfaces must live in `domain/repository`.
14. Repository implementations must live in `data`.
15. New code must follow the naming and placement rules in this document even if older code has not been migrated yet.

---

## 2. Target Package Tree

```text
app/src/main/java/com/shadow/apps/memoir/
├── MemoirApplication.kt
├── MainActivity.kt
├── data/
│   ├── firebase/
│   │   ├── FirebaseManager.kt
│   │   ├── auth/
│   │   │   └── FirebaseAuthRepository.kt
│   │   ├── profile/
│   │   │   ├── FirebaseProfileRepository.kt
│   │   │   └── ProfileFirestoreMapper.kt
│   │   ├── expenses/
│   │   │   ├── FirebaseExpenseRepository.kt
│   │   │   └── ExpenseFirestoreMapper.kt
│   │   ├── subscriptions/
│   │   │   ├── FirebaseSubscriptionRepository.kt
│   │   │   └── SubscriptionFirestoreMapper.kt
│   │   ├── actions/
│   │   │   ├── FirebaseActionRepository.kt
│   │   │   └── ActionFirestoreMapper.kt
│   │   ├── diary/
│   │   │   ├── FirebaseDiaryRepository.kt
│   │   │   └── DiaryFirestoreMapper.kt
│   │   ├── health/
│   │   │   ├── FirebaseHealthRepository.kt
│   │   │   └── HealthFirestoreMapper.kt
│   │   ├── categories/
│   │   │   ├── FirebaseCategoryRepository.kt
│   │   │   └── CategoryFirestoreMapper.kt
│   │   ├── analytics/
│   │   │   ├── FirebaseAnalyticsRepository.kt
│   │   │   └── MonthlyAnalyticsFirestoreMapper.kt
│   │   └── conflicts/
│   │       ├── FirebaseConflictRepository.kt
│   │       └── ConflictFirestoreMapper.kt
│   ├── preferences/
│   │   ├── EncryptedConfigStore.kt
│   │   └── DeviceConfigStore.kt
│   ├── fx/
│   │   ├── FrankfurterFxRepository.kt
│   │   ├── FrankfurterApi.kt
│   │   ├── FxDto.kt
│   │   └── FxMapper.kt
│   ├── ai/
│   │   ├── GeminiReceiptParserRepository.kt
│   │   ├── GeminiApi.kt
│   │   ├── GeminiReceiptParser.kt
│   │   ├── ReceiptParsingDto.kt
│   │   └── ReceiptParsingMapper.kt
│   ├── healthconnect/
│   │   ├── HealthConnectManager.kt
│   │   └── HealthConnectMapper.kt
│   └── export/
│       ├── FileExportRepository.kt
│       ├── ExportManager.kt
│       ├── CsvExporter.kt
│       └── JsonExporter.kt
├── domain/
│   ├── model/
│   │   ├── Profile.kt
│   │   ├── Device.kt
│   │   ├── Expense.kt
│   │   ├── Subscription.kt
│   │   ├── ActionDefinition.kt
│   │   ├── ActionLog.kt
│   │   ├── DiaryEntry.kt
│   │   ├── HealthSnapshot.kt
│   │   ├── Category.kt
│   │   ├── Conflict.kt
│   │   ├── MonthlyAnalytics.kt
│   │   └── Currency.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ProfileRepository.kt
│   │   ├── ExpenseRepository.kt
│   │   ├── SubscriptionRepository.kt
│   │   ├── ActionRepository.kt
│   │   ├── DiaryRepository.kt
│   │   ├── HealthRepository.kt
│   │   ├── CategoryRepository.kt
│   │   ├── AnalyticsRepository.kt
│   │   ├── ConflictRepository.kt
│   │   ├── FxRepository.kt
│   │   ├── ReceiptParserRepository.kt
│   │   └── ExportRepository.kt
│   └── usecase/
│       ├── onboarding/
│       ├── expenses/
│       ├── subscriptions/
│       ├── actions/
│       ├── diary/
│       ├── health/
│       ├── analytics/
│       ├── conflicts/
│       ├── settings/
│       └── export/
├── ui/
│   ├── navigation/
│   │   ├── AppNavHost.kt
│   │   └── Routes.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── components/
│   │   └── shared app-wide composables
│   ├── splash/
│   │   └── SplashScreen.kt
│   ├── home/
│   │   └── HomeScreen.kt
│   ├── onboarding/
│   │   ├── gettingstarted/
│   │   ├── firebasesetup/
│   │   ├── scanqr/
│   │   ├── devicetype/
│   │   ├── devicesetup/
│   │   ├── signin/
│   │   ├── setupcomplete/
│   │   └── components/
│   ├── expenses/
│   │   ├── list/
│   │   ├── editor/
│   │   └── components/
│   ├── subscriptions/
│   │   ├── list/
│   │   ├── editor/
│   │   └── components/
│   ├── actions/
│   │   ├── list/
│   │   ├── detail/
│   │   ├── editor/
│   │   └── components/
│   ├── diary/
│   │   ├── list/
│   │   ├── editor/
│   │   └── components/
│   ├── analytics/
│   │   ├── dashboard/
│   │   └── components/
│   ├── conflicts/
│   │   ├── list/
│   │   ├── detail/
│   │   └── components/
│   └── settings/
│       ├── main/
│       ├── currency/
│       ├── firebase/
│       ├── devices/
│       └── export/
├── di/
│   ├── FirebaseModule.kt
│   ├── RepositoryModule.kt
│   ├── NetworkModule.kt
│   ├── PreferencesModule.kt
│   └── WorkerModule.kt
└── workers/
    ├── HealthSyncWorker.kt
    ├── NudgeWorker.kt
    ├── ActionReminderWorker.kt
    └── SubscriptionRenewalWorker.kt
```

---

## 3. UI Layer Rules

The `ui` layer contains only Compose UI, ViewModels, UI state, navigation, theme, and UI-only helpers.

### 3.1 Screen Folder Rule

Every screen or route must get its own folder.

Correct:

```text
ui/onboarding/signin/
├── SignInScreen.kt
├── SignInViewModel.kt
└── SignInUiState.kt
```

Incorrect:

```text
ui/onboarding/
├── SignInScreen.kt
├── SignInViewModel.kt
└── SignInUiState.kt
```

The old flat style is not allowed for new screens.

### 3.2 Feature Folder vs Screen Folder

A feature folder groups related screens.

Examples:

```text
ui/expenses/list/
ui/expenses/editor/
ui/expenses/components/
```

Use a feature folder when screens belong to the same product area. Use a screen folder for each route inside the feature.

### 3.3 Components Placement

Use the closest valid `components` folder.

```text
ui/expenses/components/ExpenseCard.kt
```

Use this when the component is shared by multiple expense screens only.

```text
ui/components/PrimaryActionButton.kt
```

Use this only when the component is shared by multiple features.

Private composables used only by one screen may stay inside that screen's `Screen.kt`.

### 3.4 Screen File Content

A `Screen.kt` file must contain:

1. The public route composable.
2. The private stateless content composable.
3. Small private screen-only composables.
4. Previews for that screen.

It must not contain:

1. Repository calls.
2. Firestore calls.
3. HTTP calls.
4. Long business logic.
5. Data mapping logic.
6. Mutable app state outside Compose state helpers.

Template:

```kotlin
package com.shadow.apps.memoir.ui.expenses.list

/*
 * Route
 *
 * Entry point used by navigation. This composable connects the ViewModel
 * to the stateless screen content.
 */
@Composable
fun ExpensesScreen(
    onAddExpense: () -> Unit,
    viewModel: ExpensesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExpensesContent(
        uiState = uiState,
        onAddExpense = onAddExpense,
        onRefresh = viewModel::refresh,
    )
}

/*
 * Content
 *
 * Stateless UI. This composable receives all state and events as parameters.
 */
@Composable
private fun ExpensesContent(
    uiState: ExpensesUiState,
    onAddExpense: () -> Unit,
    onRefresh: () -> Unit,
) {
    // UI only.
}

/*
 * Preview
 *
 * Preview data must be fake and local to the UI layer.
 */
@Preview
@Composable
private fun ExpensesContentPreview() {
    ExpensesContent(
        uiState = ExpensesUiState(),
        onAddExpense = {},
        onRefresh = {},
    )
}
```

### 3.5 ViewModel File Content

A `ViewModel.kt` file must contain:

1. One `@HiltViewModel` class.
2. A private mutable state flow.
3. A public immutable state flow.
4. Public event handlers called by the screen.
5. Calls to domain use cases.

It must not contain:

1. Compose UI.
2. Firestore SDK calls.
3. HTTP calls.
4. DTO mapping.
5. Direct Android storage logic.
6. Large business calculations that belong in use cases.

Template:

```kotlin
package com.shadow.apps.memoir.ui.expenses.list

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val observeExpenses: ObserveExpensesUseCase,
    private val refreshExpenses: RefreshExpensesUseCase,
) : ViewModel() {

    /*
     * State
     *
     * The screen observes only this immutable StateFlow.
     */
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    /*
     * Initialization
     *
     * Start long-lived observations here.
     */
    init {
        observeExpenseChanges()
    }

    /*
     * Events
     *
     * Public functions called by the screen.
     */
    fun refresh() {
        viewModelScope.launch {
            refreshExpenses()
        }
    }

    /*
     * Private helpers
     *
     * Keep helpers small. Move complex business rules to use cases.
     */
    private fun observeExpenseChanges() {
        viewModelScope.launch {
            observeExpenses().collect { expenses ->
                _uiState.update { it.copy(expenses = expenses) }
            }
        }
    }
}
```

### 3.6 UI State File Content

A `UiState.kt` file must contain:

1. One main immutable UI state data class.
2. UI-only enums used only by that screen.
3. UI-only value objects used only by that screen.

It must not contain:

1. Repository interfaces.
2. Repository implementations.
3. Use case classes.
4. Firestore DTOs.
5. Android framework types unless unavoidable for UI display.

Template:

```kotlin
package com.shadow.apps.memoir.ui.expenses.list

/*
 * UI state
 *
 * Complete render state for ExpensesScreen.
 */
data class ExpensesUiState(
    val expenses: List<ExpenseListItem> = emptyList(),
    val selectedViewMode: ExpenseViewMode = ExpenseViewMode.Budget,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/*
 * UI models
 *
 * Screen-specific display models. Domain models are allowed when they fit
 * the UI directly, but display-specific formatting belongs here.
 */
data class ExpenseListItem(
    val id: String,
    val merchant: String,
    val amountText: String,
    val categoryName: String,
)

enum class ExpenseViewMode {
    Cash,
    Budget,
}
```

---

## 4. Domain Layer Rules

The `domain` layer contains pure Kotlin business code.

### 4.1 Domain Models

Domain models live in:

```text
domain/model/
```

Use domain models for core app entities:

1. `Expense`
2. `Subscription`
3. `ActionDefinition`
4. `ActionLog`
5. `DiaryEntry`
6. `HealthSnapshot`
7. `MonthlyAnalytics`
8. `Category`
9. `Profile`
10. `Device`
11. `Conflict`
12. `Currency`

Domain model files must contain:

1. Plain Kotlin data classes.
2. Enums that represent business concepts.
3. Small value classes when they improve type safety.

Domain model files must not contain:

1. Firebase annotations.
2. Firestore `DocumentSnapshot`.
3. Android `Context`.
4. Compose types.
5. API DTOs.
6. JSON parsing.

Template:

```kotlin
package com.shadow.apps.memoir.domain.model

/*
 * Expense
 *
 * Domain representation of a stored expense. This model is independent of
 * Firestore, Compose, and API response formats.
 */
data class Expense(
    val id: String,
    val amount: Double,
    val currency: String,
    val amountInr: Long,
    val merchant: String,
    val categoryId: String,
    val tags: List<String>,
    val note: String?,
    val createdAtMillis: Long,
    val updatedAtMillis: Long,
    val deviceId: String,
    val version: Int,
    val schemaVersion: Int,
)
```

### 4.2 Repository Interfaces

Repository interfaces live in:

```text
domain/repository/
```

Repository interfaces define what the app needs, not how the data is fetched.

They may return:

1. Domain models.
2. Kotlin `Flow`.
3. Kotlin `Result` only when useful.
4. Plain Kotlin values.

They must not expose:

1. Firestore types.
2. HTTP response types.
3. Android framework types.
4. DTOs.

Template:

```kotlin
package com.shadow.apps.memoir.domain.repository

interface ExpenseRepository {

    /*
     * Reads
     */
    fun observeExpenses(): Flow<List<Expense>>

    suspend fun getExpense(id: String): Expense?

    /*
     * Writes
     */
    suspend fun saveExpense(expense: Expense)

    suspend fun deleteExpense(id: String)
}
```

### 4.3 Use Cases

Use cases live in feature folders:

```text
domain/usecase/expenses/SaveExpenseUseCase.kt
domain/usecase/expenses/GetBudgetViewUseCase.kt
domain/usecase/health/SyncHealthDataUseCase.kt
```

A use case file must contain:

1. One public use case class.
2. One public `operator fun invoke(...)` or a clearly named public function.
3. Business rules for one user action or one business calculation.

A use case must not contain:

1. Compose code.
2. Firestore code.
3. HTTP code.
4. Android UI code.

Template:

```kotlin
package com.shadow.apps.memoir.domain.usecase.expenses

class SaveExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val fxRepository: FxRepository,
) {

    /*
     * Execution
     *
     * Converts currency when required and saves the final domain model.
     */
    suspend operator fun invoke(input: SaveExpenseInput) {
        // Business rules only.
    }
}
```

---

## 5. Data Layer Rules

The `data` layer contains implementation details for external systems.

Use this rule to place files:

1. Firestore collection or Firebase Auth code -> `data/firebase`.
2. Android Keystore-backed secure preferences or local config -> `data/preferences`.
3. Frankfurter currency and FX API -> `data/fx`.
4. Gemini receipt parsing -> `data/ai`.
5. Health Connect integration -> `data/healthconnect`.
6. CSV or JSON export implementation -> `data/export`.

### 5.1 Firebase

`data/firebase` owns Firebase app setup, Firebase Auth, and Firestore-backed repositories.

```text
data/firebase/
├── FirebaseManager.kt
├── auth/
├── profile/
├── expenses/
├── subscriptions/
├── actions/
├── diary/
├── health/
├── categories/
├── analytics/
└── conflicts/
```

Each Firestore feature folder should contain:

1. A source-specific repository class, such as `FirebaseExpenseRepository.kt`
2. `XFirestoreMapper.kt`

Repository implementation files contain:

1. Firestore collection paths.
2. Firestore queries.
3. Transactions.
4. Snapshot listeners.
5. Calls to mapper functions.
6. Implementation of a `domain/repository` interface.

Mapper files contain:

1. Firestore document to domain mapping.
2. Domain to Firestore map conversion.
3. Timestamp conversion.
4. Schema version defaulting.

Repository implementation files must not contain:

1. Compose code.
2. ViewModel state.
3. UI formatting.
4. Business calculations that belong in use cases.

Template:

```kotlin
package com.shadow.apps.memoir.data.firebase.expenses

@Singleton
class FirebaseExpenseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
) : ExpenseRepository {

    /*
     * References
     *
     * Firestore paths for user-owned expense documents.
     */
    private fun expensesRef(uid: String): CollectionReference =
        firebaseManager.firestore()
            .collection("users")
            .document(uid)
            .collection("expenses")

    /*
     * Reads
     */
    override fun observeExpenses(): Flow<List<Expense>> {
        // Firestore listener implementation.
    }

    /*
     * Writes
     */
    override suspend fun saveExpense(expense: Expense) {
        // Firestore write implementation.
    }
}
```

Mapper template:

```kotlin
package com.shadow.apps.memoir.data.firebase.expenses

/*
 * Firestore mapping
 *
 * Converts between Firestore document data and pure domain models.
 */
internal object ExpenseFirestoreMapper {

    fun fromDocument(document: DocumentSnapshot): Expense {
        // Firestore -> domain.
    }

    fun toDocument(expense: Expense): Map<String, Any?> {
        // Domain -> Firestore.
    }
}
```

### 5.2 Preferences

`data/preferences` owns local encrypted config and device setup values.

Examples:

```text
data/preferences/EncryptedConfigStore.kt
data/preferences/DeviceConfigStore.kt
```

Use this folder for:

1. Firebase credentials saved locally.
2. Device ID cache.
3. Local setup flags.
4. Secure user configuration.

Do not use this folder for Firestore data, domain models, or UI state.

### 5.3 FX

`data/fx` owns Frankfurter API integration.

```text
data/fx/
├── FrankfurterFxRepository.kt
├── FrankfurterApi.kt
├── FxDto.kt
└── FxMapper.kt
```

Responsibilities:

1. Fetch supported currencies.
2. Fetch latest exchange rates.
3. Fetch historical exchange rates.
4. Maintain in-memory per-day rate cache.
5. Convert API DTOs to domain models.

Do not place expense saving logic here. Expense saving belongs in `domain/usecase/expenses` and `data/firebase/expenses`.

### 5.4 AI

`data/ai` owns Gemini receipt parsing.

```text
data/ai/
├── GeminiReceiptParserRepository.kt
├── GeminiApi.kt
├── GeminiReceiptParser.kt
├── ReceiptParsingDto.kt
└── ReceiptParsingMapper.kt
```

Responsibilities:

1. Build Gemini requests.
2. Send receipt images to Gemini.
3. Parse Gemini responses.
4. Convert parsed output to domain-friendly receipt data.
5. Ensure receipt images are not persisted after parsing.

Do not place CameraX code here. Camera UI belongs in `ui`, and camera capture infrastructure may get its own integration folder if it grows.

### 5.5 Health Connect

`data/healthconnect` owns Android Health Connect integration.

```text
data/healthconnect/
├── HealthConnectManager.kt
└── HealthConnectMapper.kt
```

Responsibilities:

1. Request or check Health Connect permissions.
2. Read sleep, steps, heart rate, stress, active minutes, and workouts.
3. Convert Health Connect records into domain `HealthSnapshot` data.

Firestore writes for health snapshots still belong in:

```text
data/firebase/health/
```

### 5.6 Export

`data/export` owns CSV and JSON export implementation.

```text
data/export/
├── FileExportRepository.kt
├── ExportManager.kt
├── CsvExporter.kt
└── JsonExporter.kt
```

Responsibilities:

1. Read domain data through repository interfaces or use cases.
2. Convert export data into CSV.
3. Convert export data into JSON.
4. Provide files or shareable content to the Android share flow.

UI for export belongs in:

```text
ui/settings/export/
```

---

## 6. Dependency Injection

Hilt modules live in:

```text
di/
```

Recommended files:

```text
di/FirebaseModule.kt
di/RepositoryModule.kt
di/NetworkModule.kt
di/PreferencesModule.kt
di/WorkerModule.kt
```

### 6.1 RepositoryModule

`RepositoryModule.kt` binds domain repository interfaces to data implementations.

Template:

```kotlin
package com.shadow.apps.memoir.di

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /*
     * Firebase repositories
     */
    @Binds
    abstract fun bindExpenseRepository(
        implementation: FirebaseExpenseRepository,
    ): ExpenseRepository

    /*
     * External service repositories
     */
    @Binds
    abstract fun bindFxRepository(
        implementation: FrankfurterFxRepository,
    ): FxRepository
}
```

### 6.2 Module Responsibilities

Use this placement:

1. `FirebaseModule.kt` -> Firebase-related singleton providers if needed.
2. `RepositoryModule.kt` -> `@Binds` for repository interfaces.
3. `NetworkModule.kt` -> HTTP clients, JSON serializers, API classes.
4. `PreferencesModule.kt` -> preference stores and encrypted storage helpers.
5. `WorkerModule.kt` -> worker-related bindings or factories when needed.

---

## 7. Workers

Workers live in:

```text
workers/
```

Workers are background execution entry points. They should be thin.

Worker files may contain:

1. WorkManager worker classes.
2. Worker input key constants.
3. Calls to domain use cases.

Worker files must not contain:

1. Firestore mapping logic.
2. Health Connect parsing logic.
3. Business rules.
4. UI state.

Template:

```kotlin
package com.shadow.apps.memoir.workers

@HiltWorker
class HealthSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncHealthData: SyncHealthDataUseCase,
) : CoroutineWorker(context, params) {

    /*
     * Work execution
     *
     * Delegates health sync rules to the domain use case.
     */
    override suspend fun doWork(): Result {
        return runCatching {
            syncHealthData()
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }
}
```

---

## 8. Navigation

Navigation lives in:

```text
ui/navigation/
```

`Routes.kt` contains route constants or sealed route definitions.

`AppNavHost.kt` contains the app navigation graph.

Navigation files may import screen composables. Screen files must not define global app navigation routes.

Template:

```kotlin
package com.shadow.apps.memoir.ui.navigation

/*
 * Routes
 *
 * Central list of app navigation destinations.
 */
object Routes {
    const val Splash = "splash"
    const val SignIn = "onboarding/signin"
    const val Expenses = "expenses/list"
}
```

---

## 9. Naming Rules

### 9.1 UI

Use these names:

```text
<ScreenName>Screen.kt
<ScreenName>ViewModel.kt
<ScreenName>UiState.kt
```

Examples:

```text
SignInScreen.kt
SignInViewModel.kt
SignInUiState.kt
ExpenseEditorScreen.kt
ExpenseEditorViewModel.kt
ExpenseEditorUiState.kt
```

### 9.2 Domain

Use these names:

```text
<Entity>.kt
<Entity>Repository.kt
<Action><Entity>UseCase.kt
```

Examples:

```text
Expense.kt
ExpenseRepository.kt
SaveExpenseUseCase.kt
GetBudgetViewUseCase.kt
```

### 9.3 Data

Use these names:

```text
<Source><Entity>Repository.kt
<Entity>FirestoreMapper.kt
<Service>Api.kt
<Service>Dto.kt
<Service>Mapper.kt
```

Examples:

```text
FirebaseExpenseRepository.kt
ExpenseFirestoreMapper.kt
FrankfurterApi.kt
FxDto.kt
FxMapper.kt
GeminiApi.kt
ReceiptParsingDto.kt
```

---

## 10. Block Comment Rules

Block comments must be used to segregate logical groups inside files and to explain what each group is used for.

Use Kotlin block comments:

```kotlin
/*
 * State
 *
 * Holds the mutable and immutable UI state streams for this ViewModel.
 */
```

Do not use noisy comments for obvious single lines. Use block comments for meaningful file sections.

### 10.1 Required Block Comment Sections

Use these section names where applicable.

For `Screen.kt`:

```text
Route
Content
State helpers
Screen-only components
Preview
```

For `ViewModel.kt`:

```text
State
Initialization
Events
Private helpers
```

For `UiState.kt`:

```text
UI state
UI models
UI enums
```

For repository implementations:

```text
References
Reads
Writes
Transactions
Listeners
Private helpers
```

For mapper files:

```text
Firestore mapping
API mapping
Domain mapping
Defaults
```

For use cases:

```text
Execution
Validation
Business rules
Private helpers
```

For DI modules:

```text
Firebase repositories
External service repositories
Stores
Network clients
Workers
```

### 10.2 Comment Quality

Good:

```kotlin
/*
 * Transactions
 *
 * Expense writes use version checks so conflicting offline edits can create
 * conflict documents instead of silently overwriting server data.
 */
```

Bad:

```kotlin
/*
 * Function
 */
```

Bad:

```kotlin
// Sets amount.
```

Block comments must clarify intent, ownership, or grouping.

---

## 11. Placement Guide

Use this section when deciding where new code belongs.

### 11.1 Screens

Question: "Is this a user-facing route?"

Place it in:

```text
ui/<feature>/<screen>/
```

Example:

```text
ui/settings/currency/CurrencySettingsScreen.kt
ui/settings/currency/CurrencySettingsViewModel.kt
ui/settings/currency/CurrencySettingsUiState.kt
```

### 11.2 Feature-only UI Component

Question: "Is this composable reused by more than one screen in the same feature?"

Place it in:

```text
ui/<feature>/components/
```

Example:

```text
ui/expenses/components/ExpenseCard.kt
```

### 11.3 App-wide UI Component

Question: "Is this composable reused by multiple features?"

Place it in:

```text
ui/components/
```

Example:

```text
ui/components/LoadingState.kt
```

### 11.4 Business Entity

Question: "Is this a core app concept independent of storage and UI?"

Place it in:

```text
domain/model/
```

Example:

```text
domain/model/Expense.kt
```

### 11.5 Business Operation

Question: "Is this a user action or business calculation?"

Place it in:

```text
domain/usecase/<feature>/
```

Example:

```text
domain/usecase/expenses/ComputeVirtualEntriesUseCase.kt
```

### 11.6 Data Contract

Question: "Does UI or domain need data without knowing where it comes from?"

Place the interface in:

```text
domain/repository/
```

Example:

```text
domain/repository/ExpenseRepository.kt
```

### 11.7 Firestore Implementation

Question: "Does this read or write Firestore?"

Place it in:

```text
data/firebase/<collection-or-feature>/
```

Example:

```text
data/firebase/expenses/FirebaseExpenseRepository.kt
data/firebase/expenses/ExpenseFirestoreMapper.kt
```

### 11.8 Firebase App Setup

Question: "Does this initialize Firebase or provide Firebase SDK instances?"

Place it in:

```text
data/firebase/FirebaseManager.kt
```

### 11.9 Encrypted Local Config

Question: "Does this store Firebase credentials, setup state, or device config locally?"

Place it in:

```text
data/preferences/
```

### 11.10 Frankfurter FX API

Question: "Does this call Frankfurter or map FX responses?"

Place it in:

```text
data/fx/
```

### 11.11 Gemini Receipt Parsing

Question: "Does this call Gemini or parse receipt extraction output?"

Place it in:

```text
data/ai/
```

### 11.12 Health Connect

Question: "Does this read Android Health Connect records?"

Place it in:

```text
data/healthconnect/
```

### 11.13 Health Firestore Sync

Question: "Does this write health snapshots to Firestore?"

Place it in:

```text
data/firebase/health/
```

### 11.14 Export

Question: "Does this generate CSV or JSON export files?"

Place it in:

```text
data/export/
```

### 11.15 Background Work

Question: "Does this run in WorkManager?"

Place it in:

```text
workers/
```

---

## 12. Feature Examples

### 12.1 Add Expense Flow

```text
ui/expenses/editor/ExpenseEditorScreen.kt
ui/expenses/editor/ExpenseEditorViewModel.kt
ui/expenses/editor/ExpenseEditorUiState.kt
ui/expenses/components/CategoryPicker.kt

domain/model/Expense.kt
domain/repository/ExpenseRepository.kt
domain/repository/FxRepository.kt
domain/usecase/expenses/SaveExpenseUseCase.kt
domain/usecase/expenses/ValidateExpenseUseCase.kt

data/firebase/expenses/FirebaseExpenseRepository.kt
data/firebase/expenses/ExpenseFirestoreMapper.kt
data/fx/FrankfurterFxRepository.kt
data/fx/FrankfurterApi.kt
data/fx/FxMapper.kt
```

### 12.2 Receipt Scanner Flow

```text
ui/expenses/scanner/ReceiptScannerScreen.kt
ui/expenses/scanner/ReceiptScannerViewModel.kt
ui/expenses/scanner/ReceiptScannerUiState.kt

domain/repository/ReceiptParserRepository.kt
domain/usecase/expenses/ParseReceiptUseCase.kt
domain/usecase/expenses/SaveExpenseUseCase.kt

data/ai/GeminiReceiptParserRepository.kt
data/ai/GeminiApi.kt
data/ai/ReceiptParsingMapper.kt
```

### 12.3 Health Sync Flow

```text
workers/HealthSyncWorker.kt

domain/model/HealthSnapshot.kt
domain/repository/HealthRepository.kt
domain/usecase/health/SyncHealthDataUseCase.kt

data/healthconnect/HealthConnectManager.kt
data/healthconnect/HealthConnectMapper.kt
data/firebase/health/FirebaseHealthRepository.kt
data/firebase/health/HealthFirestoreMapper.kt
```

### 12.4 Diary Entry Flow

```text
ui/diary/editor/DiaryEntryScreen.kt
ui/diary/editor/DiaryEntryViewModel.kt
ui/diary/editor/DiaryEntryUiState.kt
ui/diary/components/MoodSelector.kt

domain/model/DiaryEntry.kt
domain/repository/DiaryRepository.kt
domain/usecase/diary/SaveDiaryEntryUseCase.kt

data/firebase/diary/FirebaseDiaryRepository.kt
data/firebase/diary/DiaryFirestoreMapper.kt
```

---

## 13. Strict Review Checklist

Use this checklist before adding or moving code.

1. Does every screen route have its own folder?
2. Are `Screen`, `ViewModel`, and `UiState` together in that screen folder?
3. Are shared feature composables in `ui/<feature>/components`?
4. Are app-wide composables in `ui/components`?
5. Are business models in `domain/model`?
6. Are repository interfaces in `domain/repository`?
7. Are business rules in `domain/usecase`?
8. Are Firestore repositories in `data/firebase/<feature>`?
9. Are Firestore mappers separate from repository implementations?
10. Are Frankfurter API classes in `data/fx`?
11. Are Gemini classes in `data/ai`?
12. Are Health Connect classes in `data/healthconnect`?
13. Are encrypted local config classes in `data/preferences`?
14. Are export classes in `data/export`?
15. Are WorkManager classes in `workers`?
16. Does `domain` avoid Android, Firebase, Compose, and API DTO imports?
17. Does `ui` avoid Firestore and HTTP imports?
18. Does each non-trivial file use block comments to separate logical groups?
19. Is naming consistent with this document?
20. Would a new developer know where this code belongs by reading this document?

If the answer to any item is "no", the code should be moved or renamed before merging.
