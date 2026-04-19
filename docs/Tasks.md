# 📘 Shadow Memoir — Android Implementation Task Breakdown

**Version:** 1.0 (Derived from Architecture v3.0)
**Platform:** Android (Kotlin + Jetpack Compose)
**Architecture Style:** Clean Architecture (data/domain/ui)
**Theme:** ✅ Use imported theme consistently across all UI components

---

## 🔰 0. Project Setup & Foundations

### 🔧 Core Setup

* [ ] Create Android project (min SDK aligned with Health Connect)
* [ ] Enable **Jetpack Compose**
* [ ] Setup **Hilt DI**
* [ ] Configure **Firebase SDK (dynamic init)**
* [ ] Enable Firestore offline persistence
* [ ] Setup navigation (Compose Navigation)

### 🔐 Authentication

* [ ] Implement Firebase Auth (Google Sign-In)
* [ ] Persist login session
* [ ] Logout flow
* [ ] Handle auth errors & edge cases

### 🎨 Theme Integration

* [ ] Apply imported theme globally
* [ ] Create reusable:

    * [ ] Typography system
    * [ ] Color tokens
    * [ ] Spacing system
* [ ] Ensure:

    * [ ] Dark mode compatibility
    * [ ] Consistent component styling (buttons, cards, inputs)

---

## 🧱 1. Core Architecture Layers

### 📦 Data Layer

* [ ] FirebaseManager (dynamic initialization)
* [ ] Firestore collections setup
* [ ] Repository implementations:

    * [ ] ExpenseRepository
    * [ ] SubscriptionRepository
    * [ ] ActionRepository
    * [ ] DiaryRepository
    * [ ] HealthRepository
    * [ ] AnalyticsRepository
    * [ ] ConflictRepository

### 🧠 Domain Layer

* [ ] Define all models:

    * [ ] Expense
    * [ ] Subscription
    * [ ] ActionDefinition
    * [ ] ActionLog
    * [ ] DiaryEntry
    * [ ] HealthSnapshot
    * [ ] MonthlyAnalytics
* [ ] Implement use cases:

    * [ ] SaveExpenseUseCase
    * [ ] GetCashViewUseCase
    * [ ] GetBudgetViewUseCase
    * [ ] ComputeVirtualEntriesUseCase
    * [ ] LogActionUseCase
    * [ ] SaveDiaryEntryUseCase
    * [ ] ResolveConflictUseCase
    * [ ] SyncHealthDataUseCase

### 🎛️ UI Layer

* [ ] MVVM setup (ViewModel per feature)
* [ ] State management using StateFlow
* [ ] Error + loading states

---

## 💰 2. Expense Tracking Module

### 🧾 Core Features

* [ ] Add expense (manual)
* [ ] Edit expense
* [ ] Delete expense
* [ ] Expense list view (grouped by date)

### 🌍 FX Conversion

* [ ] Integrate Frankfurter API
* [ ] Implement:

    * [ ] Live rate fetch
    * [ ] Daily cache (in-memory)
    * [ ] Offline fallback
* [ ] Compute:

    * [ ] amountINR
    * [ ] fxRateUsed
    * [ ] fxDate

### 📂 Categories

* [ ] Default categories seed
* [ ] Custom category creation
* [ ] Category picker UI

### 🧠 Business Logic

* [ ] Handle:

    * [ ] Subscription-linked expenses
    * [ ] Multi-currency input
    * [ ] Tagging system

---

## 📊 3. Budget Views (Critical Feature)

### 💵 Cash View

* [ ] Show all expenses as-is
* [ ] Include subscription lump sums

### 📉 Budget View

* [ ] Exclude subscription expenses
* [ ] Compute virtual monthly entries
* [ ] Inject computed entries at runtime

### 🔄 View Toggle

* [ ] Toggle UI (default = Budget View)
* [ ] Recompute list dynamically

---

## 🔁 4. Subscription Module

### 🧾 Features

* [ ] Create subscription
* [ ] Link to expense
* [ ] Edit subscription
* [ ] Renewal workflow

### 🧮 Logic

* [ ] Calculate:

    * [ ] monthlyAmortizedINR
* [ ] Generate:

    * [ ] Virtual entries (runtime only)

### 🔔 Reminders

* [ ] Renewal reminder scheduling
* [ ] Configurable days-before alert

---

## 📷 5. AI Receipt Scanner

### 📸 Capture

* [ ] CameraX integration
* [ ] Gallery picker

### 🤖 Processing

* [ ] Convert image → base64
* [ ] Call Gemini API
* [ ] Parse structured JSON

