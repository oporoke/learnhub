package com.omondit.learnhub.domain.usecase.question

import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetPracticeQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<String, List<Question>>() {

    override suspend fun invoke(params: String): Result<List<Question>> {
        if (params.isBlank()) {
            return Result.failure(Exception("Content ID cannot be empty"))
        }
        return questionRepository.getPracticeQuestions(params)
    }
}
