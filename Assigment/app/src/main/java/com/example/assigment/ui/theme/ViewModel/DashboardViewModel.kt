package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Dao.RequestDao
import com.example.assigment.ui.theme.Dao.ServiceProviderDao
import com.example.assigment.ui.theme.Dao.UserDao
import com.example.assigment.ui.theme.Database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao
    private val serviceProviderDao: ServiceProviderDao
    private val requestDao: RequestDao

    // Count flows
    val totalUserCount: Flow<Int>
    val activeUserCount: Flow<Int>
    val availableProviderCount: Flow<Int>
    val totalRequestCount: Flow<Int>
    val completedRequestCount: Flow<Int>
    val pendingRequestCount: Flow<Int>
    val rejectedRequestCount: Flow<Int>

    init {
        val database = AppDatabase.getDatabase(application)
        userDao = database.userDao()
        serviceProviderDao = database.serviceProviderDao()
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

        totalUserCount = userDao.getAllUsers().map { it.size }
            .onEach { count -> Log.d("DashboardViewModel", "Total users: $count") }
        
        activeUserCount = userDao.getAllActiveUsers().map { it.size }
            .onEach { count -> Log.d("DashboardViewModel", "Active users: $count") }
        
        availableProviderCount = serviceProviderDao.countAvailableProviders()
            .onEach { count -> Log.d("DashboardViewModel", "Available providers: $count") }

        // Initialize sample data if needed
        viewModelScope.launch {
            val currentRequests = requestDao.getAllRequests().map { it.size }
                .onEach { count -> Log.d("DashboardViewModel", "Current request count: $count") }
        }
    }
} 