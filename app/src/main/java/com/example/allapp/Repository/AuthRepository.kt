package com.example.allapp.Repository

import com.example.allapp.Model.AuthResponse
import com.example.allapp.Model.LoginRequest
import com.example.allapp.Model.RegisterRequest
import com.example.allapp.Model.UserProfile
import com.example.allapp.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(registerRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(loginRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(token: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProfile("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 