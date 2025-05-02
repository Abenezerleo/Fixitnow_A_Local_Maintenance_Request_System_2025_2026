package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Dao.AccountDao
import com.example.assigment.ui.theme.Dao.RequestDao
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.Entity.Account
import com.example.assigment.ui.theme.Enum.RoleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val accountDao: AccountDao
    private val requestDao: RequestDao
    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _requesters = MutableStateFlow<List<Account>>(emptyList())
    val requesters: StateFlow<List<Account>> = _requesters.asStateFlow()

    private val _providers = MutableStateFlow<List<Account>>(emptyList())
    val providers: StateFlow<List<Account>> = _providers.asStateFlow()

    // Count flows
    val totalRequesterCount: Flow<Int>
    val totalProviderCount: Flow<Int>
    val availableProviderCount: Flow<Int>
    val totalRequestCount: Flow<Int>
    val completedRequestCount: Flow<Int>
    val pendingRequestCount: Flow<Int>
    val rejectedRequestCount: Flow<Int>

    init {
        val database = AppDatabase.getDatabase(application)
        accountDao = database.accountDao()
        requestDao = database.requestDao()

        // Initialize count flows with logging
        totalRequestCount = requestDao.getTotalRequestCount()
            .onEach { count -> Log.d("DashboardViewModel", "Total requests: $count") }
        
        completedRequestCount = requestDao.getCompletedRequestCount()
            .onEach { count -> Log.d("DashboardViewModel", "Completed requests: $count") }
        
        pendingRequestCount = requestDao.getPendingRequestCount()
            .onEach { count -> Log.d("DashboardViewModel", "Pending requests: $count") }
        
        rejectedRequestCount = requestDao.getRejectedRequestCount()
            .onEach { count -> Log.d("DashboardViewModel", "Rejected requests: $count") }

        totalRequesterCount = accountDao.getAccountsByRole(RoleType.REQUESTER).map { it.size }
            .onEach { count -> Log.d("DashboardViewModel", "Total requesters: $count") }
        
        totalProviderCount = accountDao.getAccountsByRole(RoleType.PROVIDER).map { it.size }
            .onEach { count -> Log.d("DashboardViewModel", "Total providers: $count") }
        
        availableProviderCount = accountDao.getAccountsByRole(RoleType.PROVIDER).map { 
            it.count { provider -> provider.rating >= 4.0f }
        }.onEach { count -> Log.d("DashboardViewModel", "Available providers: $count") }

        // Initialize account lists
        viewModelScope.launch {
            accountDao.getAccountsByRole(RoleType.REQUESTER).collect { requesters ->
                _requesters.value = requesters
            }
        }

        viewModelScope.launch {
            accountDao.getAccountsByRole(RoleType.PROVIDER).collect { providers ->
                _providers.value = providers
            }
        }
    }

    fun getRequesterById(accountId: String) {
        viewModelScope.launch {
            val requester = accountDao.getAccountById(accountId)
            if (requester?.role == RoleType.REQUESTER) {
                _currentAccount.value = requester
            }
        }
    }

    fun getProviderById(accountId: String) {
        viewModelScope.launch {
            val provider = accountDao.getAccountById(accountId)
            if (provider?.role == RoleType.PROVIDER) {
                _currentAccount.value = provider
            }
        }
    }

    fun updateRequester(
        accountId: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: String
    ) {
        viewModelScope.launch {
            val existingRequester = accountDao.getAccountById(accountId)
            existingRequester?.let { requester ->
                if (requester.role == RoleType.REQUESTER) {
                    val updatedRequester = requester.copy(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender
                    )
                    accountDao.updateAccount(updatedRequester)
                }
            }
        }
    }

    fun updateProvider(
        accountId: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: String
    ) {
        viewModelScope.launch {
            val existingProvider = accountDao.getAccountById(accountId)
            existingProvider?.let { provider ->
                if (provider.role == RoleType.PROVIDER) {
                    val updatedProvider = provider.copy(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender,
                    )
                    accountDao.updateAccount(updatedProvider)
                }
            }
        }
    }

    fun deleteAccount(accountId: String) {
        viewModelScope.launch {
            val account = accountDao.getAccountById(accountId)
            account?.let {
                accountDao.deleteAccount(it)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val account = accountDao.login(email, password)
            _currentAccount.value = account
        }
    }

    fun logout() {
        _currentAccount.value = null
    }
} 