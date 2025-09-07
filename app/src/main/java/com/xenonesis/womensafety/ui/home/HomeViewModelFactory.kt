package com.xenonesis.womensafety.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xenonesis.womensafety.data.repository.ContactRepository
import com.xenonesis.womensafety.data.repository.LocationRepository
import com.xenonesis.womensafety.data.repository.SosRepository


class HomeViewModelFactory(
    private val sosRepository: SosRepository,
    private val locationRepository: LocationRepository,
    private val contactRepository: ContactRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(sosRepository, locationRepository, contactRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}