package com.omondit.learnhub.domain.usecase.question

import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GenerateExamQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<GenerateExamQuestionsUseCase.Params, List<Question>>() {

    data class Params(
        val classId: String,
        val topicIds: List<String>,
        val count: Int,
        val questionTypes: List<QuestionType>,
        val difficulty: Difficulty? = null
    )

    override suspend fun invoke(params: Params): Result<List<Question>> {
        return questionRepository.generateExamQuestions(
            classId = params.classId,
            topicIds = params.topicIds,
            count = params.count,
            questionTypes = params.questionTypes,
            difficulty = params.difficulty
        )
    }
}
