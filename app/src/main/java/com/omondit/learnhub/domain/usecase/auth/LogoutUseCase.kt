package com.omondit.learnhub.domain.usecase.auth

import com.omondit.learnhub.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
