package com.example.allapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.allapp.Model.Request
import com.example.allapp.Model.RequestStatus
import com.example.allapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequestViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentFilter = "ALL"

    init {
        fetchAllRequests()
    }

    fun fetchAllRequests() {
        currentFilter = "ALL"
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.getAllRequests().fold(
                    onSuccess = { requests ->
                        _requests.value = requests
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to fetch all requests"
                        println("Fetch all requests failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Fetch all requests exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCompletedRequests() {
        currentFilter = "Completed"
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.getCompletedRequests().fold(
                    onSuccess = { requests ->
                        _requests.value = requests
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to fetch completed requests"
                        println("Fetch completed requests failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Fetch completed requests exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRejectedRequests() {
        currentFilter = "Rejected"
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.getRejectedRequests().fold(
                    onSuccess = { requests ->
                        _requests.value = requests
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to fetch rejected requests"
                        println("Fetch rejected requests failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Fetch rejected requests exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRequestStatus(requestId: Int, status: RequestStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.updateRequestStatus(requestId, status).fold(
                    onSuccess = {
                        // Refresh based on the current filter
                        when (currentFilter) {
                            "Completed" -> fetchCompletedRequests()
                            "Rejected" -> fetchRejectedRequests()
                            else -> fetchAllRequests()
                        }
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to update request status"
                        println("Update request status failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Update request status exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class RequestViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RequestViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
