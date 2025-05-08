package com.example.myappprovider.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myappprovider.data.entities.Job
import com.example.myappprovider.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MyJobsViewModel(
    private val repository: JobRepository,
    private val providerId: Long
) : ViewModel() {
    private val _activeJobs = MutableStateFlow<List<Job>>(emptyList())
    val activeJobs: StateFlow<List<Job>> = _activeJobs

    private val _completedJobs = MutableStateFlow<List<Job>>(emptyList())
    val completedJobs: StateFlow<List<Job>> = _completedJobs

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            repository.getProviderJobsByStatus(providerId, false)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { jobs ->
                    _activeJobs.value = jobs
                }
        }

        viewModelScope.launch {
            repository.getProviderJobsByStatus(providerId, true)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { jobs ->
                    _completedJobs.value = jobs
                }
        }
    }

    fun completeJob(jobId: Long) {
        viewModelScope.launch {
            try {
                repository.updateJobCompletion(jobId, true)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    class Factory(
        private val repository: JobRepository,
        private val providerId: Long
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyJobsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyJobsViewModel(repository, providerId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 