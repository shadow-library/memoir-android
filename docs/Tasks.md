# Shadow Memoir — Android Screen Task Plan

**Version:** 2.0
**Platform:** Android
**Architecture:** Clean Architecture with screen-owned UI folders

All implementation must follow [DIRECTORY_STRUCTURE.md](./DIRECTORY_STRUCTURE.md). Each screen is tracked in the order it should be built: first UI and state, then Firebase/data integration.

---

## Rules

- Every user-facing route must have its own folder.
- Each screen folder must own its `Screen`, `ViewModel`, and `UiState`.
- Each screen section must use only these task groups:
  - UI + State
  - Firebase / Data Integration
- Checked tasks represent completed work in the current codebase.
- Unchecked tasks represent remaining implementation work.
- Architecture rules are referenced from [DIRECTORY_STRUCTURE.md](./DIRECTORY_STRUCTURE.md), not repeated here.

---

## 1. Splash

Target paths:

```text
ui/splash/SplashScreen.kt
ui/splash/SplashViewModel.kt
ui/splash/SplashUiState.kt
domain/usecase/appstartup/GetStartupDestinationUseCase.kt
domain/usecase/appstartup/InitialiseFirebaseFromStoredConfigUseCase.kt
```

### UI + State

- [x] Build splash screen UI
- [x] Add splash UI state
- [x] Add splash ViewModel
- [x] Route to the next destination after startup check

### Firebase / Data Integration

- [x] Load stored Firebase config
- [x] Dynamically initialize Firebase
- [x] Decide startup destination from config, auth, and setup state
- [x] Move startup logic out of `MainActivity`

---

## 2. Onboarding

### 2.1 Getting Started

Target path:

```text
ui/onboarding/gettingstarted/GettingStartedScreen.kt
```

#### UI + State

- [x] Build getting started UI
- [x] Add continue action to device type selection

#### Firebase / Data Integration

- [x] No Firebase integration needed

### 2.2 Device Type

Target path:

```text
ui/onboarding/devicetype/DeviceTypeScreen.kt
```

#### UI + State

- [x] Build new vault vs existing vault selection UI
- [x] Add navigation actions for setup or QR scan

#### Firebase / Data Integration

- [x] No Firebase integration needed

### 2.3 Firebase Setup

Target paths:

```text
ui/onboarding/firebasesetup/FirebaseSetupScreen.kt
ui/onboarding/firebasesetup/FirebaseSetupViewModel.kt
ui/onboarding/firebasesetup/FirebaseSetupUiState.kt
domain/usecase/onboarding/ParseFirebaseConfigUseCase.kt
domain/usecase/onboarding/SaveFirebaseCredentialsUseCase.kt
data/preferences/EncryptedConfigStore.kt
data/preferences/EncryptedConfigRepository.kt
```

#### UI + State

- [x] Build manual Firebase config form
- [x] Support uploading `google-services.json`
- [x] Add form state and validation
- [x] Add upload error state
- [x] Add save/loading state

#### Firebase / Data Integration

- [x] Parse `google-services.json`
- [x] Save Firebase credentials
- [x] Store credentials encrypted with Android Keystore AES-GCM
- [x] Use domain use cases instead of direct data access

### 2.4 Scan QR

Target paths:

```text
ui/onboarding/scanqr/ScanQrScreen.kt
ui/onboarding/scanqr/ScanQrViewModel.kt
ui/onboarding/scanqr/ScanQrUiState.kt
domain/usecase/onboarding/ParseFirebaseConfigUseCase.kt
domain/usecase/onboarding/SaveFirebaseCredentialsUseCase.kt
```

#### UI + State

- [x] Add camera permission state
- [x] Build QR scanning UI
- [x] Add QR success and error states
- [x] Add saving state
- [x] Add manual entry fallback navigation

#### Firebase / Data Integration

- [x] Parse QR config payload
- [x] Save Firebase credentials securely
- [x] Use domain use cases instead of direct data access

### 2.5 Sign In

Target paths:

