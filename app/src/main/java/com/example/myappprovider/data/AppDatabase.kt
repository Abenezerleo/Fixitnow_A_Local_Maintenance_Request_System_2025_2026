package com.example.myappprovider.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myappprovider.data.dao.JobDao
import com.example.myappprovider.data.dao.ProviderDao
import com.example.myappprovider.data.entities.Job
import com.example.myappprovider.data.entities.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Job::class, Provider::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun providerDao(): ProviderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                
                // Insert sample data
                CoroutineScope(Dispatchers.IO).launch {
                    val jobDao = instance.jobDao()
                    
                    // Clear existing jobs
                    jobDao.deleteAllJobs()
                    
                    // Insert sample jobs
                    jobDao.insertJob(
                        Job(
                            title = "Plumber",
                            location = "Addis Ababa",
                            wage = "1200 ETB",
                            startTime = "9:00 AM",
                            endTime = "5:00 PM",
                            date = "02/01/2025",
                            clientName = "John Doe",
                            clientPhone = "0912345678",
                            status = "AVAILABLE",
                            description = "Fix leaky faucet"
                        )
                    )
                    
                    jobDao.insertJob(
                        Job(
                            title = "Electrician",
                            location = "Bole",
                            wage = "1500 ETB",
                            startTime = "10:00 AM",
                            endTime = "6:00 PM",
                            date = "03/01/2025",
                            clientName = "Jane Smith",
                            clientPhone = "0923456789",
                            status = "AVAILABLE",
                            description = "Install new outlets"
                        )
                    )
                    
                    jobDao.insertJob(
                        Job(
                            title = "Carpenter",
                            location = "Sarbet",
                            wage = "2000 ETB",
                            startTime = "8:00 AM",
                            endTime = "4:00 PM",
                            date = "01/01/2025",
                            clientName = "Mike Johnson",
                            clientPhone = "0934567890",
                            status = "ASSIGNED",
                            providerId = 1L,
                            description = "Making kitchen cabinet",
                            isCompleted = true
                        )
                    )
                    
                    jobDao.insertJob(
                        Job(
                            title = "Painter",
                            location = "CMC",
                            wage = "1800 ETB",
                            startTime = "9:00 AM",
                            endTime = "5:00 PM",
                            date = "04/01/2025",
                            clientName = "Sarah Williams",
                            clientPhone = "0945678901",
                            status = "ASSIGNED",
                            providerId = 1L,
                            description = "Painting bedroom wall",
                            isCompleted = false
                        )
                    )
                }
                
                instance
            }
        }
    }
} 