package com.omondit.learnhub.domain.usecase.auth

import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}
