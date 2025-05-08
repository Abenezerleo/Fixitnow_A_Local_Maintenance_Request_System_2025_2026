package com.example.myappprovider.data.dao

import androidx.room.*
import com.example.myappprovider.data.entities.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs WHERE status = 'AVAILABLE'")
    fun getAvailableJobs(): Flow<List<Job>>

    @Query("SELECT * FROM jobs WHERE providerId = :providerId")
    fun getProviderJobs(providerId: Long): Flow<List<Job>>

    @Query("SELECT * FROM jobs WHERE providerId = :providerId AND isCompleted = :isCompleted")
    fun getProviderJobsByStatus(providerId: Long, isCompleted: Boolean): Flow<List<Job>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: Job): Long

    @Update
    suspend fun updateJob(job: Job)

    @Delete
    suspend fun deleteJob(job: Job)

    @Query("DELETE FROM jobs")
    suspend fun deleteAllJobs()

    @Query("UPDATE jobs SET status = :status, providerId = :providerId WHERE id = :jobId")
    suspend fun updateJobStatus(jobId: Long, status: String, providerId: Long?)

    @Query("UPDATE jobs SET isCompleted = :isCompleted WHERE id = :jobId")
    suspend fun updateJobCompletion(jobId: Long, isCompleted: Boolean)
} 