```text
ui/onboarding/signin/SignInScreen.kt
ui/onboarding/signin/SignInViewModel.kt
ui/onboarding/signin/SignInUiState.kt
domain/usecase/onboarding/LoadFirebaseCredentialsUseCase.kt
domain/usecase/onboarding/SignInWithGoogleUseCase.kt
domain/usecase/onboarding/GetPostSignInDestinationUseCase.kt
data/firebase/auth/FirebaseAuthRepository.kt
```

#### UI + State

- [x] Build sign-in screen UI
- [x] Add project display state
- [x] Add loading and error states
- [x] Add post-sign-in navigation decision

#### Firebase / Data Integration

- [x] Load stored Firebase credentials
- [x] Initialize Firebase if needed
- [x] Add Google ID token sign-in
- [x] Add Firebase Auth integration
- [x] Use domain use cases instead of direct data access

### 2.6 Device Setup

Target paths:

```text
ui/onboarding/devicesetup/DeviceSetupScreen.kt
ui/onboarding/devicesetup/DeviceSetupViewModel.kt
ui/onboarding/devicesetup/DeviceSetupUiState.kt
domain/usecase/onboarding/DetectDeviceSetupPathUseCase.kt
domain/usecase/onboarding/LoadCurrenciesUseCase.kt
domain/usecase/onboarding/CompleteDeviceSetupUseCase.kt
data/firebase/profile/FirebaseProfileRepository.kt
data/fx/FrankfurterFxRepository.kt
```

#### UI + State

- [x] Build device name UI
- [x] Add primary device toggle
- [x] Add default currency picker
- [x] Add loading, error, and saving states
- [x] Add retry save behavior

#### Firebase / Data Integration

- [x] Detect whether user profile exists
- [x] Save profile document
- [x] Register device on profile
- [x] Save local device identity securely
- [x] Load currencies through Retrofit and OkHttp Frankfurter integration
- [ ] Confirm Firestore offline persistence setup explicitly

### 2.7 Setup Complete

Target path:

```text
ui/onboarding/setupcomplete/SetupCompleteScreen.kt
```

#### UI + State

- [x] Build setup complete UI
- [x] Add continue to home navigation

#### Firebase / Data Integration

- [x] No Firebase integration needed

---

## 3. Home

Target path:

```text
ui/home/HomeScreen.kt
```

### UI + State

- [x] Add placeholder home screen
- [ ] Build dashboard shell for core modules
- [ ] Add entry points to expenses, subscriptions, actions, diary, analytics, and settings

### Firebase / Data Integration

- [ ] Load lightweight profile/default currency summary if needed
- [ ] Load global sync status indicator if used

---

## 4. Expenses

### 4.1 Expense List

Target paths:

```text
ui/expenses/list/ExpensesScreen.kt
ui/expenses/list/ExpensesViewModel.kt
ui/expenses/list/ExpensesUiState.kt
domain/model/Expense.kt
domain/repository/ExpenseRepository.kt
domain/usecase/expenses/GetCashViewUseCase.kt
domain/usecase/expenses/GetBudgetViewUseCase.kt
data/firebase/expenses/FirebaseExpenseRepository.kt
data/firebase/expenses/ExpenseFirestoreMapper.kt
```

#### UI + State

- [ ] Build expense list UI grouped by date
- [ ] Add cash/budget segmented toggle
- [ ] Add empty, loading, and error states
- [ ] Add add/edit/delete actions
- [ ] Add expense card component

#### Firebase / Data Integration

- [ ] Create `Expense` domain model
- [ ] Create `ExpenseRepository` contract
- [ ] Implement Firestore expense repository
- [ ] Implement Firestore mapper
- [ ] Observe expenses from Firestore cache
- [ ] Implement cash view use case
- [ ] Implement budget view use case
- [ ] Handle pending writes/sync state

### 4.2 Expense Editor

Target paths:

```text
ui/expenses/editor/ExpenseEditorScreen.kt
ui/expenses/editor/ExpenseEditorViewModel.kt
ui/expenses/editor/ExpenseEditorUiState.kt
domain/usecase/expenses/SaveExpenseUseCase.kt
domain/usecase/expenses/DeleteExpenseUseCase.kt
domain/usecase/expenses/ValidateExpenseUseCase.kt
```

