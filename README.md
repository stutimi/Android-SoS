# Smart SOS - Women Safety App 🚨

A comprehensive Android safety application designed to provide immediate emergency assistance and location tracking for women's safety. The app features one-tap SOS alerts, real-time location sharing, and community-based safety features.

## 📱 Features

### Core Safety Features
- **One-Tap SOS Button** - Instantly send emergency alerts with live location to trusted contacts
- **Safety Route Tracking** - Share your trip with friends/family with automatic deviation alerts
- **Panic Gesture Trigger** - Shake phone 3 times to activate SOS without opening the app
- **Emergency Contacts Management** - Add and manage trusted contacts for emergency situations
- **Real-time Location Sharing** - Share live location during emergencies or planned trips

### Smart Features
- **Community Alert Network** - Local volunteers get notified when someone nearby needs help
- **Privacy-First Design** - Location is only shared during active SOS, not always
- **Background Services** - Continuous monitoring even when app is closed
- **Firebase Integration** - Real-time notifications and cloud messaging
- **Shake Detection** - Advanced sensor-based emergency trigger

## 🛠️ Technical Specifications

### App Information
- **Package Name**: `com.xenonesis.womensafety`
- **App Name**: Smart SOS
- **Version**: 1.0 (Version Code: 1)
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 36)
- **Language**: Kotlin

### Architecture & Technologies

#### Core Android Components
- **Architecture**: MVVM with LiveData and ViewModel
- **UI**: View Binding for type-safe view references
- **Navigation**: Android Navigation Component with Bottom Navigation
- **Database**: Room Database for local data persistence
- **Background Processing**: WorkManager for reliable background tasks

#### Key Libraries & Dependencies

##### Firebase Services
- Firebase Authentication (User management)
- Firebase Firestore (Real-time database)
- Firebase Cloud Messaging (Push notifications)
- Firebase Analytics (Usage tracking)

##### Location & Maps
- Google Play Services Location (GPS tracking)
- Google Play Services Maps (Map integration)
- Background location tracking with foreground services

##### Network & Data
- Retrofit 2.11.0 (REST API communication)
- OkHttp 4.12.0 (HTTP client with logging)
- Gson 2.11.0 (JSON serialization)

##### UI & UX
- Material Design Components 1.12.0
- Glide 4.16.0 (Image loading and caching)
- Custom emergency button with visual feedback

##### Permissions & Security
- PermissionsDispatcher 4.9.2 (Runtime permission handling)
- Comprehensive permission management for location, SMS, and phone access

### Required Permissions
```xml
<!-- Location Services -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />

<!-- Communication -->
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.CALL_PHONE" />

<!-- Network & Connectivity -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- System Services -->
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Bluetooth (for wearable integration) -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/xenonesis/womensafety/
│   │   ├── ui/
│   │   │   ├── MainActivity.kt              # Main activity with bottom navigation
│   │   │   ├── AuthActivity.kt              # Authentication screen
│   │   │   ├── home/HomeFragment.kt         # Emergency button & shake detection
│   │   │   ├── tracking/TrackingFragment.kt # Location tracking interface
│   │   │   ├── contacts/ContactsFragment.kt # Emergency contacts management
│   │   │   └── settings/SettingsFragment.kt # App settings and preferences
│   │   ├── service/
│   │   │   ├── SosService.kt                # Emergency alert service
│   │   │   ├── LocationTrackingService.kt   # Background location tracking
│   │   │   └── SosFirebaseMessagingService.kt # Push notification handling
│   │   ├── receiver/
│   │   │   ├── BootReceiver.kt              # Auto-start on device boot
│   │   │   └── ShakeReceiver.kt             # Shake gesture detection
│   │   └── SosApplication.kt                # Application class
│   ├── res/
│   │   ├── layout/                          # UI layouts
│   │   ├── drawable/                        # Icons and graphics
│   │   ├── values/                          # Strings, colors, themes
│   │   └── menu/                            # Navigation menus
│   └── AndroidManifest.xml                  # App configuration
├── build.gradle.kts                         # App-level build configuration
└── google-services.json                     # Firebase configuration
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Google Play Services
- Firebase project setup

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd women-safety-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Firebase Setup**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add your Android app to the Firebase project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication, Firestore, and Cloud Messaging in Firebase

4. **Configure API Keys**
   - Add your Google Maps API key to the project
   - Update any required API configurations

5. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Development Setup

1. **Sync Project**
   ```bash
   ./gradlew sync
   ```

2. **Run Tests**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

3. **Generate APK**
   ```bash
   ./gradlew assembleRelease
   ```

## 📋 App Screens & Navigation

### Bottom Navigation Tabs
1. **Home** - Emergency SOS button and quick actions
2. **Tracking** - Location sharing and route monitoring
3. **Contacts** - Emergency contacts management
4. **Settings** - App preferences and privacy settings

### Key User Flows
1. **Emergency Alert**: Home → Tap SOS → Confirm → Send alerts
2. **Add Contact**: Contacts → Add → Enter details → Save
3. **Start Tracking**: Tracking → Start → Share with contacts
4. **Shake Detection**: Automatic trigger from any screen

## 🔧 Configuration

### Build Variants
- **Debug**: Development build with logging enabled
- **Release**: Production build with ProGuard optimization

### Gradle Configuration
- **Compile SDK**: 36
- **Min SDK**: 24
- **Target SDK**: 36
- **Java Version**: 11
- **Kotlin Version**: 2.0.21

## 🛡️ Security & Privacy

### Privacy Features
- Location data is only shared during active emergencies
- All communications are encrypted
- User data is stored securely with Firebase
- Minimal data collection approach

### Security Measures
- Runtime permission requests
- Secure API communication with HTTPS
- ProGuard code obfuscation in release builds
- Firebase security rules implementation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain consistent indentation

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support & Contact

For support, feature requests, or bug reports:
- Create an issue in the repository
- Contact: [Your contact information]

## 🙏 Acknowledgments

- Firebase for backend services
- Google Play Services for location APIs
- Material Design for UI components
- Open source community for various libraries

---

**⚠️ Important Safety Notice**: This app is designed to assist in emergency situations but should not be considered a replacement for official emergency services. Always call local emergency numbers (911, 112, etc.) for immediate life-threatening emergencies.

**Built with ❤️ for women's safety and community protection**