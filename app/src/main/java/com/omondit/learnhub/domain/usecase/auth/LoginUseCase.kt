package com.omondit.learnhub.domain.usecase.auth

import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.repository.AuthRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginUseCase.Params, User>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun invoke(params: Params): Result<User> {
        // Add validation
        if (params.email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if (params.password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        return authRepository.login(params.email, params.password)
    }
}