#### UI + State

- [ ] Build add expense UI
- [ ] Build edit expense UI
- [ ] Add amount, currency, merchant, category, tag, and note state
- [ ] Add validation, error, and loading states
- [ ] Add category picker UI

#### Firebase / Data Integration

- [ ] Save expense to Firestore
- [ ] Edit expense with version handling
- [ ] Delete expense
- [ ] Convert foreign currency to INR
- [ ] Store `amountINR`, `fxRateUsed`, and `fxDate`

### 4.3 Receipt Scanner

Target paths:

```text
ui/expenses/scanner/ReceiptScannerScreen.kt
ui/expenses/scanner/ReceiptScannerViewModel.kt
ui/expenses/scanner/ReceiptScannerUiState.kt
domain/repository/ReceiptParserRepository.kt
domain/usecase/expenses/ParseReceiptUseCase.kt
data/ai/GeminiReceiptParserRepository.kt
data/ai/GeminiApi.kt
data/ai/ReceiptParsingDto.kt
data/ai/ReceiptParsingMapper.kt
```

#### UI + State

- [ ] Build camera capture UI
- [ ] Add gallery picker
- [ ] Add processing, error, and manual correction states
- [ ] Add parsed receipt preview before save

#### Firebase / Data Integration

- [ ] Add Gemini API integration
- [ ] Parse structured receipt JSON
- [ ] Discard image after processing
- [ ] Save corrected parsed expense through expense use case

---

## 5. Subscriptions

### 5.1 Subscription List

Target paths:

```text
ui/subscriptions/list/SubscriptionsScreen.kt
ui/subscriptions/list/SubscriptionsViewModel.kt
ui/subscriptions/list/SubscriptionsUiState.kt
domain/model/Subscription.kt
domain/repository/SubscriptionRepository.kt
data/firebase/subscriptions/FirebaseSubscriptionRepository.kt
data/firebase/subscriptions/SubscriptionFirestoreMapper.kt
```

#### UI + State

- [ ] Build subscription list UI
- [ ] Add active/inactive grouping
- [ ] Add renewal state display
- [ ] Add empty, loading, and error states

#### Firebase / Data Integration

- [ ] Create `Subscription` domain model
- [ ] Create `SubscriptionRepository` contract
- [ ] Implement Firestore subscription repository
- [ ] Implement Firestore mapper
- [ ] Observe subscriptions from Firestore cache

### 5.2 Subscription Editor

Target paths:

```text
ui/subscriptions/editor/SubscriptionEditorScreen.kt
ui/subscriptions/editor/SubscriptionEditorViewModel.kt
ui/subscriptions/editor/SubscriptionEditorUiState.kt
domain/usecase/subscriptions/SaveSubscriptionUseCase.kt
domain/usecase/subscriptions/RenewSubscriptionUseCase.kt
domain/usecase/subscriptions/ComputeVirtualEntriesUseCase.kt
```

#### UI + State

- [ ] Build create/edit subscription UI
- [ ] Add link expense UI
- [ ] Add renewal workflow UI
- [ ] Add validation, error, and loading states

#### Firebase / Data Integration

- [ ] Save subscription
- [ ] Link subscription to expense
- [ ] Compute monthly amortized INR
- [ ] Generate virtual entries at runtime only
- [ ] Schedule renewal reminder through worker setup

---

## 6. Actions

### 6.1 Action List

Target paths:

```text
ui/actions/list/ActionsScreen.kt
ui/actions/list/ActionsViewModel.kt
ui/actions/list/ActionsUiState.kt
domain/model/ActionDefinition.kt
domain/model/ActionLog.kt
domain/repository/ActionRepository.kt
data/firebase/actions/FirebaseActionRepository.kt
data/firebase/actions/ActionFirestoreMapper.kt
```

#### UI + State

