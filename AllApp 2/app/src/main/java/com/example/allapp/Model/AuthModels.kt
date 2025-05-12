package com.example.allapp.Model

import java.util.Date
import com.example.allapp.Model.ServiceType

enum class RequestStatus {
    PENDING, ACCEPTED,IN_PROGRESS, REJECTED, COMPLETED, CANCELLED
}

data class Request(
    val requestId: Int,
    val serviceType: ServiceType,
    val description: String,
    val urgency: String,
    val budget: Double?,
    val status: RequestStatus,
    val scheduledDate: Date?,
    val completionDate: Date?,
    val rating: Int?,
    val review: String?,
    val requester: UserProfile,
    val provider: UserProfile?,
    val service: Service?,
    val createdAt: Date,
    val updatedAt: Date
)

data class Service(
    val id: Int,
    val name: String
)

data class UserProfile(
    val id: String? = null,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val role: String,
    val password: String? = null,
    val serviceType: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null,
    val accountName: String? = null,
    val cbeAccount: String? = null,
    val paypalAccount: String? = null,
    val telebirrAccount: String? = null,
    val awashAccount: String? = null,
    val age: Int? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val joinDate: Date? = null,
    val membershipStatus: String? = null,
    val providerRating: Double? = null,
    val totalJobsCompleted: Int? = null,
    val totalIncome: Double? = null
)

data class UserUpdate(
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val serviceType: String? = null
)

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

data class CreateRequestDto(
    val serviceType: ServiceType,
    val description: String,
    val urgency: String,
    val budget: Double?,
    val scheduledDate: Date?,
    val requester_id: String,
    val status: RequestStatus = RequestStatus.PENDING
)
 