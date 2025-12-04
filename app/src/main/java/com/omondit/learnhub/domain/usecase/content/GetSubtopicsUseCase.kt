package com.omondit.learnhub.domain.usecase.content

import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetSubtopicsUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Subtopic>>() {

    override suspend fun invoke(params: String): Result<List<Subtopic>> {
        if (params.isBlank()) {
            return Result.failure(Exception("Topic ID cannot be empty"))
        }
        return contentRepository.getSubtopics(params)
    }
}
