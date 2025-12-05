package com.omondit.learnhub.domain.usecase.question

import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetTeacherQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) : BaseUseCase<GetTeacherQuestionsUseCase.Params, List<Question>>() {

    data class Params(
        val teacherId: String,
        val classId: String? = null,
        val topicId: String? = null,
        val subtopicId: String? = null
    )

    override suspend fun invoke(params: Params): Result<List<Question>> {
        return questionRepository.getTeacherQuestions(
            teacherId = params.teacherId,
            classId = params.classId,
            topicId = params.topicId,
            subtopicId = params.subtopicId
        )
    }
}
