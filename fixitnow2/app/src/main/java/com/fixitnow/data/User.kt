package com.fixitnow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserType {
    ADMIN,
    REQUESTER,
    PROVIDER
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val username: String,
    val password: String,
    val userType: UserType
) 