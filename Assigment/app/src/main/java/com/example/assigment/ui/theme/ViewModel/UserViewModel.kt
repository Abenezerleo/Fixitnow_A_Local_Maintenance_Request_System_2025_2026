package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.Dao.UserDao
import com.example.assigment.ui.theme.Entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao
    val allUsers: Flow<List<User>>

    init {
        val database = AppDatabase.getDatabase(application)
        userDao = database.userDao()
        allUsers = userDao.getAllUsers()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.editUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteUser(user)
        }
    }

    fun getUserById(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }

    fun getUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }
    }
} 