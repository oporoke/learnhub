package com.omondit.learnhub.domain.usecase.progress

import com.omondit.learnhub.domain.model.ContentProgress
import com.omondit.learnhub.domain.repository.ProgressRepository
import javax.inject.Inject

class GetContentProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(userId: String, contentId: String): ContentProgress? {
        return progressRepository.getContentProgress(userId, contentId)
    }
}
