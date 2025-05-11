package com.example.allapp.Model

data class LoginRequest(
    val name: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val phone: String,
    val email: String,
    val gender: String,
    val role: String,
    val password: String,
    val serviceType: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null,
    val accountName: String? = null,
    val cbeAccount: String? = null,
    val paypalAccount: String? = null,
    val telebirrAccount: String? = null,
    val awashAccount: String? = null
)

data class AuthResponse(
    val access_token: String,
    val user: UserProfile?
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val role: String,
    val serviceType: String?,
    val bankName: String?,
    val accountNumber: String?,
    val accountName: String?,
    val cbeAccount: String?,
    val paypalAccount: String?,
    val telebirrAccount: String?,
    val awashAccount: String?
) 