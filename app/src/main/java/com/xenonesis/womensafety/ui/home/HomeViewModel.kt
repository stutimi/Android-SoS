package com.xenonesis.womensafety.ui.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xenonesis.womensafety.data.model.LocationData
import com.xenonesis.womensafety.data.repository.LocationRepository
import com.xenonesis.womensafety.data.repository.SosRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sosRepository: SosRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    
    private val _sosStatus = MutableLiveData<SosStatus>(SosStatus.Idle)
    val sosStatus: LiveData<SosStatus> = _sosStatus
    
    private val _currentLocation = MutableLiveData<LocationData?>()
    val currentLocation: LiveData<LocationData?> = _currentLocation
    
    private var activeSosEventId: String? = null
    
    sealed class SosStatus {
        object Idle : SosStatus()
        data class Countdown(val seconds: Int) : SosStatus()
        object Active : SosStatus()
        data class Error(val message: String) : SosStatus()
    }
    
    init {
        observeLocationUpdates()
    }
    
    private fun observeLocationUpdates() {
        locationRepository.currentLocation.observeForever { location ->
            _currentLocation.value = location
        }
    }
    
    fun startLocationUpdates() {
        locationRepository.startLocationTracking()
    }
    
    fun stopLocationUpdates() {
        locationRepository.stopLocationTracking()
    }
    
    fun updateSosCountdown(seconds: Int) {
        _sosStatus.value = SosStatus.Countdown(seconds)
    }
    
    fun triggerSos(type: String) {
        viewModelScope.launch {
            try {
                val eventId = sosRepository.triggerSos(
                    type = type,
                    location = _currentLocation.value
                )
                activeSosEventId = eventId
                _sosStatus.value = SosStatus.Active
            } catch (e: Exception) {
                _sosStatus.value = SosStatus.Error(e.message ?: "Failed to trigger SOS")
            }
        }
    }
    
    fun cancelActiveSos() {
        viewModelScope.launch {
            activeSosEventId?.let { eventId ->
                try {
                    sosRepository.cancelSos(eventId)
                    activeSosEventId = null
                    _sosStatus.value = SosStatus.Idle
                } catch (e: Exception) {
                    _sosStatus.value = SosStatus.Error(e.message ?: "Failed to cancel SOS")
                }
            }
        }
    }
    
    fun resetSosStatus() {
        _sosStatus.value = SosStatus.Idle
    }
    
    fun setSosError(message: String) {
        _sosStatus.value = SosStatus.Error(message)
    }
    
    fun makeEmergencyCall(context: Context, phoneNumber: String) {
        sosRepository.makeEmergencyCall(context, phoneNumber)
    }
    
    suspend fun shareCurrentLocation(context: Context) {
        val location = _currentLocation.value
        if (location != null) {
            val shareText = locationRepository.getLocationShareText(location)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val chooser = Intent.createChooser(shareIntent, "Share Location")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}