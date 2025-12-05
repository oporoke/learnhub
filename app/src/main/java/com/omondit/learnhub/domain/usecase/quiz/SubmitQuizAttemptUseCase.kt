package com.omondit.learnhub.domain.usecase.quiz

import com.omondit.learnhub.domain.model.QuizAttempt
import com.omondit.learnhub.domain.model.QuizResult
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class SubmitQuizAttemptUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<QuizAttempt, QuizResult>() {

    override suspend fun invoke(params: QuizAttempt): Result<QuizResult> {
        return questionRepository.submitQuizAttempt(params)
    }
}
