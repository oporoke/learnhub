package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.remote.api.AuthApiService
import com.omondit.learnhub.data.remote.dto.LoginRequest
import com.omondit.learnhub.data.remote.dto.RegisterRequest
import com.omondit.learnhub.data.remote.mapper.toDomain
import com.omondit.learnhub.data.security.SecureTokenStorage
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val secureTokenStorage: SecureTokenStorage
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    private val currentUser: StateFlow<User?> = _currentUser

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequest(email, password))

            if (response.success && response.data != null) {
                val user = response.data.user.toDomain()
                secureTokenStorage.saveToken(response.data.token)
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
                secureTokenStorage.clearToken()
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
        return _currentUser.value != null && secureTokenStorage.hasToken()
    }

    fun getAuthToken(): String? = secureTokenStorage.getToken()
}
