# ElectroShop Android

> A modern Android e-commerce client built with Kotlin and Jetpack Compose.

**Key info**

- **Package:** `com.gtg.electroshopandroid` ([app/build.gradle.kts](app/build.gradle.kts#L1-L80))
- **Minimum SDK:** 24 — **Target / Compile SDK:** 35 ([app/build.gradle.kts](app/build.gradle.kts#L1-L80))
- **Language:** Kotlin, Jetpack Compose
- **Architecture:** Repository + Retrofit network layer, ViewModel + Compose UI

## Related repositories

- Backend API: [electro-shop-backend](https://github.com/nguyenhbtrung/electro-shop-backend)
- Web frontend: [electro-shop-frontend](https://github.com/nguyenhbtrung/electro-shop-frontend)

This Android app is the mobile client for the Electro Shop backend API and complements the web frontend.

## What the project does

ElectroShop Android is a mobile client for an e-commerce platform. It provides:

- Product browsing and search
- Cart management and checkout (Stripe integration)
- Favorites, orders, returns and history screens
- User authentication and profile management
- Banner / promotional carousel and category/brand browsing

Key modules and code locations:

- App entry & navigation: [App.kt](app/src/main/java/com/gtg/electroshopandroid/App.kt#L1-L80)
- Main activity: [MainActivity.kt](app/src/main/java/com/gtg/electroshopandroid/MainActivity.kt#L1-L120)
- Application setup (Stripe key init): [ElectroShopApplication.kt](app/src/main/java/com/gtg/electroshopandroid/ElectroShopApplication.kt#L1-L80)
- Network services: [app/src/main/java/com/gtg/electroshopandroid/data/network](app/src/main/java/com/gtg/electroshopandroid/data/network)
- Repositories: [app/src/main/java/com/gtg/electroshopandroid/data/repository](app/src/main/java/com/gtg/electroshopandroid/data/repository)
- UI screens & components: [app/src/main/java/com/gtg/electroshopandroid/ui](app/src/main/java/com/gtg/electroshopandroid/ui)

## Why this is useful

- Implements a full-featured mobile storefront in modern Kotlin + Compose.
- Uses Retrofit + OkHttp for network calls and kotlinx.serialization for JSON.
- Includes Stripe test integration for payment flows.
- Clean separation between network, repository, and UI layers — easy to extend.

## Getting started

Prerequisites

- Android Studio (recommended) or command-line Gradle
- JDK 11 (project targets Java 11)

Quick start (Android Studio)

1. Open Android Studio and choose **Open** → repository root.
2. Let Gradle sync and download dependencies.
3. Run on an emulator or device.

Command-line build

Windows (from repo root):

```bash
gradlew.bat assembleDebug
gradlew.bat installDebug
```

macOS / Linux:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Notes

- The project uses the Gradle version and plugins configured in `gradle/` and the Gradle wrapper — prefer the wrapper commands above.
- A test Stripe publishable key is initialized in `ElectroShopApplication.kt` for local testing; replace with your own key for other environments ([ElectroShopApplication.kt](app/src/main/java/com/gtg/electroshopandroid/ElectroShopApplication.kt#L1-L80)).

Configuration

- API endpoints and Retrofit setup live under the `data/network` package. Update base URLs and authentication in the repository or network modules as needed.
- Token storage uses `TokenPreferences` ([preferences/TokenPreferences.kt](app/src/main/java/com/gtg/electroshopandroid/preferences/TokenPreferences.kt#L1-L200)).

Running tests

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Contributing

- Found a bug or want a feature? Open an issue or submit a pull request.
- Keep changes focused and include a short description and tests where applicable.
- Code style: Kotlin + idiomatic Compose patterns. Follow existing project structure.

If this repository adds a `CONTRIBUTING.md` later, follow those guidelines — otherwise use the standard GitHub PR workflow.

## Where to get help

- File issues in this repository. Include device/emulator, Android Studio version, and steps to reproduce.
- For implementation questions, inspect the code under `app/src/main/java/com/gtg/electroshopandroid`.

## Maintainers

- Maintainers: repository owner (see GitHub repository page).
