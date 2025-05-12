package com.example.allapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.allapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _totalRequesters = MutableStateFlow(0)
    val totalRequesters: StateFlow<Int> = _totalRequesters

    private val _totalProviders = MutableStateFlow(0)
    val totalProviders: StateFlow<Int> = _totalProviders

    private val _totalCompleted = MutableStateFlow(0)
    val totalCompleted: StateFlow<Int> = _totalCompleted

    private val _totalPending = MutableStateFlow(0)
    val totalPending: StateFlow<Int> = _totalPending

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchDashboardStatistics()
    }

    fun fetchDashboardStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Fetch total requesters
                userRepository.getTotalRequesters().fold(
                    onSuccess = { count ->
                        _totalRequesters.value = count
                    },
                    onFailure = { exception ->
                        _error.value = "Failed to fetch total requesters: ${exception.message}"
                    }
                )

                // Fetch total providers
                userRepository.getTotalProviders().fold(
                    onSuccess = { count ->
                        _totalProviders.value = count
                    },
                    onFailure = { exception ->
                        _error.value = "Failed to fetch total providers: ${exception.message}"
                    }
                )

                // Fetch request statistics
                userRepository.getRequestStatistics().fold(
                    onSuccess = { stats ->
                        _totalCompleted.value = stats.totalCompleted
                        _totalPending.value = stats.totalPending
                    },
                    onFailure = { exception ->
                        _error.value = "Failed to fetch request statistics: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Fetch dashboard statistics exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class DashboardViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 