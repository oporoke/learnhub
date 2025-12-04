package com.omondit.learnhub.domain.usecase.content

import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.usecase.NoParamsUseCase
import javax.inject.Inject

class GetClassesUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : NoParamsUseCase<List<CKlass>>() {

    override suspend fun invoke(): Result<List<CKlass>> {
        return contentRepository.getClasses()
    }
}