- [ ] Build daily action checklist UI
- [ ] Add action cards
- [ ] Add today log state
- [ ] Add empty, loading, and error states

#### Firebase / Data Integration

- [ ] Create action models
- [ ] Create `ActionRepository` contract
- [ ] Implement Firestore action repository
- [ ] Implement Firestore mapper
- [ ] Observe definitions and logs

### 6.2 Action Detail

Target paths:

```text
ui/actions/detail/ActionDetailScreen.kt
ui/actions/detail/ActionDetailViewModel.kt
ui/actions/detail/ActionDetailUiState.kt
```

#### UI + State

- [ ] Build historical log view
- [ ] Add trend display
- [ ] Add edit/delete entry actions

#### Firebase / Data Integration

- [ ] Load action history
- [ ] Save edited logs
- [ ] Apply last-write-wins behavior

### 6.3 Action Editor

Target paths:

```text
ui/actions/editor/ActionEditorScreen.kt
ui/actions/editor/ActionEditorViewModel.kt
ui/actions/editor/ActionEditorUiState.kt
domain/usecase/actions/SaveActionDefinitionUseCase.kt
domain/usecase/actions/LogActionUseCase.kt
```

#### UI + State

- [ ] Build create/edit action definition UI
- [ ] Add input type controls
- [ ] Add reminder settings UI
- [ ] Add validation, error, and loading states

#### Firebase / Data Integration

- [ ] Save action definitions
- [ ] Log action with deterministic `{actionId}_{date}` ID
- [ ] Schedule action reminder if enabled

---

## 7. Diary

### 7.1 Diary List

Target paths:

```text
ui/diary/list/DiaryScreen.kt
ui/diary/list/DiaryViewModel.kt
ui/diary/list/DiaryUiState.kt
domain/model/DiaryEntry.kt
domain/repository/DiaryRepository.kt
data/firebase/diary/FirebaseDiaryRepository.kt
data/firebase/diary/DiaryFirestoreMapper.kt
```

#### UI + State

- [ ] Build diary list grouped by date
- [ ] Add entry cards
- [ ] Add tag summary
- [ ] Add empty, loading, and error states

#### Firebase / Data Integration

- [ ] Create `DiaryEntry` model
- [ ] Create `DiaryRepository` contract
- [ ] Implement Firestore diary repository
- [ ] Implement Firestore mapper
- [ ] Observe diary entries from cache

### 7.2 Diary Editor

Target paths:

```text
ui/diary/editor/DiaryEntryScreen.kt
ui/diary/editor/DiaryEntryViewModel.kt
ui/diary/editor/DiaryEntryUiState.kt
domain/usecase/diary/SaveDiaryEntryUseCase.kt
```

#### UI + State

- [ ] Build create/edit entry UI
- [ ] Add structured tags UI
- [ ] Add validation, error, and loading states

#### Firebase / Data Integration

- [ ] Save diary entry
- [ ] Delete diary entry
- [ ] Apply last-write-wins behavior

---

## 8. Analytics

### 8.1 Analytics Dashboard

Target paths:

```text
ui/analytics/dashboard/AnalyticsScreen.kt
ui/analytics/dashboard/AnalyticsViewModel.kt
ui/analytics/dashboard/AnalyticsUiState.kt
domain/model/MonthlyAnalytics.kt
domain/repository/AnalyticsRepository.kt
data/firebase/analytics/FirebaseAnalyticsRepository.kt
data/firebase/analytics/MonthlyAnalyticsFirestoreMapper.kt
```

#### UI + State

- [ ] Build monthly analytics dashboard UI
- [ ] Add cash/budget analytics toggle
- [ ] Add spend trend chart
- [ ] Add category breakdown chart
- [ ] Add correlation cards
- [ ] Add empty, loading, and error states

#### Firebase / Data Integration

- [ ] Read `/analytics/monthly`
- [ ] Display read-only analytics
- [ ] Respect Firestore cache/offline reads

---

## 9. Conflicts

### 9.1 Conflict List

Target paths:

