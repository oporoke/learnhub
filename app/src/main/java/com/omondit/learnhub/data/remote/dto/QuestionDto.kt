package com.omondit.learnhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: String,
    val contentId: String? = null,
    val teacherId: String? = null,
    val classId: String,
    val questionType: String,
    val questionText: String,
    val subQuestions: List<SubQuestionDto>? = null,
    val options: List<String>? = null,
    val correctAnswer: String,
    val difficulty: String,
    val topicId: String,
    val subtopicId: String
)

@Serializable
data class SubQuestionDto(
    val label: String,
    val text: String,
    val answer: String
)