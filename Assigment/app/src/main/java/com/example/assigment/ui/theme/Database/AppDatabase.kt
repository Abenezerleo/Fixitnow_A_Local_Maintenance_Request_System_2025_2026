package com.example.assigment.ui.theme.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.assigment.ui.theme.Dao.RequestDao
import com.example.assigment.ui.theme.Dao.ServiceProviderDao
import com.example.assigment.ui.theme.Dao.UserDao
import com.example.assigment.ui.theme.Entity.Request
import com.example.assigment.ui.theme.Entity.ServiceProvider
import com.example.assigment.ui.theme.Entity.User

@Database(entities = [User::class, ServiceProvider::class, Request::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun serviceProviderDao(): ServiceProviderDao
    abstract fun requestDao(): RequestDao

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
                instance
            }
        }
    }
} 