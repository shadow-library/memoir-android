# AGENTS.md

This file provides guidance to AI models when working with code in this repository.

## Authoritative documents

Before making non-trivial changes, read:

- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) — full app spec: data model, sync strategy, conflict rules, BYOF model, currency/FX, health integration, notifications. Treat as the source of truth for behavior.
- [docs/DIRECTORY_STRUCTURE.md](docs/DIRECTORY_STRUCTURE.md) — **mandatory** package structure and file-placement rules. New code must follow it; the §13 review checklist applies before merging.
- [docs/Tasks.md](docs/Tasks.md) — per-screen task tracker showing what is done vs remaining.

If something in code conflicts with these docs, the docs win — flag the conflict rather than silently following the code.

## Build / run

Gradle wrapper is checked in. Common commands (run from repo root):

```bash
./gradlew assembleDebug              # build debug APK
./gradlew installDebug               # build + install on connected device/emulator
./gradlew test                       # JVM unit tests
./gradlew testDebugUnitTest --tests "com.shadow.apps.memoir.SomeTest"   # single test
./gradlew connectedDebugAndroidTest  # instrumentation tests (needs device/emulator)
./gradlew lint                       # Android lint
./gradlew :app:assembleRelease       # release build (minify + shrink enabled)
```

Toolchain: AGP 9.x, Kotlin 2.3.x, JDK 17, `compileSdk` 37, `minSdk` 31. Versions are centralized in [gradle/libs.versions.toml](gradle/libs.versions.toml) — update there, not in module build files.

Debug builds use `applicationIdSuffix = ".debug"`, so debug and release variants can coexist on the same device.

## Architecture in one screen

Clean Architecture with strict layer separation. Dependencies flow inward: `ui → domain ← data`.

- `domain/` is **pure Kotlin** — no Android, Firebase, Compose, Hilt, Retrofit, OkHttp, or Health Connect imports. Contains `model/`, `repository/` (interfaces only), and `usecase/<feature>/`.
- `data/` implements domain repositories. Subfolders are by external system, not by feature: `data/firebase/<collection>/`, `data/preferences/`, `data/fx/`, `data/ai/`, `data/healthconnect/`, `data/export/`. Each Firestore feature folder pairs a `Firebase<Entity>Repository.kt` with a sibling `<Entity>FirestoreMapper.kt`.
- `ui/` is Compose-only. Every route gets its own folder owning `<Name>Screen.kt`, `<Name>ViewModel.kt`, `<Name>UiState.kt`. Repository/Firestore/HTTP calls in a Screen or ViewModel are a layering violation — push them into a use case. Components shared by multiple screens of one feature live in `ui/<feature>/components/`; app-wide components in `ui/components/`.
- `di/` holds Hilt modules. `RepositoryModule` `@Binds` domain interfaces to data implementations.
- `workers/` holds WorkManager classes. Workers must be thin — they call use cases, never Firestore or Health Connect directly.

Block comments (`/* Section */`) are required to segregate logical groups inside non-trivial files. See DIRECTORY_STRUCTURE §10 for required section names per file type (`Route` / `Content` / `Preview` for screens, `State` / `Initialization` / `Events` for ViewModels, `References` / `Reads` / `Writes` for repository impls, etc.).

## Domain invariants worth knowing before touching data code

These are app-wide rules, not per-screen details:

- **BYOF (Bring Your Own Firebase).** There is no shared backend. Each user provisions their own Firebase project; the app loads credentials at runtime via `FirebaseManager` using a named app (`"memoir"`). Never hardcode Firebase config or assume a default app.
- **Credentials are encrypted at rest.** Firebase config and device identity go through `EncryptedConfigStore` (AES-256-GCM, key in Android Keystore). Never write Firebase config to plain `SharedPreferences`, logs, analytics, or crash reports.
- **Profile lives at `users/{uid}`.** The user document _is_ the profile (uid, primaryDeviceId, defaultCurrency, devices[], createdAt). All other collections are subcollections of it.
- **Primary-device guard.** Health polling, scheduled notifications, and renewal reminders only run on the device whose ID matches `primaryDeviceId`. Workers must read this from the Firestore offline cache before acting; secondary devices skip silently. `deviceId` on Android = `Settings.Secure.ANDROID_ID`.
- **Conflict strategy is per-entity.** Expenses and Subscriptions use version-checked transactions and surface conflict docs in `/conflicts/{id}` for manual resolution. ActionLog and DiaryEntry use last-write-wins (plain `.set()`, no version check). HealthSnapshot and MonthlyAnalytics have a single writer so no conflict logic exists.
- **Money is stored twice.** Every expense persists `amount`+`currency` verbatim _and_ `amountINR` (rounded integer) plus `fxRateUsed`/`fxDate`. INR expenses store `fxRateUsed: 1.0`. Use cases — not repositories — own FX conversion.
- **Subscriptions are amortized at render time.** `monthlyAmortizedINR` lives on the doc, but the per-month "virtual entries" used by Budget View are computed in the app from `[startDate, nextRenewalDate)` — never written to Firestore. Real lump-sum payments are tagged `isSubscriptionPayment: true` and hidden from Budget View.
- **Receipt images are never stored.** Sent to Gemini, parsed, discarded.
- **No secondary local DB.** Firestore offline persistence is the only local store for app data.

## Onboarding routing

Splash decides where to send the user based on three flags read from `EncryptedConfigStore` and Firebase Auth:

```
!hasCredentials                              → GettingStarted → … → SignIn → DeviceSetup → SetupComplete → Home
hasCredentials, !signedIn, !setupComplete    → SignIn → DeviceSetup → …
hasCredentials, !signedIn,  setupComplete    → SignIn → Home (re-auth)
hasCredentials,  signedIn, !setupComplete    → DeviceSetup → …
hasCredentials,  signedIn,  setupComplete    → Home
```

`GetStartupDestinationUseCase` and `GetPostSignInDestinationUseCase` encapsulate this. Don't replicate the decision in screens.

## State of the codebase

The full onboarding flow (5 steps) is implemented end-to-end. Splash, navigation scaffold, Hilt wiring, Firestore profile writes, encrypted config, and Frankfurter currency fetch are in place. Most non-onboarding feature folders listed in DIRECTORY_STRUCTURE.md don't exist yet — they will be created as their screens are built. Track progress in [docs/Tasks.md](docs/Tasks.md).

## Conventions

- Naming: `<Name>Screen.kt` / `<Name>ViewModel.kt` / `<Name>UiState.kt` for UI; `<Entity>.kt` / `<Entity>Repository.kt` / `<Action><Entity>UseCase.kt` for domain; `Firebase<Entity>Repository.kt` / `<Entity>FirestoreMapper.kt` for Firestore impls.
- Prefer Material 3 (`androidx.compose.material3`) and the extended icon set already on the classpath. Inter is the project font (see `ui/theme/Type.kt`).
- New libraries go in [gradle/libs.versions.toml](gradle/libs.versions.toml) under both `[versions]` and `[libraries]`, then referenced via `libs.<name>`.
