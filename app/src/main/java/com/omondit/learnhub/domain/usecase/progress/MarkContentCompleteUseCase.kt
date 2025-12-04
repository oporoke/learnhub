package com.omondit.learnhub.domain.usecase.progress

import com.omondit.learnhub.domain.repository.ProgressRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class MarkContentCompleteUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) : BaseUseCase<MarkContentCompleteUseCase.Params, Unit>() {

    data class Params(
        val userId: String,
        val contentId: String,
        val subtopicId: String
    )

    override suspend fun invoke(params: Params): Result<Unit> {
        return progressRepository.markContentComplete(
            userId = params.userId,
            contentId = params.contentId,
            subtopicId = params.subtopicId
        )
    }
}
