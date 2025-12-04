package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType

interface QuestionRepository {
    // Practice questions (for students)
    suspend fun getPracticeQuestions(contentId: String): Result<List<Question>>

    // Teacher question bank
    suspend fun getTeacherQuestions(
        teacherId: String,
        classId: String? = null,
        topicId: String? = null,
        subtopicId: String? = null
    ): Result<List<Question>>

    suspend fun createQuestion(question: Question): Result<Question>
    suspend fun updateQuestion(question: Question): Result<Question>
    suspend fun deleteQuestion(questionId: String): Result<Unit>

    // Exam generation
    suspend fun generateExamQuestions(
        classId: String,
        topicIds: List<String>,
        count: Int,
        questionTypes: List<QuestionType>,
        difficulty: Difficulty? = null
    ): Result<List<Question>>
}