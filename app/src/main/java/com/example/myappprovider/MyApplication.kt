package com.example.myappprovider

import android.app.Application
import com.example.myappprovider.data.AppDatabase
import com.example.myappprovider.data.repository.JobRepository
import com.example.myappprovider.data.repository.ProviderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val jobRepository by lazy { JobRepository(database.jobDao()) }
    val providerRepository by lazy { ProviderRepository(database.providerDao()) }

    override fun onCreate() {
        super.onCreate()
        
        // Insert sample data
        CoroutineScope(Dispatchers.IO).launch {
            database.insertSampleData()
        }
    }
} 