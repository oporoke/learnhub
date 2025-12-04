package com.omondit.learnhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val classId: String? = null,
    val schoolId: String? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val user: UserDto,
    val token: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
