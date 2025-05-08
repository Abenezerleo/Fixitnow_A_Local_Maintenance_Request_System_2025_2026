package com.example.myappprovider.data.repository

import com.example.myappprovider.data.dao.ProviderDao
import com.example.myappprovider.data.entities.Provider
import kotlinx.coroutines.flow.Flow

class ProviderRepository(private val providerDao: ProviderDao) {
    fun getProvider(providerId: Long): Flow<Provider> = providerDao.getProvider(providerId)

    suspend fun insertProvider(provider: Provider): Long = providerDao.insertProvider(provider)

    suspend fun updateProvider(provider: Provider) = providerDao.updateProvider(provider)

    suspend fun updateProviderStatus(providerId: Long, status: String) = 
        providerDao.updateProviderStatus(providerId, status)

    suspend fun updateProviderStats(providerId: Long, rating: Float, totalJobs: Int, completedJobs: Int) =
        providerDao.updateProviderStats(providerId, rating, totalJobs, completedJobs)

    suspend fun updateProviderProfile(
        providerId: Long,
        fullName: String,
        phone: String,
        email: String,
        dob: String,
        jobTitle: String
    ) = providerDao.updateProviderProfile(providerId, fullName, phone, email, dob, jobTitle)

    suspend fun updateProviderPaymentInfo(
        providerId: Long,
        cbe: String,
        teleBirr: String,
        awashBank: String,
        paypal: String
    ) = providerDao.updateProviderPaymentInfo(providerId, cbe, teleBirr, awashBank, paypal)
} 