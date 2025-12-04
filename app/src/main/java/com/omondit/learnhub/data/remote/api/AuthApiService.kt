package com.omondit.learnhub.data.remote.api

import com.omondit.learnhub.data.remote.ApiResponse
import com.omondit.learnhub.data.remote.dto.LoginRequest
import com.omondit.learnhub.data.remote.dto.LoginResponse
import com.omondit.learnhub.data.remote.dto.RegisterRequest
import com.omondit.learnhub.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<UserDto>

    @POST("auth/logout")
    suspend fun logout(): ApiResponse<Unit>

    @GET("auth/me")
    suspend fun getCurrentUser(): ApiResponse<UserDto>
}