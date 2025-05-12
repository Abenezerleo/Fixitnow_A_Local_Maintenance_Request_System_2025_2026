package com.example.allapp.ViewModel
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.allapp.Model.AuthResponse
import com.example.allapp.Model.LoginRequest
import com.example.allapp.Model.RegisterRequest
import com.example.allapp.Model.UserProfile
import com.example.allapp.Model.enums.RoleType
import com.example.allapp.Repository.AuthRepository
import com.example.allapp.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AccountViewModel(private val context: Context) : ViewModel() {
    private val authRepository = AuthRepository()
    private val TAG = "AccountViewModel"

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess: StateFlow<Boolean> = _signupSuccess

    init {
        getToken()?.let { token ->
            RetrofitClient.setAuthToken(token)
        }
    }

    fun createAdminAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val registerRequest = RegisterRequest(
                    name = "Admin",
                    email = "admin@fixitnow.com",
                    phone = "1234567890",
                    password = "admin123",
                    role = RoleType.ADMIN.name,
                    gender = "Male"
                )
                authRepository.register(registerRequest).fold(
                    onSuccess = { response ->
                        _currentUser.value = response.user
                        saveToken(response.access_token)
                    },
                    onFailure = { exception ->
                        if (!exception.message?.contains("already exists", ignoreCase = true)!!) {
                            _error.value = exception.message
                        }
                    }
                )
            } catch (e: Exception) {
                if (!e.message?.contains("already exists", ignoreCase = true)!!) {
                    _error.value = e.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val loginRequest = LoginRequest(username, password)
                authRepository.login(loginRequest).fold(
                    onSuccess = { response ->
                        val userProfile = extractUserFromJwt(response.access_token)
                        println("Login success: user=$userProfile, role=${userProfile?.role}")
                        _currentUser.value = userProfile
                        saveToken(response.access_token)
                    },
                    onFailure = { exception ->
                        println("Login failed: ${exception.message}")
                        _error.value = exception.message ?: "Login failed"
                        _currentUser.value = null
                        clearToken()
                    }
                )
            } catch (e: Exception) {
                println("Login exception: ${e.message}")
                _error.value = e.message ?: "An error occurred"
                _currentUser.value = null
                clearToken()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signup(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: RoleType,
        gender: String,
        serviceType: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val registerRequest = RegisterRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    role = role.name,
                    gender = gender,
                    serviceType = serviceType
                )
                authRepository.register(registerRequest).fold(
                    onSuccess = { response ->
                        _signupSuccess.value = true
                        _currentUser.value = response.user
                        saveToken(response.access_token)
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Registration failed"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        clearSession()
    }

    private fun clearSession() {
        viewModelScope.launch {
            try {
                val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                RetrofitClient.setAuthToken(null)  // Clear the access token
                _currentUser.value = null
                _signupSuccess.value = false
                Log.d(TAG, "Session cleared successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing session", e)
                _error.value = "Failed to logout: ${e.message}"
            }
        }
    }

    private fun saveToken(token: String) {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("auth_token", token)
            .apply()
        RetrofitClient.setAuthToken(token)
    }

    private fun clearToken() {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .edit()
            .remove("auth_token")
            .apply()
        RetrofitClient.setAuthToken(null)
    }

    fun getToken(): String? {
        return context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getString("auth_token", null)
    }

    fun isAuthenticated(): Boolean {
        return getToken() != null && _currentUser.value != null
    }

    fun getCurrentRole(): String? {
        return _currentUser.value?.role
    }

    private fun extractUserFromJwt(token: String): UserProfile? {
        return try {
            val payload = token.split(".")[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedPayload = String(decodedBytes)
            val json = JSONObject(decodedPayload)
            val id = json.getInt("sub").toString() // Convert Int to String
            val name = json.getString("name")
            val role = json.getString("role")
            // Provide default values for required fields not in JWT
            UserProfile(
                id = id,
                name = name,
                email = "", // Placeholder; fetch later if needed
                phone = "", // Placeholder
                gender = "",
                password="",// Placeholder
                role = role,
                serviceType = null,
                bankName = null,
                accountNumber = null,
                accountName = null,
                cbeAccount = null,
                paypalAccount = null,
                telebirrAccount = null,
                awashAccount = null
            )
        } catch (e: Exception) {
            println("JWT decode error: ${e.message}")
            null
        }
    }
}

class AccountViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}