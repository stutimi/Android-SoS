package com.xenonesis.womensafety.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import com.xenonesis.womensafety.data.dao.SosEventDao
import com.xenonesis.womensafety.data.model.Contact
import com.xenonesis.womensafety.data.model.LocationData
import com.xenonesis.womensafety.data.model.SosEvent
import com.xenonesis.womensafety.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SosRepository(
    private val sosEventDao: SosEventDao,
    private val contactRepository: ContactRepository,
    private val locationRepository: LocationRepository
) {
    
    fun getAllSosEvents(): LiveData<List<SosEvent>> = sosEventDao.getAllSosEvents()
    
    fun getActiveSosEvents(): LiveData<List<SosEvent>> = sosEventDao.getActiveSosEvents()
    
    suspend fun triggerSos(
        context: Context?,
        type: String,
        location: LocationData? = null
    ): Long = withContext(Dispatchers.IO) {
        
        val currentLocation = location ?: locationRepository.getCurrentLocation()
        
        if (currentLocation == null) {
            throw Exception("Unable to get current location")
        }
        
        // Create SOS event
        val sosEvent = SosEvent(
            type = type,
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude,
            address = currentLocation.address,
            timestamp = System.currentTimeMillis()
        )
        
        val eventId = sosEventDao.insertSosEvent(sosEvent)
        
        // Get emergency contacts
        val contacts = contactRepository.getAllContacts().value ?: emptyList()
        val primaryContacts = contacts.filter { it.isPrimary }
        val allContacts = if (primaryContacts.isNotEmpty()) primaryContacts else contacts.take(3)
        
        // Send SMS to contacts
        val notifiedContacts = mutableListOf<String>()
        allContacts.forEach { contact ->
            try {
                sendEmergencySms(contact, currentLocation)
                notifiedContacts.add(contact.id.toString())
            } catch (e: Exception) {
                // Log error but continue with other contacts
            }
        }
        
        // Update SOS event with notified contacts
        val updatedEvent = sosEvent.copy(
            id = eventId,
            contactsNotified = notifiedContacts
        )
        sosEventDao.updateSosEvent(updatedEvent)
        
        eventId
    }
    
    suspend fun cancelSos(eventId: Long) = withContext(Dispatchers.IO) {
        val event = sosEventDao.getSosEventById(eventId)
        event?.let {
            val updatedEvent = it.copy(
                isResolved = true,
                resolvedAt = System.currentTimeMillis(),
                notes = "Cancelled by user"
            )
            sosEventDao.updateSosEvent(updatedEvent)
        }
    }
    
    suspend fun resolveSos(eventId: Long, notes: String? = null) = withContext(Dispatchers.IO) {
        val event = sosEventDao.getSosEventById(eventId)
        event?.let {
            val updatedEvent = it.copy(
                isResolved = true,
                resolvedAt = System.currentTimeMillis(),
                notes = notes
            )
            sosEventDao.updateSosEvent(updatedEvent)
        }
    }
    
    private suspend fun sendEmergencySms(contact: Contact, location: LocationData) {
        val message = createEmergencyMessage(location)
        val smsManager = SmsManager.getDefault()
        
        try {
            // For long messages, divide into parts
            val parts = smsManager.divideMessage(message)
            if (parts.size == 1) {
                smsManager.sendTextMessage(contact.phoneNumber, null, message, null, null)
            } else {
                smsManager.sendMultipartTextMessage(contact.phoneNumber, null, parts, null, null)
            }
        } catch (e: Exception) {
            throw Exception("Failed to send SMS to ${contact.name}: ${e.message}")
        }
    }
    
    private fun createEmergencyMessage(location: LocationData): String {
        val mapsUrl = locationRepository.getGoogleMapsUrl(location.latitude, location.longitude)
        val address = location.address ?: "Unknown location"
        
        return "🚨 EMERGENCY ALERT 🚨\n\n" +
               "I need immediate help!\n\n" +
               "Location: $address\n" +
               "Maps: $mapsUrl\n\n" +
               "Please contact me or call emergency services.\n" +
               "Time: ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}"
    }
    
    fun makeEmergencyCall(context: Context, phoneNumber: String = Constants.EMERGENCY_NUMBER_POLICE) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to dial intent
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
    
    suspend fun getSosEventsByDateRange(startTime: Long, endTime: Long): List<SosEvent> = 
        withContext(Dispatchers.IO) {
            sosEventDao.getSosEventsByDateRange(startTime, endTime)
        }
    
    suspend fun getSosEventsByType(type: String): List<SosEvent> = 
        withContext(Dispatchers.IO) {
            sosEventDao.getSosEventsByType(type)
        }
    
    suspend fun getLatestSosEvent(): SosEvent? = 
        withContext(Dispatchers.IO) {
            sosEventDao.getLatestSosEvent()
        }
    
    suspend fun getSosEventCountSince(startTime: Long): Int = 
        withContext(Dispatchers.IO) {
            sosEventDao.getSosEventCountSince(startTime)
        }
    
    suspend fun cleanupOldSosEvents(daysToKeep: Int = 30) = 
        withContext(Dispatchers.IO) {
            val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
            sosEventDao.deleteOldSosEvents(cutoffTime)
        }
}