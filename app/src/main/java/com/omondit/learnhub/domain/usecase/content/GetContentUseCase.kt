package com.omondit.learnhub.domain.usecase.content

import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetContentUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Content>>() {

    override suspend fun invoke(params: String): Result<List<Content>> {
        if (params.isBlank()) {
            return Result.failure(Exception("Subtopic ID cannot be empty"))
        }
        return contentRepository.getContent(params)
    }
}