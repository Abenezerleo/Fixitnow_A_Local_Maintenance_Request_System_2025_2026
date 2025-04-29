package com.example.assigment.ui.theme.Dao

import androidx.room.*
import com.example.assigment.ui.theme.Entity.Request
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Insert
    suspend fun insertRequest(request: Request): Long

    @Update
    suspend fun updateRequest(request: Request)

    @Delete
    suspend fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests")
    fun getAllRequests(): Flow<List<Request>>

    @Query("SELECT * FROM requests WHERE requestId = :id")
    suspend fun getRequestById(id: String): Request?

    @Query("SELECT * FROM requests WHERE completed = :completed")
    fun getRequestsByStatus(completed: Boolean): Flow<List<Request>>

    @Query("SELECT * FROM requests WHERE reported = :reported")
    fun getRequestsByReportedStatus(reported: Boolean): Flow<List<Request>>

    @Query("SELECT * FROM requests WHERE rejected = :rejected")
    fun getRequestsByRejectedStatus(rejected: Boolean): Flow<List<Request>>

    // Count queries
    @Query("SELECT COUNT(*) FROM requests")
    fun getTotalRequestCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM requests WHERE completed = 1 AND rejected = 0")
    fun getCompletedRequestCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM requests WHERE rejected = 1")
    fun getRejectedRequestCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM requests WHERE completed = 0 AND rejected = 0")
    fun getPendingRequestCount(): Flow<Int>
} 