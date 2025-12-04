package com.omondit.learnhub.domain.model

data class Question(
    val id: String,
    val contentId: String? = null, // Linked to content for practice questions
    val teacherId: String? = null, // For teacher-created questions
    val classId: String,
    val questionType: QuestionType,
    val questionText: String,
    val subQuestions: List<SubQuestion>? = null,
    val options: List<String>? = null, // For MCQ
    val correctAnswer: String,
    val difficulty: Difficulty,
    val topicId: String,
    val subtopicId: String
)

enum class QuestionType {
    STANDALONE,
    SECTIONAL, // Has sub-questions like (a), (b), (c)
    MCQ
}

data class SubQuestion(
    val label: String, // e.g., "(a)", "(i)"
    val text: String,
    val answer: String
)

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}