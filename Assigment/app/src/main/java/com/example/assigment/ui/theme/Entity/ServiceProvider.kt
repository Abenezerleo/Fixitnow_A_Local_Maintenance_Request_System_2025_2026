package com.example.assigment.ui.theme.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assigment.ui.theme.Enum.ServiceType
import com.example.assigment.ui.theme.Enum.StatusType
import java.util.*

@Entity(tableName = "service_providers")
data class ServiceProvider(
    @PrimaryKey
    val providerId: String,
    val userId: String,
    val name: String?,
    val email: String?,
    val phoneNumber: String?,
    val gender: String,
    val serviceType: ServiceType,
    val licenseNumber: String?,
    val rating: Float,
    val totalJobsCompleted: Int,
    val provstatus: StatusType,
    val DoB: Date
)