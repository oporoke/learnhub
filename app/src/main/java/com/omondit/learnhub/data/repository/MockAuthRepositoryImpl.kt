package com.omondit.learnhub.data.repository

import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.model.UserRole
import com.omondit.learnhub.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    private val currentUser: StateFlow<User?> = _currentUser

    // Mock user database
    private val mockUsers = listOf(
        User(
            id = "1",
            name = "John Teacher",
            email = "teacher@test.com",
            role = UserRole.TEACHER,
            classId = null,
            schoolId = "school1"
        ),
        User(
            id = "2",
            name = "Jane Student",
            email = "student@test.com",
            role = UserRole.STUDENT,
            classId = "form3",
            schoolId = "school1"
        ),
        User(
            id = "3",
            name = "Admin User",
            email = "admin@test.com",
            role = UserRole.ADMIN,
            classId = null,
            schoolId = null
        )
    )

    override suspend fun login(email: String, password: String): Result<User> {
        // Simulate network delay
        delay(1000)

        // Mock validation: any password with length >= 6 works
        if (password.length < 6) {
            return Result.failure(Exception("Invalid password"))
        }

        // Find user by email
        val user = mockUsers.find { it.email.equals(email, ignoreCase = true) }

        return if (user != null) {
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(Exception("User not found. Try: teacher@test.com, student@test.com, or admin@test.com"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        delay(1000)

        // Check if user already exists
        if (mockUsers.any { it.email.equals(email, ignoreCase = true) }) {
            return Result.failure(Exception("User already exists"))
        }

        // Create new user
        val newUser = User(
            id = (mockUsers.size + 1).toString(),
            name = name,
            email = email,
            role = UserRole.STUDENT, // Default role
            classId = null,
            schoolId = null
        )

        _currentUser.value = newUser
        return Result.success(newUser)
    }

    override suspend fun logout(): Result<Unit> {
        delay(500)
        _currentUser.value = null
        return Result.success(Unit)
    }

    override suspend fun getCurrentUser(): User? {
        return _currentUser.value
    }

    override suspend fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
