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

class PerformanceViewModel(
    private val repository: ProviderRepository,
    private val providerId: Long
) : ViewModel() {
    private val _provider = MutableStateFlow<Provider?>(null)
    val provider: StateFlow<Provider?> = _provider

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadProviderStats()
    }

    private fun loadProviderStats() {
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

    fun updateStats(rating: Float, totalJobs: Int, completedJobs: Int) {
        viewModelScope.launch {
            try {
                repository.updateProviderStats(providerId, rating, totalJobs, completedJobs)
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
            if (modelClass.isAssignableFrom(PerformanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PerformanceViewModel(repository, providerId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 