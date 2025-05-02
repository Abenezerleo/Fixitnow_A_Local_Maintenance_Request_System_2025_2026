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

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val accountDao: AccountDao
    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount: StateFlow<Account?> = _currentAccount.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        accountDao = database.accountDao()
        
        viewModelScope.launch {
            accountDao.getAccountsByRole(RoleType.REQUESTER).collect { accounts ->
                _accounts.value = accounts
            }
        }
    }

    fun addUser(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: String
    ) {
        viewModelScope.launch {
            val newAccount = Account(
                accountId = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                gender = gender,
                role = RoleType.REQUESTER, // Automatically set as requester
                rating = 0f,
                totalJobsCompleted = 0
            )
            accountDao.insertAccount(newAccount)
        }
    }

    fun updateUser(
        accountId: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        gender: String
    ) {
        viewModelScope.launch {
            val existingAccount = accountDao.getAccountById(accountId)
            existingAccount?.let { account ->
                if (account.role == RoleType.REQUESTER) { // Only update if role is requester
                    val updatedAccount = account.copy(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender
                    )
                    accountDao.updateAccount(updatedAccount)
                }
            }
        }
    }

    fun deleteUser(accountId: String) {
        viewModelScope.launch {
            val account = accountDao.getAccountById(accountId)
            account?.let {
                if (it.role == RoleType.REQUESTER) { // Only delete if role is requester
                    accountDao.deleteAccount(it)
                }
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