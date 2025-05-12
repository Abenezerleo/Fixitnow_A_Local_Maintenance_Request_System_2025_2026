package com.example.allapp.api

import com.example.allapp.Model.AuthResponse
import com.example.allapp.Model.CreateRequestDto
import com.example.allapp.Model.LoginRequest
import com.example.allapp.Model.RegisterRequest
import com.example.allapp.Model.Request
import com.example.allapp.Model.UserProfile
import com.example.allapp.Model.UserUpdate
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/" // Use this for Android Emulator
        // const val BASE_URL = "http://localhost:3000/" // Use this for physical device
    }

    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfile

    // User endpoints
    @GET("users")
    suspend fun getAllUsers(): List<UserProfile>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserProfile

    @POST("users")
    suspend fun createUser(@Body user: UserProfile): UserProfile

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: UserUpdate): UserProfile

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)

    @GET("users/search-requester")
    suspend fun searchRequesterByName(@Query("name") name: String): List<UserProfile>

    @GET("users/search-provider")
    suspend fun searchProviderByName(@Query("name") name: String): List<UserProfile>

    // Request endpoints
    @GET("requests/unassigned")
    suspend fun getUnassignedRequests(@Header("Authorization") token: String): Response<List<Request>>

    @GET("requests/provider-requests")
    suspend fun getProviderRequests(@Header("Authorization") token: String): Response<List<Request>>

    @GET("requests/provider-accepted")
    suspend fun getProviderAcceptedRequests(@Header("Authorization") token: String): Response<List<Request>>

    @GET("requests/provider-completed")
    suspend fun getProviderCompletedRequests(@Header("Authorization") token: String): Response<List<Request>>

    @GET("requests/requester-requests")
    suspend fun getRequesterRequests(@Header("Authorization") token: String): Response<List<Request>>

    @POST("requests")
    suspend fun createRequest(
        @Body createRequestDto: CreateRequestDto,
        @Header("Authorization") token: String
    ): Response<Request>

    @PATCH("requests/{requestId}/accept")
    suspend fun acceptRequest(
        @Path("requestId") requestId: Int,
        @Header("Authorization") token: String
    ): Response<Request>

    @PATCH("requests/{requestId}/cancel")
    suspend fun cancelRequest(
        @Path("requestId") requestId: Int,
        @Header("Authorization") token: String
    ): Response<Request>

    @DELETE("requests/user/{userId}")
    suspend fun deleteUserRequests(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    // Provider statistics endpoints
    @GET("requests/provider-stats/completed-count")
    suspend fun getProviderCompletedCount(@Header("Authorization") token: String): Response<Int>

    @GET("requests/provider-stats/average-rating")
    suspend fun getProviderAverageRating(@Header("Authorization") token: String): Response<Double>

    @GET("requests/provider-stats/total-budget")
    suspend fun getProviderTotalBudget(@Header("Authorization") token: String): Response<Double>

    // Add your API endpoints here, for example:
    @GET("api/items")
    suspend fun getItems(): List<Any> // Replace 'Any' with your actual data model

    @GET("api/items/{id}")
    suspend fun getItem(@Path("id") id: String): Any // Replace 'Any' with your actual data model

    @POST("api/items")
    suspend fun createItem(@Body item: Any): Any // Replace 'Any' with your actual data model
}