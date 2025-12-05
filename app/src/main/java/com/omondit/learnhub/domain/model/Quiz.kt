package com.omondit.learnhub.domain.model

data class Quiz(
    val id: String,
    val subtopicId: String,
    val questions: List<Question>,
    val passingScore: Int = 70 // percentage
)

data class QuizAttempt(
    val id: String = "",
    val userId: String,
    val quizId: String,
    val subtopicId: String,
    val answers: Map<String, QuizAnswer>, // questionId -> answer
    val score: Int = 0,
    val totalQuestions: Int,
    val passed: Boolean = false,
    val completedAt: Long = System.currentTimeMillis()
)

sealed class QuizAnswer {
    data class MCQAnswer(val selectedOption: String) : QuizAnswer()
    data class StandaloneAnswer(val answer: String) : QuizAnswer()
    data class SectionalAnswer(val answers: Map<String, String>) : QuizAnswer() // subQuestionLabel -> answer
}

data class QuizResult(
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val passed: Boolean,
    val questionResults: List<QuestionResult>
)

data class QuestionResult(
    val question: Question,
    val userAnswer: QuizAnswer?,
    val isCorrect: Boolean,
    val correctAnswer: String
)
