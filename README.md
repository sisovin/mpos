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

- Kotlin and Compose compiler versions: The project uses Kotlin 1.9.10 aligned with Compose 1.5.3. Avoid mixing Kotlin 2.0 and older Compose compiler versions unless you intentionally apply the Compose Compiler Gradle plugin as documented by Jetpack Compose.

- Min SDK vs dependencies: Some libraries (e.g., navigation-event, some newer Compose-related libraries) require minSdk >= 23. This repository uses minSdk 26 across modules now to support adaptive icons and avoid resource linking failures.

- Compose compiler artifact mismatch: Some modules attempted to use Compose 1.6.0 and the compiler version was not found; choose stable pairing of Compose, compiler and Kotlin to avoid failures. See Jetpack Compose compatibility table for correct versions.

-
Environment notes and build tips:
- Use Java JDK 17 (OpenJDK 17 or Oracle JDK 17) for building the project. If you have a newer JDK (20/21) installed, either install JDK 17 and set `JAVA_HOME` to it, or set `org.gradle.java.home` in `gradle.properties` pointing to a JDK 17 installation.
- If you see KAPT IllegalAccessErrors related to com.sun.tools.javac (module export issues), make sure to build with JDK 17 or apply JVM export flags in `gradle.properties` (see below), and run `./gradlew --stop` then `./gradlew clean --no-build-cache build`.
- Example: Add this to `gradle.properties` when you must use a newer JDK (not recommended):
  org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 --add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED

- Hilt & KAPT: The Hilt DI uses the Gradle plugin and annotation processors; ensure `pluginManagement` has mavenCentral and google() and `kapt` plugin is applied on modules with `@Inject` and `@HiltViewModel`.
Use the included helper script to validate your local Java/Gradle environment and get recommendations:

```powershell
.\scripts\check-environment.ps1
```

Follow the script instructions to set `JAVA_HOME` or `org.gradle.java.home`.

If you'd like the script to automatically set `org.gradle.java.home` to an existing JDK 17 install, use:

```powershell
.\scripts\set-java-home.ps1
```

Or set a specific JDK path manually:

```powershell
.\scripts\set-java-home.ps1 -Path 'C:\Program Files\Java\jdk-17.0.8'
```
=======
- Kotlin and Compose compiler versions: The project uses Kotlin 1.9.10 aligned with Compose 1.5.3. Avoid mixing Kotlin 2.0 and older Compose compiler versions unless you intentionally apply the Compose Compiler Gradle plugin as documented by Jetpack Compose.

- Min SDK vs dependencies: Some libraries (e.g., navigation-event, some newer Compose-related libraries) require minSdk >= 23. This repository uses minSdk 26 across modules now to support adaptive icons and avoid resource linking failures.

- Compose compiler artifact mismatch: Some modules attempted to use Compose 1.6.0 and the compiler version was not found; choose stable pairing of Compose, compiler and Kotlin to avoid failures. See Jetpack Compose compatibility table for correct versions.

Environment notes and build tips:
- Use Java JDK 17 (OpenJDK 17 or Oracle JDK 17) for building the project. If you have a newer JDK (20/21) installed, either install JDK 17 and set `JAVA_HOME` to it, or set `org.gradle.java.home` in `gradle.properties` pointing to a JDK 17 installation.
- If you see KAPT IllegalAccessErrors related to com.sun.tools.javac (module export issues), make sure to build with JDK 17 or apply JVM export flags in `gradle.properties` (see below), and run `./gradlew --stop` then `./gradlew clean --no-build-cache build`.
- Example: Add this to `gradle.properties` when you must use a newer JDK (not recommended):
  org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8 --add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED

- Hilt & KAPT: The Hilt DI uses the Gradle plugin and annotation processors; ensure `pluginManagement` has mavenCentral and google() and `kapt` plugin is applied on modules with `@Inject` and `@HiltViewModel`.
Use the included helper script to validate your local Java/Gradle environment and get recommendations:

```powershell
.\scripts\check-environment.ps1
```

Follow the script instructions to set `JAVA_HOME` or `org.gradle.java.home`.

If you'd like the script to automatically set `org.gradle.java.home` to an existing JDK 17 install, use:

```powershell
.\scripts\set-java-home.ps1
```

Or set a specific JDK path manually:

```powershell
.\scripts\set-java-home.ps1 -Path 'C:\Program Files\Java\jdk-17.0.8'
```

Launcher & Logo
- The app now displays the provided `assets/launcher.png` as the in-app splash screen background and `assets/ic_mpos_logo.png` as the in-app MPos app logo. This is implemented in Compose under `app/src/main/java/com/peanech/mpos/ui/splash/SplashScreen.kt` and `app/src/main/java/com/peanech/mpos/ui/logo/AppLogo.kt`.
- Note: To change the system launcher icon (the icon shown on the Android home screen), you must copy the logo images into `res/mipmap-<density>/` (mdpi/hdpi/xhdpi/xxhdpi) and set `android:icon` and `android:roundIcon` in `AndroidManifest.xml` to `@mipmap/ic_launcher`. This is a resource-level change which requires binary image files in `res/mipmap-` and can't be done using only files in `assets`.

Manual steps to set icon (optional):
1) Generate density variants of `ic_mpos_logo.png` (mdpi/hdpi/xhdpi/xxhdpi) — Android Studio's Image Asset tool can do this.
2) Replace the existing `mipmap-*/ic_launcher.png` and `mipmap-*/ic_launcher_round.png` with the generated images.
3) Verify `AndroidManifest.xml` `android:icon`/`android:roundIcon` point to `@mipmap/ic_launcher`.

- Some transitive dependencies and circular module references were resolved by separating network/dto types and adjusting module dependencies.

### 🛠 Build fixes I applied (summary)

- Forced `com.squareup:javapoet:1.13.0` in the root `build.gradle.kts` and added it to the buildscript classpath to eliminate the Hilt plugin `NoSuchMethodError` caused by multiple JavaPoet versions on the build/tooling classpath.
- Aligned Kotlin to 1.9.10 and Compose to 1.5.3 (Compose BOM) across modules.
- Set minSdk to 26 across modules to avoid resource linking failures (adaptive icons requirement).
- Added `--add-exports`/`--add-opens` flags in `gradle.properties` as a fallback when building with newer JDK versions (recommended to build with JDK 17 instead).
- Added environment helper scripts: `scripts/check-environment.ps1` and `scripts/set-java-home.ps1` to help enforce JDK 17 in Windows environments.

With these adjustments the local build completes successfully (tested with `./gradlew clean :app:assembleDebug`).

>>>>>>> 12892f7 (feat(ui): Improve Products & Cart UI, add CartRepository & CartViewModel, add product cards and bottom cart bar; Hilt binding for cart; in-memory cart implementation; update themes and build configs)
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
