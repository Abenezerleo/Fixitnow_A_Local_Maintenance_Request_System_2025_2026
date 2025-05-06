package com.example.assigment.ui.theme.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assigment.ui.theme.Enum.RoleType


@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey
    val accountId: String,
    val fullName : String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val gender: String,
    val role: RoleType,
    val rating: Float,
    val totalJobsCompleted: Int
)
