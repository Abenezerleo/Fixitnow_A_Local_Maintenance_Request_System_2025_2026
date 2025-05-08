package com.example.myappprovider.data.dao

import androidx.room.*
import com.example.myappprovider.data.entities.Provider
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers WHERE id = :providerId")
    fun getProvider(providerId: Long): Flow<Provider>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: Provider): Long

    @Update
    suspend fun updateProvider(provider: Provider)

    @Query("UPDATE providers SET status = :status WHERE id = :providerId")
    suspend fun updateProviderStatus(providerId: Long, status: String)

    @Query("UPDATE providers SET rating = :rating, totalJobs = :totalJobs, completedJobs = :completedJobs WHERE id = :providerId")
    suspend fun updateProviderStats(providerId: Long, rating: Float, totalJobs: Int, completedJobs: Int)

    @Query("UPDATE providers SET fullName = :fullName, phone = :phone, email = :email, dob = :dob, jobTitle = :jobTitle WHERE id = :providerId")
    suspend fun updateProviderProfile(
        providerId: Long,
        fullName: String,
        phone: String,
        email: String,
        dob: String,
        jobTitle: String
    )

    @Query("UPDATE providers SET cbeAccount = :cbe, teleBirrAccount = :teleBirr, awashBankAccount = :awashBank, paypalAccount = :paypal WHERE id = :providerId")
    suspend fun updateProviderPaymentInfo(
        providerId: Long,
        cbe: String,
        teleBirr: String,
        awashBank: String,
        paypal: String
    )
} 