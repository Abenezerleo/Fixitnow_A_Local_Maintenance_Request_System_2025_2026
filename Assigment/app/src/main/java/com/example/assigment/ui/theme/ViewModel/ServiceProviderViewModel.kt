package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.Dao.AccountDao
import com.example.assigment.ui.theme.Entity.Account
import com.example.assigment.ui.theme.Enum.RoleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ServiceProviderViewModel(application: Application) : AndroidViewModel(application) {
    private val accountDao: AccountDao
    private val _currentProvider = MutableStateFlow<Account?>(null)
    val currentProvider: StateFlow<Account?> = _currentProvider.asStateFlow()

    private val _providers = MutableStateFlow<List<Account>>(emptyList())
    val providers: StateFlow<List<Account>> = _providers.asStateFlow()

    init {
            val database = AppDatabase.getDatabase(application)
        accountDao = database.accountDao()
        
        viewModelScope.launch {
            accountDao.getAccountsByRole(RoleType.PROVIDER).collect { providers ->
                _providers.value = providers
            }
        }
    }

    fun addProvider(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: String
    ) {
        viewModelScope.launch {
            val newProvider = Account(
                accountId = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                gender = gender,
                role = RoleType.PROVIDER,
                rating = 0f,
                totalJobsCompleted = 0
            )
            accountDao.insertAccount(newProvider)
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

    fun deleteProvider(accountId: String) {
        viewModelScope.launch {
            val provider = accountDao.getAccountById(accountId)
            provider?.let {
                if (it.role == RoleType.PROVIDER) {
                    accountDao.deleteAccount(it)
                }
            }
        }
    }

    fun getProviderById(accountId: String) {
        viewModelScope.launch {
            val provider = accountDao.getAccountById(accountId)
            if (provider?.role == RoleType.PROVIDER) {
                _currentProvider.value = provider
            }
            }
    }

    fun updateProviderRating(accountId: String, rating: Float) {
        viewModelScope.launch {
            val provider = accountDao.getAccountById(accountId)
            provider?.let {
                if (it.role == RoleType.PROVIDER) {
                    val updatedProvider = it.copy(rating = rating)
                    accountDao.updateAccount(updatedProvider)
                }
            }
            }
    }

    fun incrementJobsCompleted(accountId: String) {
        viewModelScope.launch {
            val provider = accountDao.getAccountById(accountId)
            provider?.let {
                if (it.role == RoleType.PROVIDER) {
                    val updatedProvider = it.copy(totalJobsCompleted = it.totalJobsCompleted + 1)
                    accountDao.updateAccount(updatedProvider)
                }
            }
            }
    }
} 