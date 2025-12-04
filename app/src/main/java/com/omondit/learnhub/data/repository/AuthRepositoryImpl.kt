package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.remote.api.AuthApiService
import com.omondit.learnhub.data.remote.dto.LoginRequest
import com.omondit.learnhub.data.remote.dto.RegisterRequest
import com.omondit.learnhub.data.remote.mapper.toDomain
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {

    // In-memory storage for now (will use DataStore later)
    private val _currentUser = MutableStateFlow<User?>(null)
    private val currentUser: StateFlow<User?> = _currentUser

    private var authToken: String? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequest(email, password))

            if (response.success && response.data != null) {
                val user = response.data.user.toDomain()
                authToken = response.data.token
                _currentUser.value = user
                Result.success(user)
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return try {
            val response = authApiService.register(RegisterRequest(name, email, password))

            if (response.success && response.data != null) {
                val user = response.data.toDomain()
                Result.success(user)
            } else {
                Result.failure(Exception(response.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = authApiService.logout()

            if (response.success) {
                authToken = null
                _currentUser.value = null
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Logout failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return _currentUser.value
    }

    override suspend fun isLoggedIn(): Boolean {
        return _currentUser.value != null && authToken != null
    }

    fun getAuthToken(): String? = authToken
}
