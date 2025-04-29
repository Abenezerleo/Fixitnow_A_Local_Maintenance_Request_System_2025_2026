package com.example.assigment.ui.theme.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.Dao.ServiceProviderDao
import com.example.assigment.ui.theme.Entity.ServiceProvider
import com.example.assigment.ui.theme.Enum.ServiceType
import com.example.assigment.ui.theme.Enum.StatusType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ServiceProviderViewModel(application: Application) : AndroidViewModel(application) {
    private val serviceProviderDao: ServiceProviderDao
    val allProviders: Flow<List<ServiceProvider>>

    init {
        try {
            val database = AppDatabase.getDatabase(application)
            serviceProviderDao = database.serviceProviderDao()
            allProviders = serviceProviderDao.getAvailableProviders()
                .catch { e -> 
                    // Handle any errors in the flow
                    e.printStackTrace()
                }
        } catch (e: Exception) {
            throw RuntimeException("Failed to initialize database", e)
        }
    }

    fun addProvider(provider: ServiceProvider) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                serviceProviderDao.insertProvider(provider)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateProvider(provider: ServiceProvider) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                serviceProviderDao.updateProvider(provider)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteProvider(provider: ServiceProvider) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                serviceProviderDao.deleteProvider(provider)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getProviderById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                serviceProviderDao.getById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getProvidersByServiceType(serviceType: ServiceType): Flow<List<ServiceProvider>> {
        return serviceProviderDao.getByServiceType(serviceType)
            .catch { e -> 
                e.printStackTrace()
            }
    }

    fun getProvidersByMultipleServiceTypes(serviceTypes: List<ServiceType>): Flow<List<ServiceProvider>> {
        return serviceProviderDao.getByMultipleServiceTypes(serviceTypes)
            .catch { e -> 
                e.printStackTrace()
            }
    }

    fun getAvailableProviders(): Flow<List<ServiceProvider>> {
        return serviceProviderDao.getAvailableProviders()
            .catch { e -> 
                e.printStackTrace()
            }
    }

    fun countAvailableProviders(): Flow<Int> {
        return serviceProviderDao.countAvailableProviders()
            .catch { e -> 
                e.printStackTrace()
            }
    }

    fun getAverageRatingByService(serviceType: ServiceType): Flow<Float> {
        return serviceProviderDao.getAverageRatingByService(serviceType)
            .catch { e -> 
                e.printStackTrace()
            }
    }
} 