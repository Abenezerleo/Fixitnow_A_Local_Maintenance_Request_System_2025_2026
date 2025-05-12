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

class ProviderViewModel(private val repository: UserRepository) : ViewModel() {
    private val _providers = MutableStateFlow<List<UserProfile>>(emptyList())
    val providers: StateFlow<List<UserProfile>> = _providers

    private val _searchResults = MutableStateFlow<List<UserProfile>>(emptyList())
    val searchResults: StateFlow<List<UserProfile>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadProviders()
    }

    private fun loadProviders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllUsers().onSuccess { users ->
                    _providers.value = users.filter { it.role == "PROVIDER" }
                    _searchResults.value = _providers.value
                }.onFailure { e ->
                    _error.value = e.message ?: "Failed to load providers"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchProviders(query: String) {
        if (query.isBlank()) {
            _searchResults.value = _providers.value
        } else {
            _searchResults.value = _providers.value.filter { provider ->
                provider.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun createProvider(userProfile: UserProfile) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createUser(userProfile).onSuccess { newProvider ->
                    _providers.value = _providers.value + newProvider
                    _searchResults.value = _providers.value
                }.onFailure { e ->
                    _error.value = e.message ?: "Failed to create provider"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProvider(id: String, userUpdate: UserUpdate) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateUser(id, userUpdate).onSuccess { updatedProvider ->
                    _providers.value = _providers.value.map { 
                        if (it.id == id) updatedProvider else it 
                    }
                    _searchResults.value = _providers.value
                }.onFailure { e ->
                    _error.value = e.message ?: "Failed to update provider"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProvider(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteUser(id).onSuccess {
                    _providers.value = _providers.value.filter { it.id != id }
                    _searchResults.value = _providers.value
                }.onFailure { e ->
                    _error.value = e.message ?: "Failed to delete provider"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class ProviderViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProviderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProviderViewModel(UserRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 