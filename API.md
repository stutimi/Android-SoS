# Smart SOS API Documentation 📡

This document provides comprehensive API documentation for the Smart SOS women's safety application. The app primarily uses Firebase services for backend operations, including Firestore for data storage, Firebase Authentication for user management, and Firebase Cloud Messaging for real-time notifications.

## 🏗️ Architecture Overview

The Smart SOS app follows a **Firebase-first architecture** with the following components:

- **Firebase Authentication** - User authentication and session management
- **Cloud Firestore** - NoSQL database for real-time data storage
- **Firebase Cloud Messaging (FCM)** - Push notifications and real-time messaging
- **Firebase Storage** - File storage for evidence and media
- **Google Play Services** - Location services and maps integration

## 🔐 Authentication

### Firebase Authentication

The app uses Firebase Authentication with phone number verification as the primary authentication method.

#### Authentication Flow

```kotlin
// Sign in with phone number
FirebaseAuth.getInstance().signInWithCredential(credential)

// User session management
val currentUser = FirebaseAuth.getInstance().currentUser
```

#### User Registration Data Model

```kotlin
data class FirebaseUser(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String? = null,
    val fcmToken: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Date? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val emergencyContacts: List<String> = emptyList(),
    val allowCommunityAlerts: Boolean = true,
    val shareLocationWithContacts: Boolean = true,
    val autoCallEmergencyServices: Boolean = false
)
```

## 📊 Firestore Database Structure

### Collections Overview

| Collection | Purpose | Document Structure |
|------------|---------|-------------------|
| `users` | User profiles and settings | UserProfile |
| `sos_events` | Emergency events and alerts | SosEvent |
| `community_alerts` | Community safety alerts | CommunityAlert |
| `location_history` | User location tracking | LocationHistory |
| `emergency_contacts` | User emergency contacts | EmergencyContact |
| `safety_tips` | Safety tips and advice | SafetyTip |
| `user_reports` | User reports and feedback | UserReport |
| `fcm_tokens` | FCM token management | FcmToken |
| `app_settings` | Global app configuration | AppSettings |

### Collection Constants

```kotlin
object FirestoreCollections {
    const val USERS = "users"
    const val SOS_EVENTS = "sos_events"
    const val COMMUNITY_ALERTS = "community_alerts"
    const val LOCATION_HISTORY = "location_history"
    const val EMERGENCY_CONTACTS = "emergency_contacts"
    const val SAFETY_TIPS = "safety_tips"
    const val USER_REPORTS = "user_reports"
    const val FCM_TOKENS = "fcm_tokens"
    const val APP_SETTINGS = "app_settings"
}
```

## 👤 User Management API

### User Profile Operations

#### Create User Profile

```kotlin
suspend fun createUser(user: FirebaseUser): Result<String>
```

**Firestore Path**: `/users/{userId}`

**Request Model**:
```kotlin
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImageUrl: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val lastActiveAt: Timestamp = Timestamp.now(),
    val isActive: Boolean = true,
    val emergencyContacts: List<String> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val fcmToken: String? = null,
    val location: UserLocation? = null
)
```

#### Update User Profile

```kotlin
suspend fun updateUser(userId: String, user: FirebaseUser): Result<Unit>
```

#### Get User Profile

```kotlin
suspend fun getUser(userId: String): Result<FirebaseUser?>
```

### User Settings

```kotlin
data class UserSettings(
    val shakeEnabled: Boolean = true,
    val autoCallEnabled: Boolean = true,
    val communityAlertsEnabled: Boolean = true,
    val shakeSensitivity: Float = 2.7f,
    val sosCountdown: Int = 5,
    val shareLocationWithContacts: Boolean = true,
    val allowCommunityAlerts: Boolean = true,
    val silentMode: Boolean = false,
    val emergencyNumber: String = "911"
)
```

## 🚨 SOS Events API

### SOS Event Operations

#### Create SOS Event

```kotlin
suspend fun createSosEvent(sosEvent: FirebaseSosEvent): Result<String>
```

**Firestore Path**: `/sos_events/{eventId}`

