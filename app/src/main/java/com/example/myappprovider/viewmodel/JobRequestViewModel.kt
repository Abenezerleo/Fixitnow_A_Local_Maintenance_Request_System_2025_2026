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

class JobRequestViewModel(
    private val repository: JobRepository,
    private val providerId: Long
) : ViewModel() {
    private val _availableJobs = MutableStateFlow<List<Job>>(emptyList())
    val availableJobs: StateFlow<List<Job>> = _availableJobs

    private val _assignedJobs = MutableStateFlow<List<Job>>(emptyList())
    val assignedJobs: StateFlow<List<Job>> = _assignedJobs

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            repository.getAvailableJobs()
                .catch { e ->
                    _error.value = e.message
                }
                .collect { jobs ->
                    _availableJobs.value = jobs.filter { it.status == "AVAILABLE" }
                }
        }

        viewModelScope.launch {
            repository.getProviderJobs(providerId)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { jobs ->
                    _assignedJobs.value = jobs.filter { it.status == "ASSIGNED" || it.status == "COMPLETED" }
                }
        }
    }

    fun acceptJob(jobId: Long) {
        viewModelScope.launch {
            try {
                // Find the job in available jobs
                val jobToMove = _availableJobs.value.find { it.id == jobId }
                jobToMove?.let { job ->
                    // Update job status in database
                    repository.updateJobStatus(jobId, "ASSIGNED", providerId)
                    
                    // Remove from available jobs immediately
                    _availableJobs.value = _availableJobs.value.filter { it.id != jobId }
                    
                    // Add to assigned jobs immediately with updated status
                    val updatedJob = job.copy(
                        status = "ASSIGNED",
                        providerId = providerId,
                        isCompleted = false
                    )
                    _assignedJobs.value = _assignedJobs.value + updatedJob
                }
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
            if (modelClass.isAssignableFrom(JobRequestViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobRequestViewModel(repository, providerId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 