package com.example.allapp.api

import com.example.allapp.Model.AuthResponse
import com.example.allapp.Model.LoginRequest
import com.example.allapp.Model.RegisterRequest
import com.example.allapp.Model.UserProfile
import retrofit2.http.*

interface ApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/" // Use this for Android Emulator
        // const val BASE_URL = "http://localhost:3000/" // Use this for physical device
    }

    // Add your API endpoints here, for example:
    @GET("api/items")
    suspend fun getItems(): List<Any> // Replace 'Any' with your actual data model

    @GET("api/items/{id}")
    suspend fun getItem(@Path("id") id: String): Any // Replace 'Any' with your actual data model

    @POST("api/items")
    suspend fun createItem(@Body item: Any): Any // Replace 'Any' with your actual data model

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfile
}