package com.omondit.learnhub.domain.usecase.auth

import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.repository.AuthRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<RegisterUseCase.Params, User>() {

    data class Params(
        val name: String,
        val email: String,
        val password: String
    )

    override suspend fun invoke(params: Params): Result<User> {
        // Validate
        if (params.name.isBlank()) {
            return Result.failure(Exception("Name cannot be empty"))
        }
        if (params.email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if (params.password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        return authRepository.register(params.name, params.email, params.password)
    }
}
