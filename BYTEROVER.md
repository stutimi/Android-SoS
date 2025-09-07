# Byterover Handbook

## Layer 1: System Overview

### 1.1. Project Purpose

A comprehensive Android safety application designed to provide immediate emergency assistance and location tracking for women's safety. The app features one-tap SOS alerts, real-time location sharing, and community-based safety features.

### 1.2. Tech Stack

-   **Language**: Kotlin
-   **Architecture**: MVVM (Model-View-ViewModel) with LiveData and ViewModel
-   **Core Android**: AndroidX, Activity, Fragment, Lifecycle, Navigation
-   **Database**: Room
-   **Networking**: Retrofit, OkHttp, Gson
-   **UI**: Material Design Components, View Binding, Glide
-   **Firebase**: Authentication, Firestore, Cloud Messaging, Analytics
-   **Google Play Services**: Location, Maps
-   **Permissions**: Dexter
-   **Background Processing**: WorkManager

### 1.3. Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern.

-   **Model**: Represents the data and business logic. This includes Room database entities, DAOs, and repositories.
-   **View**: The UI of the application, composed of Activities and Fragments. They observe ViewModels for data changes.
-   **ViewModel**: Acts as a bridge between the Model and the View. It holds and processes UI-related data and exposes it to the View via LiveData.

The project is structured into several packages:

-   `data`: Contains models, DAOs, repositories, and Firebase-related classes.
-   `ui`: Contains Activities, Fragments, and ViewModels for different screens.
-   `service`: Contains background services for location tracking and push notifications.
-   `utils`: Contains helper classes and constants.

## Layer 2: Module Map

### 2.1. Core Modules

-   **`auth`**: Manages user authentication (anonymous sign-in) using Firebase Authentication.
-   **`contacts`**: Manages emergency contacts, allowing users to add, view, and delete them. The data is stored locally using Room.
-   **`home`**: The main screen with the SOS button. It handles the SOS trigger and initiates the alert process.
-   **`maps`**: Displays the user's location and the location of trusted contacts on a map.
-   **`tracking`**: Manages real-time location sharing with trusted contacts.
-   **`settings`**: Provides options to configure the app's settings.

### 2.2. Data Layer

-   **`ContactRepository`**: Manages contact-related data operations, interacting with the `ContactDao`.
-   **`LocationRepository`**: Manages location data.
-   **`SosRepository`**: Manages SOS-related data and events.
-   **`FirebaseRepository`**: Handles interactions with Firebase services like Firestore.
-   **`SosDatabase`**: The Room database that stores contacts and other local data.

### 2.3. Service Layer

-   **`SosFirebaseMessagingService`**: Handles incoming push notifications from Firebase Cloud Messaging.
-   **`PushNotificationTester`**: A utility to test push notifications.

## Layer 3: Integration Guide

### 3.1. APIs and Endpoints

The application does not expose any of its own APIs. It communicates with external services:

-   **Firebase**:
    -   Authentication for user management.
    -   Firestore for real-time data storage.
    -   Cloud Messaging for push notifications.
-   **Google Maps API**:
    -   Used to display maps and user locations. The API key is configured in `local.properties`.

### 3.2. Configuration

-   **`google-services.json`**: Firebase project configuration file.
-   **`local.properties`**: Contains the `MAPS_API_KEY`.
-   **`build.gradle.kts`**: Contains build configurations, dependencies, and other project settings.

## Layer 4: Extension Points

### 4.1. Design Patterns

-   **MVVM**: As described in the architecture section.
-   **Repository Pattern**: Used to abstract data sources.
-   **Singleton**: The `SosDatabase` is implemented as a singleton.

### 4.2. Customization

-   **Theming**: The app's theme can be customized in `res/values/themes.xml`.
-   **SOS Behavior**: The SOS logic in `HomeViewModel` and related classes can be extended to add new alert mechanisms (e.g., sending emails, integrating with other services).
-   **New Features**: The modular structure allows for adding new features by creating new packages under the `ui` and `data` directories.