**Request Model**:
```kotlin
data class SosEvent(
    val id: String = "",
    val userId: String = "",
    val type: String = "", // "manual", "shake", "panic_button", "route_deviation"
    val status: String = "active", // "active", "resolved", "cancelled", "false_alarm"
    val severity: String = "high", // "low", "medium", "high", "critical"
    val location: SosLocation = SosLocation(),
    val userInfo: SosUserInfo = SosUserInfo(),
    val timestamp: Timestamp = Timestamp.now(),
    val resolvedAt: Timestamp? = null,
    val description: String = "",
    val evidence: List<Evidence> = emptyList(),
    val notificationsSent: List<NotificationRecord> = emptyList(),
    val emergencyServicesCalled: Boolean = false,
    val responseTime: Long? = null
)
```

#### SOS Types

| Type | Description | Trigger Method |
|------|-------------|----------------|
| `manual` | User manually pressed SOS button | UI button press |
| `shake` | Device shake detection | Accelerometer sensor |
| `panic_button` | External panic button | Bluetooth device |
| `route_deviation` | Route deviation detected | GPS tracking |

#### Update SOS Event

```kotlin
suspend fun updateSosEvent(eventId: String, sosEvent: FirebaseSosEvent): Result<Unit>
```

#### Observe SOS Events (Real-time)

```kotlin
fun observeSosEvents(userId: String): Flow<List<FirebaseSosEvent>>
```

### SOS Location Data

```kotlin
data class SosLocation(
    val geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
    val address: String = "",
    val accuracy: Float = 0f,
    val altitude: Double? = null,
    val bearing: Float? = null,
    val speed: Float? = null,
    val timestamp: Timestamp = Timestamp.now()
)
```

## 📍 Location Services API

### Location Sharing

#### Share Location

```kotlin
suspend fun shareLocation(locationShare: FirebaseLocationShare): Result<String>
```

**Request Model**:
```kotlin
data class FirebaseLocationShare(
    val id: String = "",
    val userId: String = "",
    val sharedWithUserId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String? = null,
    val isActive: Boolean = true,
    val expiresAt: Date? = null,
    val tripName: String? = null,
    val estimatedArrival: Date? = null,
    val timestamp: Date? = null
)
```

#### Observe Shared Locations (Real-time)

```kotlin
fun observeSharedLocations(userId: String): Flow<List<FirebaseLocationShare>>
```

### Location History

```kotlin
data class LocationHistory(
    val id: String = "",
    val userId: String = "",
    val location: SosLocation = SosLocation(),
    val activityType: String = "unknown", // "still", "walking", "running", "driving", "cycling"
    val confidence: Float = 0f,
    val sosEventId: String? = null,
    val isShared: Boolean = false,
    val sharedWith: List<String> = emptyList()
)
```

## 👥 Emergency Contacts API

### Emergency Contact Operations

**Firestore Path**: `/users/{userId}/emergency_contacts/{contactId}`

#### Contact Data Model

```kotlin
data class EmergencyContact(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val relationship: String = "", // "family", "friend", "colleague", "neighbor", "other"
    val priority: Int = 1, // 1-5, 1 being highest priority
    val isVerified: Boolean = false,
    val canReceiveSms: Boolean = true,
    val canReceiveCalls: Boolean = true,
    val canReceivePush: Boolean = true,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)
```

#### CRUD Operations

```kotlin
// Create contact
suspend fun addEmergencyContact(contact: EmergencyContact): Result<String>

// Read contacts
suspend fun getEmergencyContacts(userId: String): Result<List<EmergencyContact>>

// Update contact
suspend fun updateEmergencyContact(contactId: String, contact: EmergencyContact): Result<Unit>

// Delete contact
suspend fun deleteEmergencyContact(contactId: String): Result<Unit>
```

## 🏘️ Community Alerts API

### Community Alert Operations

#### Create Community Alert

```kotlin
suspend fun createCommunityAlert(alert: FirebaseCommunityAlert): Result<String>
```

**Request Model**:
```kotlin
data class CommunityAlert(
    val id: String = "",
    val createdBy: String = "",
    val creatorName: String = "",
    val type: String = "", // "safety_concern", "suspicious_activity", "incident", "warning"
    val severity: String = "medium", // "low", "medium", "high", "critical"
    val status: String = "active", // "active", "resolved", "verified", "dismissed"
    val title: String = "",
    val description: String = "",
    val location: SosLocation = SosLocation(),
    val radius: Double = 1000.0, // Alert radius in meters
    val timestamp: Timestamp = Timestamp.now(),
    val resolvedAt: Timestamp? = null,
    val verifiedBy: List<String> = emptyList(),
    val reportedBy: List<String> = emptyList(),
    val evidence: List<Evidence> = emptyList(),
    val tags: List<String> = emptyList(),
    val isAnonymous: Boolean = false
)
```

