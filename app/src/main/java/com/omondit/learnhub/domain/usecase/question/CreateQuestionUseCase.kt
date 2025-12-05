package com.omondit.learnhub.domain.usecase.question


import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class CreateQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<Question, Question>() {

    override suspend fun invoke(params: Question): Result<Question> {
        return questionRepository.createQuestion(params)
    }
}
