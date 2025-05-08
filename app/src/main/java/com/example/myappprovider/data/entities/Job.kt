package com.example.myappprovider.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val location: String,
    val wage: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val clientName: String,
    val clientPhone: String,
    val status: String, // "AVAILABLE", "ASSIGNED", "COMPLETED"
    val providerId: Long? = null,
    val description: String = "",
    val isCompleted: Boolean = false
) 