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

class RequesterRequestViewModel(application: Application) : AndroidViewModel(application) {
    private val requestRepository = RequestRepository(application.applicationContext)

    private val _requesterRequests = MutableStateFlow<List<Request>>(emptyList())
    val requesterRequests: StateFlow<List<Request>> = _requesterRequests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchRequesterRequests()
    }

    fun fetchRequesterRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = requestRepository.getRequesterRequests()) {
                    is Result.Success -> {
                        _requesterRequests.value = result.data
                        if (result.data.isEmpty() && !result.data.equals(result.data)) {
                            _error.value = "Some requests were filtered due to authorization issues."
                        }
                    }
                    is Result.Failure -> {
                        _error.value = "Failed to fetch requests: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
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
                        fetchRequesterRequests()
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

    fun cancelRequest(requestId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Check if the requestId exists in the current list
                val requestExists = _requesterRequests.value.any { it.requestId == requestId }
                if (!requestExists) {
                    _error.value = "Request ID $requestId not found in your requests."
                    return@launch
                }
                when (val result = requestRepository.cancelRequest(requestId)) {
                    is Result.Success -> {
                        fetchRequesterRequests() // Refresh the list
                    }

                    is Result.Failure -> {
                        _error.value = "Failed to cancel request: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred while canceling the request"
            } finally {
                _isLoading.value = false
            }
        }
    }
}