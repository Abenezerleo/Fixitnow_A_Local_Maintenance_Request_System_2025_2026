package com.example.myappprovider.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myappprovider.data.entities.Provider
import com.example.myappprovider.data.repository.ProviderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProviderRepository,
    private val providerId: Long
) : ViewModel() {
    private val _provider = MutableStateFlow<Provider?>(null)
    val provider: StateFlow<Provider?> = _provider

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadProvider()
    }

    private fun loadProvider() {
        viewModelScope.launch {
            repository.getProvider(providerId)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { provider ->
                    _provider.value = provider
                }
        }
    }

    fun updateProfile(
        fullName: String,
        phone: String,
        email: String,
        dob: String,
        jobTitle: String
    ) {
        viewModelScope.launch {
            try {
                repository.updateProviderProfile(providerId, fullName, phone, email, dob, jobTitle)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updatePaymentInfo(
        cbe: String,
        teleBirr: String,
        awashBank: String,
        paypal: String
    ) {
        viewModelScope.launch {
            try {
                repository.updateProviderPaymentInfo(providerId, cbe, teleBirr, awashBank, paypal)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateStatus(status: String) {
        viewModelScope.launch {
            try {
                repository.updateProviderStatus(providerId, status)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    class Factory(
        private val repository: ProviderRepository,
        private val providerId: Long
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository, providerId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 