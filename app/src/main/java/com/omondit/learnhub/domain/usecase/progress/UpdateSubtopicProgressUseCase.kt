package com.omondit.learnhub.domain.usecase.progress

import com.omondit.learnhub.domain.repository.ProgressRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdateSubtopicProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) : BaseUseCase<UpdateSubtopicProgressUseCase.Params, Unit>() {

    data class Params(
        val userId: String,
        val subtopicId: String,
        val totalContent: Int,
        val completedContent: Int
    )

    override suspend fun invoke(params: Params): Result<Unit> {
        return progressRepository.updateSubtopicProgress(
            userId = params.userId,
            subtopicId = params.subtopicId,
            totalContent = params.totalContent,
            completedContent = params.completedContent
        )
    }
}
