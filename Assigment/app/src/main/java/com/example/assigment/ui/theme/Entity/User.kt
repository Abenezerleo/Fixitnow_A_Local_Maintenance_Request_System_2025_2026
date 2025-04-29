package com.example.assigment.ui.theme.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assigment.ui.theme.Enum.UserType
import java.util.Date

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val profileImageUrl: String?,
    val userType: UserType, // Enum:Male/Female
    val DoB: Date,
    val reported: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val emoji: String
        get() = if (userType == UserType.Male) "\uD83D\uDC68" else "\uD83D\uDC69"
}


