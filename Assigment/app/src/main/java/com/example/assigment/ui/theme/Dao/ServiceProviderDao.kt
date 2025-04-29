package com.example.assigment.ui.theme.Dao

import androidx.room.*
import com.example.assigment.ui.theme.Entity.ServiceProvider
import com.example.assigment.ui.theme.Enum.ServiceType
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceProviderDao {
    @Insert
    suspend fun insertProvider(provider: ServiceProvider): Long

    @Update
    suspend fun updateProvider(provider: ServiceProvider)

    @Delete
    suspend fun deleteProvider(provider: ServiceProvider)

    @Query("SELECT * FROM service_providers WHERE providerId = :id")
    suspend fun getById(id: String): ServiceProvider?

    @Query("SELECT * FROM service_providers WHERE userId = :userId")
    suspend fun getByUserId(userId: String): ServiceProvider?

    @Query("SELECT * FROM service_providers WHERE serviceType = :serviceType")
    fun getByServiceType(serviceType: ServiceType): Flow<List<ServiceProvider>>

    @Query("SELECT * FROM service_providers WHERE serviceType IN (:serviceTypes)")
    fun getByMultipleServiceTypes(serviceTypes: List<ServiceType>): Flow<List<ServiceProvider>>

    @Query("SELECT * FROM service_providers WHERE provstatus = 'AVAILABLE'")
    fun getAvailableProviders(): Flow<List<ServiceProvider>>

    @Query("SELECT * FROM service_providers WHERE serviceType = :serviceType AND provstatus = 'AVAILABLE' LIMIT :limit")
    fun getTopAvailableByServiceType(serviceType: ServiceType, limit: Int = 5): Flow<List<ServiceProvider>>

    // Statistics
    @Query("SELECT COUNT(*) FROM service_providers WHERE provstatus = 'AVAILABLE'")
    fun countAvailableProviders(): Flow<Int>

    @Query("SELECT AVG(rating) FROM service_providers WHERE serviceType = :serviceType")
    fun getAverageRatingByService(serviceType: ServiceType): Flow<Float>
}