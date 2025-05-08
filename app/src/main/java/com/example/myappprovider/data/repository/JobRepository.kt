package com.example.myappprovider.data.repository

import com.example.myappprovider.data.dao.JobDao
import com.example.myappprovider.data.entities.Job
import kotlinx.coroutines.flow.Flow

class JobRepository(private val jobDao: JobDao) {
    fun getAvailableJobs(): Flow<List<Job>> = jobDao.getAvailableJobs()
    
    fun getProviderJobs(providerId: Long): Flow<List<Job>> = jobDao.getProviderJobs(providerId)
    
    fun getProviderJobsByStatus(providerId: Long, isCompleted: Boolean): Flow<List<Job>> = 
        jobDao.getProviderJobsByStatus(providerId, isCompleted)

    suspend fun insertJob(job: Job): Long = jobDao.insertJob(job)

    suspend fun updateJob(job: Job) = jobDao.updateJob(job)

    suspend fun deleteJob(job: Job) = jobDao.deleteJob(job)

    suspend fun updateJobStatus(jobId: Long, status: String, providerId: Long?) = 
        jobDao.updateJobStatus(jobId, status, providerId)

    suspend fun updateJobCompletion(jobId: Long, isCompleted: Boolean) = 
        jobDao.updateJobCompletion(jobId, isCompleted)
} 