### 🔁 Fallback

* [ ] ML Kit OCR (offline fallback)
* [ ] Manual correction UI

### ⚠️ Security

* [ ] Ensure:

    * [ ] Image NOT stored
    * [ ] Memory cleared after processing

---

## 🏃 6. Health Integration

### 🔌 Setup

* [ ] Integrate Health Connect API
* [ ] Request permissions

### 🔄 Sync

* [ ] WorkManager (every 4 hours)
* [ ] Fetch last 24h data

### 🧠 Logic

* [ ] Only primary device writes data
* [ ] Upsert daily HealthSnapshot

---

## ✅ 7. Action Tracking Module

### 🧩 Features

* [ ] Create action definitions
* [ ] Support input types:

    * [ ] Boolean
    * [ ] Number
    * [ ] Duration
    * [ ] Text
    * [ ] Scale

### 📅 Daily Logs

* [ ] One log per action per day
* [ ] Deterministic ID: `{actionId}_{date}`

### 🔔 Reminders

* [ ] Per-action reminder scheduling

---

## 📓 8. Diary Module

### ✍️ Features

* [ ] Create multiple entries per day
* [ ] Edit entries
* [ ] Delete entries

### 😊 Mood Tracking

* [ ] Mood selector UI

### 🏷️ Tags

* [ ] Support:

    * [ ] Energy
    * [ ] Gratitude
    * [ ] Reflection
    * [ ] Custom tags

---

## 🔄 9. Sync & Offline System

### 🔁 Firestore Sync

* [ ] Use local cache for reads/writes
* [ ] Real-time listeners

### 📡 Offline Support

* [ ] Queue writes
* [ ] Sync on reconnect

### 📊 Sync Status

* [ ] Show pending writes indicator

---

## ⚔️ 10. Conflict Resolution

### 📌 Trigger

* [ ] Version mismatch detection

### 🗂️ Storage

* [ ] Save conflicts in `/conflicts`

### 🖥️ UI

* [ ] Conflict list screen
* [ ] Side-by-side diff view

### 🔧 Resolution

* [ ] Accept local OR server version
* [ ] Update version
* [ ] Delete conflict doc

---

## 🔔 11. Notifications & Nudges

### 📅 Scheduled Notifications

* [ ] Daily spend reminder
* [ ] Weekly summary
* [ ] Budget anomaly alerts
* [ ] Subscription reminders
* [ ] Action reminders

### ⚙️ Implementation

* [ ] WorkManager jobs
* [ ] Primary device guard check

---

## 📤 12. Export Module

### 📁 Export Formats

* [ ] CSV export
* [ ] JSON export

### 🧠 Data Aggregation

* [ ] Join:

    * [ ] Expenses
    * [ ] Health
    * [ ] Actions
    * [ ] Diary

### 📤 Delivery

* [ ] Android share sheet

---

## 📊 13. Analytics Module (Read-only)

### 📥 Fetch

* [ ] Read `/analytics/monthly`

### 📊 Display

* [ ] Cash View analytics
* [ ] Budget View analytics
* [ ] Charts:

    * [ ] Spend trends
    * [ ] Category breakdown
    * [ ] Correlations

---

## ⚙️ 14. Settings Module

### 🔧 Features

* [ ] Currency selection
* [ ] Device management
* [ ] Set primary device
* [ ] Export data
* [ ] Logout

---

## 🧪 15. Testing & Quality

### ✅ Unit Tests

* [ ] Use cases
* [ ] FX calculations
* [ ] Budget view logic

### 🧪 Integration Tests

* [ ] Firestore sync
* [ ] Conflict handling

### 📱 UI Tests

* [ ] Critical flows:

    * [ ] Add expense
    * [ ] Budget toggle
    * [ ] Subscription flow

---

## 🚀 16. Release Preparation

* [ ] App icons & branding
* [ ] Proguard / R8 setup
* [ ] Crashlytics integration
* [ ] Performance monitoring
* [ ] Play Store listing assets

---

# 🧠 Key Implementation Priorities

### Phase 1 (MVP Core)

* Auth + Firestore setup
* Expense tracking
* Budget vs Cash view
* Subscription basics

### Phase 2

* Actions + Diary
* Notifications
* Health integration

### Phase 3

* AI scanning
* Conflict resolution
* Analytics

---

# ⚠️ Critical Rules (Must Follow)

* ❗ Offline-first always
* ❗ Never store receipt images
* ❗ Budget view computed, never persisted
* ❗ Health data → primary device only
* ❗ Expense conflicts must NOT auto-resolve
