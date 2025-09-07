# Byterover Handbook: Smart SOS - Women Safety App

This handbook provides a comprehensive overview of the Smart SOS application, its architecture, and its modules.

## Layer 1: System Overview

### 1.1. Project Purpose

A comprehensive Android safety application designed to provide immediate emergency assistance and location tracking for women's safety. The app features one-tap SOS alerts, real-time location sharing, and community-based safety features.

### 1.2. Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM with LiveData and ViewModel
- **UI:** View Binding, Android Navigation Component, Material Design
- **Database:** Room
- **Background Processing:** WorkManager
- **Networking:** Retrofit, OkHttp, Gson
- **Image Loading:** Glide
- **Firebase:** Authentication, Firestore, Cloud Messaging, Analytics
- **Location & Maps:** Google Play Services Location, Google Play Services Maps
- **Permissions:** Dexter

### 1.3. Architecture

The application follows the Model-View-ViewModel (MVVM) architecture pattern.

- **Model:** Represents the data and business logic. This includes Room database entities, repositories, and data sources.
- **View:** Represents the UI of the application. This includes Activities and Fragments.
- **ViewModel:** Acts as a bridge between the Model and the View. It holds the UI-related data and exposes it to the View through LiveData.

The app is structured into several packages, including `ui`, `service`, `data`, and `utils`. The `ui` package contains the presentation layer, the `service` package contains background services, the `data` package contains the data layer, and the `utils` package contains utility classes.

## Layer 2: Module Map

### 2.1. Core Modules

- **Authentication:** Handles user sign-up, sign-in, and session management.
  - `AuthActivity.kt`: The main entry point for the authentication flow.
  - `AuthViewModel.kt`: The ViewModel for the authentication screen.
- **Home:** The main screen with the SOS button and shake detection.
  - `HomeFragment.kt`: The UI for the home screen.
  - `HomeViewModel.kt`: The ViewModel for the home screen.
- **Contacts:** Manages emergency contacts.
  - `ContactsFragment.kt`: The UI for managing contacts.
  - `ContactsViewModel.kt`: The ViewModel for the contacts screen.
  - `AddContactDialogFragment.kt`: A dialog for adding new contacts.
- **Tracking:** Real-time location sharing and route monitoring.
  - `TrackingFragment.kt`: The UI for the tracking screen.
- **Settings:** Application settings and user preferences.
  - `SettingsFragment.kt`: The UI for the settings screen.
- **SOS Service:** The background service responsible for sending emergency alerts.
  - `SosFirebaseMessagingService.kt`: Handles incoming push notifications.
- **Location Service:** The background service for location tracking.
  - `LocationRepository.kt`: The repository for location data.

## Layer 3: Integration Guide

### 3.1. Firebase Integration

The application is tightly integrated with Firebase for various backend services:

- **Firebase Authentication:** Used for user authentication and management.
- **Firebase Firestore:** Used as the real-time database for storing user data, contacts, and SOS events.
- **Firebase Cloud Messaging:** Used for sending and receiving push notifications, which are crucial for the SOS feature.

The Firebase configuration is stored in the `app/google-services.json` file.

### 3.2. Google Maps Integration

The application uses the Google Maps SDK for displaying maps and the Google Play Services Location API for location tracking. The Google Maps API key is configured in the `app/build.gradle.kts` file.

## Layer 4: Extension Points

### 4.1. Design Patterns

- **Repository Pattern:** The application uses the repository pattern to abstract the data sources. This allows for easy swapping of data sources without affecting the rest of the application.
- **Dependency Injection:** The app follows the principle of dependency injection by passing dependencies through constructors. This makes the code more modular and testable.
- **ViewModel and LiveData:** The use of ViewModel and LiveData allows for a clean separation of concerns and makes the UI more resilient to configuration changes.

### 4.2. Customization Areas

- **Room Database:** The local database can be extended by adding new entities and DAOs in the `com.xenonesis.womensafety.data` package.
- **WorkManager:** New background tasks can be added by creating new Worker classes and enqueueing them with the WorkManager.
- **UI:** The UI can be customized by modifying the XML layouts in the `app/src/main/res/layout` directory and the styles in the `app/src/main/res/values` directory.