#### Alert Types

| Type | Description | Severity Levels |
|------|-------------|----------------|
| `safety_concern` | General safety concerns | low, medium, high |
| `suspicious_activity` | Suspicious behavior or activities | medium, high |
| `incident` | Actual incidents or emergencies | high, critical |
| `warning` | Preventive warnings | low, medium |

#### Observe Community Alerts (Real-time)

```kotlin
fun observeCommunityAlerts(
    latitude: Double, 
    longitude: Double, 
    radiusKm: Double
): Flow<List<FirebaseCommunityAlert>>
```

## 📱 Push Notifications API

### Firebase Cloud Messaging

#### FCM Token Management

```kotlin
data class FcmToken(
    val token: String = "",
    val userId: String = "",
    val deviceId: String = "",
    val platform: String = "android",
    val appVersion: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val lastUsed: Timestamp = Timestamp.now(),
    val isActive: Boolean = true
)
```

#### Update FCM Token

```kotlin
suspend fun updateFcmToken(userId: String, token: String): Result<Unit>
```

### Notification Types

| Type | Purpose | Data Payload |
|------|---------|--------------|
| `sos_alert` | Emergency SOS notifications | `{type, event_id, user_id, location}` |
| `community_alert` | Community safety alerts | `{type, alert_id, severity, location}` |
| `location_share` | Location sharing notifications | `{type, share_id, user_id}` |
| `safety_check` | Safety check requests | `{type, request_id, from_user}` |

### Notification Data Structure

```kotlin
data class NotificationRecord(
    val contactId: String = "",
    val contactName: String = "",
    val contactPhone: String = "",
    val method: String = "", // "sms", "call", "push", "email"
    val status: String = "", // "sent", "delivered", "failed", "read"
    val sentAt: Timestamp = Timestamp.now(),
    val deliveredAt: Timestamp? = null,
    val readAt: Timestamp? = null,
    val errorMessage: String? = null
)
```

## 📄 Evidence and Media API

### Evidence Data Model

```kotlin
data class Evidence(
    val id: String = "",
    val type: String = "", // "photo", "audio", "video", "text"
    val url: String = "",
    val filename: String = "",
    val size: Long = 0,
    val mimeType: String = "",
    val uploadedAt: Timestamp = Timestamp.now(),
    val description: String = ""
)
```

### Supported Media Types

| Type | MIME Types | Max Size | Storage Location |
|------|------------|----------|------------------|
| `photo` | image/jpeg, image/png | 10MB | Firebase Storage |
| `audio` | audio/mp3, audio/wav | 25MB | Firebase Storage |
| `video` | video/mp4, video/mov | 100MB | Firebase Storage |
| `text` | text/plain | 10KB | Firestore |

## ⚙️ App Configuration API

### Global App Settings

```kotlin
data class AppSettings(
    val id: String = "",
    val emergencyNumbers: Map<String, String> = mapOf(
        "police" to "911",
        "fire" to "911",
        "medical" to "911"
    ),
    val sosCountdownDefault: Int = 5,
    val maxEmergencyContacts: Int = 10,
    val communityAlertRadius: Double = 5000.0,
    val maintenanceMode: Boolean = false,
    val minAppVersion: String = "1.0.0",
    val supportEmail: String = "support@womensafety.com",
    val privacyPolicyUrl: String = "",
    val termsOfServiceUrl: String = "",
    val lastUpdated: Timestamp = Timestamp.now()
)
```

## 🔧 Constants and Configuration

### Application Constants

```kotlin
object Constants {
    // Location Settings
    const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
    const val LOCATION_FASTEST_INTERVAL = 5000L // 5 seconds
    const val LOCATION_DISPLACEMENT = 10f // 10 meters
    
    // Shake Detection
    const val SHAKE_THRESHOLD_GRAVITY = 2.7f
    const val SHAKE_REQUIRED_COUNT = 3
    const val SHAKE_COUNT_RESET_TIME_MS = 3000
    
    // SOS Configuration
    const val SOS_COUNTDOWN_SECONDS = 5
    const val SOS_AUTO_CALL_DELAY = 10000L // 10 seconds
    
    // Route Deviation
    const val ROUTE_DEVIATION_THRESHOLD = 500f // 500 meters
    
    // Network
    const val NETWORK_TIMEOUT = 30000L // 30 seconds
    const val RETRY_ATTEMPTS = 3
}
```

