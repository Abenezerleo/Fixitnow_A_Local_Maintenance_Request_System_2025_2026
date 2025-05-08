package com.example.myappprovider.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class Provider(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phone: String,
    val email: String,
    val dob: String,
    val jobTitle: String,
    val cbeAccount: String = "",
    val teleBirrAccount: String = "",
    val awashBankAccount: String = "",
    val paypalAccount: String = "",
    val status: String = "ACTIVE",
    val rating: Float = 0f,
    val totalJobs: Int = 0,
    val completedJobs: Int = 0
) 