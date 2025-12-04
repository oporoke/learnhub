package com.omondit.learnhub.domain.usecase.content

import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetSubjectsUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Subject>>() {

    override suspend fun invoke(params: String): Result<List<Subject>> {
        if (params.isBlank()) {
            return Result.failure(Exception("Class ID cannot be empty"))
        }
        return contentRepository.getSubjects(params)
    }
}