```text
ui/conflicts/list/ConflictsScreen.kt
ui/conflicts/list/ConflictsViewModel.kt
ui/conflicts/list/ConflictsUiState.kt
domain/model/Conflict.kt
domain/repository/ConflictRepository.kt
data/firebase/conflicts/FirebaseConflictRepository.kt
data/firebase/conflicts/ConflictFirestoreMapper.kt
```

#### UI + State

- [ ] Build conflict list UI
- [ ] Add conflict status/severity display
- [ ] Add empty, loading, and error states

#### Firebase / Data Integration

- [ ] Read `/conflicts`
- [ ] Observe unresolved conflicts
- [ ] Create conflict docs from expense/subscription version mismatches

### 9.2 Conflict Detail

Target paths:

```text
ui/conflicts/detail/ConflictDetailScreen.kt
ui/conflicts/detail/ConflictDetailViewModel.kt
ui/conflicts/detail/ConflictDetailUiState.kt
domain/usecase/conflicts/ResolveConflictUseCase.kt
```

#### UI + State

- [ ] Build side-by-side diff UI
- [ ] Add accept local/server actions
- [ ] Add resolution loading/error state

#### Firebase / Data Integration

- [ ] Resolve conflict transaction
- [ ] Update winning document version
- [ ] Delete resolved conflict doc

---

## 10. Settings

### 10.1 Settings Main

Target paths:

```text
ui/settings/main/SettingsScreen.kt
ui/settings/main/SettingsViewModel.kt
ui/settings/main/SettingsUiState.kt
```

#### UI + State

- [ ] Build settings menu UI
- [ ] Add navigation to currency, devices, Firebase, and export settings
- [ ] Add logout action UI

#### Firebase / Data Integration

- [ ] Logout from Firebase Auth
- [ ] Clear local config if user chooses reset

### 10.2 Currency Settings

Target paths:

```text
ui/settings/currency/CurrencySettingsScreen.kt
ui/settings/currency/CurrencySettingsViewModel.kt
ui/settings/currency/CurrencySettingsUiState.kt
domain/usecase/settings/UpdateDefaultCurrencyUseCase.kt
```

#### UI + State

- [ ] Build currency picker UI
- [ ] Add current default currency state
- [ ] Add save, loading, and error states

#### Firebase / Data Integration

- [ ] Update profile `defaultCurrency`
- [ ] Use Frankfurter currency list

### 10.3 Firebase Settings

Target paths:

```text
ui/settings/firebase/FirebaseSettingsScreen.kt
ui/settings/firebase/FirebaseSettingsViewModel.kt
ui/settings/firebase/FirebaseSettingsUiState.kt
```

#### UI + State

- [ ] Show connected project summary
- [ ] Build reset/reconnect Firebase config UI

#### Firebase / Data Integration

- [ ] Load stored Firebase config
- [ ] Clear/re-save config securely
- [ ] Reinitialize Firebase after config change

### 10.4 Device Settings

Target paths:

```text
ui/settings/devices/DeviceManagementScreen.kt
ui/settings/devices/DeviceManagementViewModel.kt
ui/settings/devices/DeviceManagementUiState.kt
```

#### UI + State

- [ ] Build registered devices UI
- [ ] Add set primary device action
- [ ] Add share config QR UI
- [ ] Add loading and error states

#### Firebase / Data Integration

- [ ] Read profile devices
- [ ] Update `primaryDeviceId`
- [ ] Generate QR config from stored credentials

### 10.5 Export Settings

Target paths:

```text
ui/settings/export/ExportScreen.kt
ui/settings/export/ExportViewModel.kt
ui/settings/export/ExportUiState.kt
domain/repository/ExportRepository.kt
data/export/FileExportRepository.kt
data/export/CsvExporter.kt
data/export/JsonExporter.kt
```

#### UI + State

- [ ] Build export format selection UI
- [ ] Add export progress/error state
- [ ] Add share sheet trigger state

#### Firebase / Data Integration

- [ ] Read cached operational data
- [ ] Generate CSV export
- [ ] Generate JSON export
- [ ] Launch Android share sheet
