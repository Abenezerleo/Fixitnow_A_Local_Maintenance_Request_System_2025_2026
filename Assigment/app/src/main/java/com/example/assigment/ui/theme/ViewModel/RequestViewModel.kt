package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.Dao.RequestDao
import com.example.assigment.ui.theme.Entity.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RequestViewModel(application: Application) : AndroidViewModel(application) {
    private val requestDao: RequestDao
    val allRequests: Flow<List<Request>>
    
    // Count flows
    val totalRequestCount: Flow<Int>
    val completedRequestCount: Flow<Int>
    val rejectedRequestCount: Flow<Int>
    val pendingRequestCount: Flow<Int>

    init {
        val database = AppDatabase.getDatabase(application)
        requestDao = database.requestDao()
        allRequests = requestDao.getAllRequests()
        
        // Initialize count flows
        totalRequestCount = requestDao.getTotalRequestCount()
        completedRequestCount = requestDao.getCompletedRequestCount()
        rejectedRequestCount = requestDao.getRejectedRequestCount()
        pendingRequestCount = requestDao.getPendingRequestCount()
        
        // Initialize sample data and monitor counts
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // First, clear any existing data
                val currentRequests = requestDao.getAllRequests().first()
                currentRequests.forEach { request ->
                    requestDao.deleteRequest(request)
                }
                Log.d("RequestViewModel", "Cleared existing data")

                // Insert sample data
                val sampleRequests = listOf(
                    // Pending requests
                    Request("REQ-001", "🔧", "Plumbing", "John Doe", "10:00 AM", "2025-04-22", false, false, false),
                    Request("REQ-002", "🔨", "Carpentry", "John Builder", "3:30 PM", "2025-04-22", false, false, false),
                    // Completed requests
                    Request("REQ-003", "💡", "Electrical", "Bob Electric", "2:00 PM", "2025-04-20", true, false, false),
                    Request("REQ-004", "🔨", "Carpentry", "Mike Builder", "3:30 PM", "2025-04-19", true, false, false),
                    
                    // Rejected requests
                    Request("REQ-005", "🚰", "Plumbing", "Tom Plumber", "9:00 AM", "2025-04-18", false, false, true),
                    Request("REQ-006", "🧹", "Cleaning", "Sarah Cleaner", "1:00 PM", "2025-04-17", false, false, true)
                )
                
                sampleRequests.forEach { request ->
                    Log.d("RequestViewModel", "Inserting request: ${request.requestId} (completed: ${request.completed}, rejected: ${request.rejected})")
                    requestDao.insertRequest(request)
                }
                Log.d("RequestViewModel", "Sample data initialization completed")

                // Monitor counts
                launch {
                    totalRequestCount.collect { count ->
                        Log.d("RequestViewModel", "Total count: $count")
                    }
                }
                launch {
                    completedRequestCount.collect { count ->
                        Log.d("RequestViewModel", "Completed count: $count")
                    }
                }
                launch {
                    pendingRequestCount.collect { count ->
                        Log.d("RequestViewModel", "Pending count: $count")
                    }
                }
                launch {
                    rejectedRequestCount.collect { count ->
                        Log.d("RequestViewModel", "Rejected count: $count")
                    }
                }

                // Verify data after insertion
                val verifyRequests = requestDao.getAllRequests().first()
                Log.d("RequestViewModel", "Verification - Total requests in database: ${verifyRequests.size}")
                verifyRequests.forEach { request ->
                    Log.d("RequestViewModel", "Request ${request.requestId}: completed=${request.completed}, rejected=${request.rejected}")
                }

            } catch (e: Exception) {
                Log.e("RequestViewModel", "Error in database operations", e)
                e.printStackTrace()
            }
        }
    }

    fun addRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.insertRequest(request)
        }
    }

    fun updateRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.updateRequest(request)
        }
    }

    fun deleteRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.deleteRequest(request)
        }
    }

    fun getRequestById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.getRequestById(id)
        }
    }

    fun getRequestsByStatus(completed: Boolean): Flow<List<Request>> {
        return requestDao.getRequestsByStatus(completed)
    }

    fun getRequestsByReportedStatus(reported: Boolean): Flow<List<Request>> {
        return requestDao.getRequestsByReportedStatus(reported)
    }

    fun getRequestsByRejectedStatus(rejected: Boolean): Flow<List<Request>> {
        return requestDao.getRequestsByRejectedStatus(rejected)
    }

    fun rejectRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.updateRequest(request.copy(rejected = true, completed = false))
        }
    }

    fun completeRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            requestDao.updateRequest(request.copy(completed = true, rejected = false))
        }
    }
} 