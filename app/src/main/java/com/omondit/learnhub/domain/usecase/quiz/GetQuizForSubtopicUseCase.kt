package com.omondit.learnhub.domain.usecase.quiz

import com.omondit.learnhub.domain.model.Quiz
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetQuizForSubtopicUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<String, Quiz>() {

    override suspend fun invoke(params: String): Result<Quiz> {
        return questionRepository.getQuizForSubtopic(params)
    }
}