### SOS Types

```kotlin
object SosTypes {
    const val MANUAL = "manual"
    const val SHAKE = "shake"
    const val PANIC_BUTTON = "panic_button"
    const val ROUTE_DEVIATION = "route_deviation"
}
```

## 🔄 Real-time Data Flow

### SOS Event Flow

1. **Trigger Detection** → Shake/Button/Manual
2. **Location Acquisition** → GPS coordinates + address
3. **Event Creation** → Firestore document creation
4. **Contact Notification** → SMS/Call/Push notifications
5. **Real-time Updates** → Live status updates to contacts
6. **Event Resolution** → Manual or automatic resolution

### Location Sharing Flow

1. **Permission Grant** → Location access permission
2. **Tracking Start** → Background location service
3. **Data Collection** → Continuous location updates
4. **Real-time Sync** → Firestore real-time updates
5. **Contact Updates** → Live location sharing
6. **Tracking Stop** → Service termination

## 🛡️ Security and Privacy

### Data Protection

- **Encryption**: All data transmitted using HTTPS/TLS
- **Authentication**: Firebase Authentication with phone verification
- **Authorization**: Firestore security rules for data access control
- **Privacy**: Location data only shared during active sessions
- **Anonymization**: Option for anonymous community alerts

### Firestore Security Rules Example

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // SOS events are readable by emergency contacts
    match /sos_events/{eventId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
        request.auth.uid == resource.data.userId;
    }
    
    // Community alerts are publicly readable but restricted write
    match /community_alerts/{alertId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
        request.auth.uid == resource.data.createdBy;
    }
  }
}
```

## 📊 Error Handling

### Result Wrapper

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data class Loading(val isLoading: Boolean = true) : Result<Nothing>()
}
```

### Common Error Codes

| Error Code | Description | Resolution |
|------------|-------------|------------|
| `PERMISSION_DENIED` | Insufficient permissions | Request required permissions |
| `NETWORK_ERROR` | Network connectivity issues | Retry with exponential backoff |
| `LOCATION_UNAVAILABLE` | GPS location not available | Use last known location |
| `AUTHENTICATION_FAILED` | User authentication failed | Re-authenticate user |
| `QUOTA_EXCEEDED` | API quota exceeded | Implement rate limiting |

## 🧪 Testing

### Firebase Test Suite

The app includes a comprehensive Firebase test suite for API validation:

```kotlin
class FirebaseTestSuite {
    suspend fun testUserOperations()
    suspend fun testSosEventOperations()
    suspend fun testCommunityAlertOperations()
    suspend fun testLocationSharingOperations()
    suspend fun testNotificationOperations()
}
```

### Test Data Models

```kotlin
data class TestResult(
    val testName: String,
    val success: Boolean,
    val duration: Long,
    val errorMessage: String? = null,
    val timestamp: Timestamp = Timestamp.now()
)
```

## 📈 Rate Limiting and Quotas

### Firestore Limits

| Operation | Limit | Notes |
|-----------|-------|-------|
| Document writes | 1 per second per document | Sustained writes |
| Document reads | No limit | Best effort |
| Collection queries | 1 per second | Complex queries |
| Real-time listeners | 100 per client | Concurrent listeners |

### FCM Limits

| Operation | Limit | Notes |
|-----------|-------|-------|
| Message rate | 600,000 per minute | Per project |
| Topic subscribers | 2,000,000 | Per topic |
| Payload size | 4KB | Message payload |

## 🔗 External Integrations

### Google Play Services

- **Location Services**: GPS and network location
- **Maps API**: Map display and geocoding
- **Activity Recognition**: Movement detection

### Third-party Services

- **SMS Gateway**: Emergency SMS notifications
- **Voice Calling**: Emergency voice calls
- **Geocoding**: Address resolution

---

## 📞 Support and Documentation

For additional API support and documentation:

- **Firebase Console**: [https://console.firebase.google.com](https://console.firebase.google.com)
- **Firestore Documentation**: [https://firebase.google.com/docs/firestore](https://firebase.google.com/docs/firestore)
- **FCM Documentation**: [https://firebase.google.com/docs/cloud-messaging](https://firebase.google.com/docs/cloud-messaging)

**⚠️ Important**: This API documentation reflects the current implementation. Always refer to the latest code for the most up-to-date API specifications.

---

**Built with Firebase 🔥 for real-time safety and security**