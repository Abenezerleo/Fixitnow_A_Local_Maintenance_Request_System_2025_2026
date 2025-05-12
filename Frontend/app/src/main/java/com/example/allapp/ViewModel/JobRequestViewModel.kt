package com.example.allapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.allapp.Model.CreateRequestDto
import com.example.allapp.Model.Request
import com.example.allapp.Repository.RequestRepository
import com.example.allapp.Network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobRequestViewModel(application: Application) : AndroidViewModel(application) {
    private val requestRepository = RequestRepository(application.applicationContext)

    private val _unassignedRequests = MutableStateFlow<List<Request>>(emptyList())
    val unassignedRequests: StateFlow<List<Request>> = _unassignedRequests

    private val _providerRequests = MutableStateFlow<List<Request>>(emptyList())
    val providerRequests: StateFlow<List<Request>> = _providerRequests

    private val _providerAcceptedRequests = MutableStateFlow<List<Request>>(emptyList())
    val providerAcceptedRequests: StateFlow<List<Request>> = _providerAcceptedRequests

    private val _providerCompletedRequests = MutableStateFlow<List<Request>>(emptyList())
    val providerCompletedRequests: StateFlow<List<Request>> = _providerCompletedRequests

    // Provider statistics
    private val _providerCompletedCount = MutableStateFlow(0)
    val providerCompletedCount: StateFlow<Int> = _providerCompletedCount

    private val _providerAverageRating = MutableStateFlow(0.0)
    val providerAverageRating: StateFlow<Double> = _providerAverageRating

    private val _providerTotalBudget = MutableStateFlow(0.0)
    val providerTotalBudget: StateFlow<Double> = _providerTotalBudget

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchRequests()
    }

    fun fetchRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Fetch unassigned requests
                when (val result = requestRepository.getUnassignedRequests()) {
                    is Result.Success -> {
                        _unassignedRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch unassigned requests: ${result.exception.message}"
                    }
                }

                // Fetch all provider requests
                when (val result = requestRepository.getProviderRequests()) {
                    is Result.Success -> {
                        _providerRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch provider requests: ${result.exception.message}"
                    }
                }

                // Fetch provider's accepted requests
                when (val result = requestRepository.getProviderAcceptedRequests()) {
                    is Result.Success -> {
                        _providerAcceptedRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch accepted requests: ${result.exception.message}"
                    }
                }

                // Fetch provider's completed requests
                when (val result = requestRepository.getProviderCompletedRequests()) {
                    is Result.Success -> {
                        _providerCompletedRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch completed requests: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProviderRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.getProviderRequests()) {
                    is Result.Success -> {
                        _providerRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch provider requests: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProviderAcceptedRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.getProviderAcceptedRequests()) {
                    is Result.Success -> {
                        _providerAcceptedRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch accepted requests: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProviderCompletedRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.getProviderCompletedRequests()) {
                    is Result.Success -> {
                        _providerCompletedRequests.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch completed requests: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptRequest(requestId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.acceptRequest(requestId)) {
                    is Result.Success -> {
                        // Refresh the requests after accepting
                        fetchRequests()
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to accept request: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProviderStats() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Fetch completed count
                when (val result = requestRepository.getProviderCompletedCount()) {
                    is Result.Success -> {
                        _providerCompletedCount.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch completed count: ${result.exception.message}"
                        return@launch
                    }
                }

                // Fetch average rating
                when (val result = requestRepository.getProviderAverageRating()) {
                    is Result.Success -> {
                        _providerAverageRating.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch average rating: ${result.exception.message}"
                        return@launch
                    }
                }

                // Fetch total budget
                when (val result = requestRepository.getProviderTotalBudget()) {
                    is Result.Success -> {
                        _providerTotalBudget.value = result.data
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch total budget: ${result.exception.message}"
                        return@launch
                    }
                }
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createRequest(createRequestDto: CreateRequestDto) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.createRequest(createRequestDto)) {
                    is Result.Success -> {
                        // Refresh the requests after creating
                        fetchRequests()
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to create request: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}