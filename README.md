# MPos — Mobile POS (Android)

This repository contains the MPOS Android application skeleton following the architecture and guidelines in `android_document.md`. This scaffolding includes modularized core libraries and Compose-based feature modules ready for continued development.

## ✅ What I implemented

- Modularization:
  - `app/` — Android application module (Compose & Hilt-bootstrapped)
  - `core-common/` — shared utilities and system-level helpers
  - `core-di/` — Hilt DI modules (RepositoryModule, NetworkModule, DatabaseModule)
  - `core-network/` — Retrofit client and `ApiService` network DTOs
  - `core-db/` — Room DB skeleton (entities, DAO, database)
  - `core-data/` — domain models & repositories (Fake repo for sample data)
  - `feature-products/` — Compose-based Products screen + `ProductsViewModel`
  - `feature-cart/` — Compose-based Cart screen

- UI & Compose
  - Added Compose theme and a Compose-based `MainActivity` with navigation between Products and Cart.
  - Implemented simple `ProductsScreen` and `CartScreen` in the feature modules.

- DI & Repository
  - Created Hilt DI modules in `core-di` to bind `ProductRepository` to `FakeProductRepository`.
  - Added `ProductsViewModel` that uses `ProductRepository` and exposes a `StateFlow`.

- Persistence & Network
  - Added `core-db` with `ProductEntity`, `ProductDao`, and `AppDatabase`.
  - Added `core-network` with `ApiService` and Network DTO.

- Tests
  - Added a unit test skeleton for `ProductsViewModel` in `feature-products`.

## ⚠️ Current Build Status — Notes & Known Issues

This is a scaffold. Some parts of the implementation are intentionally minimal and intended to be extended.

During the build process, I ran Gradle builds and addressed notable issues. The current build contains warnings and a few remaining configuration issues you should note:

- Kotlin and Compose compiler versions: The project initially used Kotlin 2.0 which requires the Compose Compiler Gradle plugin. I changed Kotlin to 1.9.21 for compatibility with Compose 1.6.0, but work remains to ensure Compose Compiler layout/version compatibility. If you want to use Kotlin 2.0, apply the Compose Compiler Gradle plugin as documented by Android Studio and Jetpack Compose docs.

- Min SDK vs dependencies: Some libraries (e.g., navigation-event, some newer Compose-related libraries) require minSdk 23+. I changed the app and modules to use minSdk 23.

- Resource compatibility: Adaptive icons require Android API >= 26 for proper resource linking. The current app uses an adaptive icon resource and requires minimum sdk to be at least 26 for adaptive icons; the app minSdk can remain at 23 but resource processing may need a fallback modification or a fixed adaptive icon configuration.

- Compose compiler artifact mismatch: Some modules attempted to use Compose 1.6.0 and the compiler version was not found; choose stable pairing of Compose, compiler and Kotlin to avoid failures. See Jetpack Compose compatibility table for correct versions.

- Hilt & KAPT: The Hilt DI uses the Gradle plugin and annotation processors; ensure `pluginManagement` has mavenCentral and google() and `kapt` plugin is applied on modules with `@Inject` and `@HiltViewModel`.

- Some transitive dependencies and circular module references were resolved by separating network/dto types and adjusting module dependencies.

## 📦 How to build & run (Windows / PowerShell)

1. Ensure you have JDK 17 or greater (the project uses Java 11 compatibility flags; adjust Gradle toolchains if needed). 2) Install Android SDK + Android Studio.

2. In the workspace root, run Gradle tasks (PowerShell):

```pwsh
cd d:\laragon\www\mobiles\MPos
./gradlew --stop
./gradlew clean :app:assembleDebug --warning-mode=all
```

3. Install the debug apk on a connected device/emulator:

```pwsh
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

## ⚡ Development recommendations & next steps

1. Compose & Kotlin version compatibility
  - Decide between Kotlin 2.x and the latest Compose versions. If Kotlin 2.0 is required, apply the Compose Compiler Gradle plugin. If staying on 1.9.x, align Compose and compiler versions accordingly.

2. Network & Data mapping
  - Implement `NetworkProduct` -> `Product` mapping in `core-data`.
  - Implement `Retrofit` + `ApiService` with proper DTOs and serialization (Kotlinx or Moshi). Add `OkHttp` and necessary interceptors for logging & caching.

3. Room & RemoteMediator
  - Implement `RemoteMediator` in `core-data` to wire Paging 3 with Room and offline behavior.

4. Expand features & tests
  - Add more Compose screens per the `android_document.md`: Onboarding, Authentication, Dashboard, Checkout, ProcessPayment, Print flows.
  - Add `Paging 3` for products list, `Coil` image loading, `WorkManager` for offline sync.
  - Add `Detekt`, `ktlint`, and CI tasks.

5. Security & production
  - Remove any debug tokens, use secure storage for secrets and avoid adding production credentials to the repo.
  - Add Firebase authentication and analytics per `android_document.md` (optional steps for production).

## 🚀 What I suggest we do next (pick any item)

- Wire the `ApiService` to `ProductRepository` with Retrofit and map DTOs to domain model; optionally set up a `RemoteMediator` and Paging 3.
- Implement Onboarding and Sign-in Compose screens and integrate Firebase sign-in (or passwordless flow).
- Fix Compose/Compiler version mismatches and finish enabling Compose in all library modules.
- Add CI and static analysis flows (ktlint/detekt), plus unit and UI tests.

If you'd like, I can continue implementing the next step (e.g., pick one of the items above and I will implement it next). For now I've implemented a full modular skeleton with Compose navigation, Hilt DI, Room & Retrofit skeletons, and a Fake repository to bootstrap feature UIs.

## 📌 File Tree Summary

```
MPos/
 ├─ app/ (Compose-based application)
 ├─ core-common/ (utilities)
 ├─ core-di/ (Hilt modules & DI)
 ├─ core-data/ (repositories, domain models)
 ├─ core-db/ (Room entities & DAOs)
 ├─ core-network/ (Retrofit ApiService & DTOs)
 ├─ feature-products/ (Compose Product UI + ViewModel)
 └─ feature-cart/ (Compose Cart UI)
```

---

Thanks — let me know which of the next steps above you'd like me to implement next, and I’ll continue with a transparent plan and iteratively produce working code and tests.
