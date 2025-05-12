
package com.example.allapp.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.allapp.Model.UserProfile
import com.example.allapp.Model.UserUpdate
import com.example.allapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val context: Context) : ViewModel() {
    data class UserWithReport(
        val user: UserProfile,
        val isReported: Boolean = false
    )

    private val userRepository = UserRepository()

    private val _requesters = MutableStateFlow<List<UserWithReport>>(emptyList())
    val requesters: StateFlow<List<UserWithReport>> = _requesters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchResults = MutableStateFlow<List<UserWithReport>>(emptyList())
    val searchResults: StateFlow<List<UserWithReport>> = _searchResults

    init {
        fetchRequesters()
    }

    fun fetchRequesters() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.getRequesters().fold(
                    onSuccess = { requesters ->
                        val usersWithReport = requesters.map { UserWithReport(it) }
                        _requesters.value = usersWithReport
                        _searchResults.value = usersWithReport
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to fetch requesters"
                        println("Fetch requesters failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Fetch requesters exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createRequester(userProfile: UserProfile) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.createUser(userProfile).fold(
                    onSuccess = {
                        fetchRequesters()
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to create requester"
                        println("Create requester failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Create requester exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRequester(id: String, userUpdate: UserUpdate) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.updateUser(id, userUpdate).fold(
                    onSuccess = {
                        fetchRequesters()
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to update requester"
                        println("Update requester failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Update requester exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteRequester(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.deleteUser(id).fold(
                    onSuccess = {
                        fetchRequesters()
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to delete requester"
                        println("Delete requester failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Delete requester exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchRequesters(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.searchRequesterByName(name).fold(
                    onSuccess = { results ->
                        val usersWithReport = results.map { UserWithReport(it) }
                        _searchResults.value = usersWithReport
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to search requesters"
                        println("Search requesters failed: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                println("Search requesters exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleReported(userId: String) {
        val currentResults = _searchResults.value.toMutableList()
        val index = currentResults.indexOfFirst { it.user.id == userId }
        if (index != -1) {
            val userWithReport = currentResults[index]
            currentResults[index] = userWithReport.copy(isReported = !userWithReport.isReported)
            _searchResults.value = currentResults
        }
    }
}

class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
