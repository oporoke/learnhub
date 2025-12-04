package com.omondit.learnhub.domain.usecase.content

import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetTopicsUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Topic>>() {

    override suspend fun invoke(params: String): Result<List<Topic>> {
        if (params.isBlank()) {
            return Result.failure(Exception("Subject ID cannot be empty"))
        }
        return contentRepository.getTopics(params)
    }
}
