package com.omondit.learnhub.data.repository

import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionResult
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.domain.model.Quiz
import com.omondit.learnhub.domain.model.QuizAnswer
import com.omondit.learnhub.domain.model.QuizAttempt
import com.omondit.learnhub.domain.model.QuizResult
import com.omondit.learnhub.domain.model.SubQuestion
import com.omondit.learnhub.domain.repository.QuestionRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepositoryImpl @Inject constructor(
    // Will add API service later
) : QuestionRepository {

    // Mock quiz questions for Linear Equations
    private val mockQuestions = listOf(
        Question(
            id = "q1",
            contentId = null,
            teacherId = null,
            classId = "form3",
            questionType = QuestionType.MCQ,
            questionText = "What is the solution to: 2x + 5 = 13?",
            subQuestions = null,
            options = listOf("x = 2", "x = 4", "x = 6", "x = 8"),
            correctAnswer = "x = 4",
            difficulty = Difficulty.EASY,
            topicId = "algebra",
            subtopicId = "linear_eq"
        ),
        Question(
            id = "q2",
            contentId = null,
            teacherId = null,
            classId = "form3",
            questionType = QuestionType.MCQ,
            questionText = "Solve for x: 3x - 7 = 8",
            subQuestions = null,
            options = listOf("x = 3", "x = 5", "x = 7", "x = 9"),
            correctAnswer = "x = 5",
            difficulty = Difficulty.EASY,
            topicId = "algebra",
            subtopicId = "linear_eq"
        ),
        Question(
            id = "q3",
            contentId = null,
            teacherId = null,
            classId = "form3",
            questionType = QuestionType.STANDALONE,
            questionText = "Solve: 5x + 10 = 35",
            subQuestions = null,
            options = null,
            correctAnswer = "x = 5",
            difficulty = Difficulty.MEDIUM,
            topicId = "algebra",
            subtopicId = "linear_eq"
        ),
        Question(
            id = "q4",
            contentId = null,
            teacherId = null,
            classId = "form3",
            questionType = QuestionType.SECTIONAL,
            questionText = "Solve the following equations:",
            subQuestions = listOf(
                SubQuestion(label = "(a)", text = "2x + 3 = 11", answer = "x = 4"),
                SubQuestion(label = "(b)", text = "4x - 5 = 15", answer = "x = 5"),
                SubQuestion(label = "(c)", text = "x/2 + 1 = 4", answer = "x = 6")
            ),
            options = null,
            correctAnswer = "", // Not used for sectional
            difficulty = Difficulty.HARD,
            topicId = "algebra",
            subtopicId = "linear_eq"
        )
    )
    private val mockTeacherQuestions = mutableListOf(
        Question(
            id = "tq1",
            contentId = null,
            teacherId = "2", // teacher@test.com
            classId = "form3",
            questionType = QuestionType.MCQ,
            questionText = "What is the coefficient of x in the equation 5x + 3 = 18?",
            subQuestions = null,
            options = listOf("3", "5", "18", "15"),
            correctAnswer = "5",
            difficulty = Difficulty.EASY,
            topicId = "algebra",
            subtopicId = "linear_eq"
        ),
        Question(
            id = "tq2",
            contentId = null,
            teacherId = "2",
            classId = "form3",
            questionType = QuestionType.STANDALONE,
            questionText = "Solve for y: 4y - 12 = 20",
            subQuestions = null,
            options = null,
            correctAnswer = "y = 8",
            difficulty = Difficulty.MEDIUM,
            topicId = "algebra",
            subtopicId = "linear_eq"
        ),
        Question(
            id = "tq3",
            contentId = null,
            teacherId = "2",
            classId = "form3",
            questionType = QuestionType.SECTIONAL,
            questionText = "Solve the following:",
            subQuestions = listOf(
                SubQuestion(label = "(a)", text = "x + 7 = 12", answer = "x = 5"),
                SubQuestion(label = "(b)", text = "3x = 21", answer = "x = 7")
            ),
            options = null,
            correctAnswer = "",
            difficulty = Difficulty.MEDIUM,
            topicId = "algebra",
            subtopicId = "linear_eq"
        )
    )

    override suspend fun getPracticeQuestions(contentId: String): Result<List<Question>> {
        delay(500)
        return Result.success(emptyList())
    }

    override suspend fun getTeacherQuestions(
        teacherId: String,
        classId: String?,
        topicId: String?,
        subtopicId: String?
    ): Result<List<Question>> {
        delay(500)

        var questions = mockTeacherQuestions.filter { it.teacherId == teacherId }

        // Apply filters
        if (classId != null) {
            questions = questions.filter { it.classId == classId }
        }
        if (topicId != null) {
            questions = questions.filter { it.topicId == topicId }
        }
        if (subtopicId != null) {
            questions = questions.filter { it.subtopicId == subtopicId }
        }

        return Result.success(questions)
    }

    override suspend fun createQuestion(question: Question): Result<Question> {
        delay(500)

        val newQuestion = question.copy(
            id = "tq_${System.currentTimeMillis()}"
        )
        mockTeacherQuestions.add(0, newQuestion)

        return Result.success(newQuestion)
    }

    override suspend fun updateQuestion(question: Question): Result<Question> {
        delay(500)
        return Result.success(question)
    }

    override suspend fun deleteQuestion(questionId: String): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }

    override suspend fun generateExamQuestions(
        classId: String,
        topicIds: List<String>,
        count: Int,
        questionTypes: List<QuestionType>,
        difficulty: Difficulty?
    ): Result<List<Question>> {
        delay(1000) // Simulate processing

        // Filter questions by criteria
        var availableQuestions = (mockQuestions + mockTeacherQuestions).filter { question ->
            question.classId == classId &&
                    topicIds.contains(question.topicId) &&
                    questionTypes.contains(question.questionType)
        }

        if (difficulty != null) {
            availableQuestions = availableQuestions.filter { it.difficulty == difficulty }
        }

        // Take random selection up to count
        val selectedQuestions = availableQuestions.shuffled().take(count)

        return Result.success(selectedQuestions)
    }

    // Quiz operations
    override suspend fun getQuizForSubtopic(subtopicId: String): Result<Quiz> {
        delay(500)

        // Return quiz for linear equations
        if (subtopicId == "linear_eq") {
            val quiz = Quiz(
                id = "quiz_linear_eq",
                subtopicId = subtopicId,
                questions = mockQuestions,
                passingScore = 70
            )
            return Result.success(quiz)
        }

        // Return empty quiz for other subtopics
        return Result.success(
            Quiz(
                id = "quiz_$subtopicId",
                subtopicId = subtopicId,
                questions = emptyList(),
                passingScore = 70
            )
        )
    }

    override suspend fun submitQuizAttempt(attempt: QuizAttempt): Result<QuizResult> {
        delay(500)

        // Get the quiz questions
        val quizResult = getQuizForSubtopic(attempt.subtopicId)

        return quizResult.fold(
            onSuccess = { quiz ->
                // Grade the quiz
                val questionResults = quiz.questions.map { question ->
                    val userAnswer = attempt.answers[question.id]
                    val isCorrect = checkAnswer(question, userAnswer)

                    QuestionResult(
                        question = question,
                        userAnswer = userAnswer,
                        isCorrect = isCorrect,
                        correctAnswer = getCorrectAnswerText(question)
                    )
                }

                val correctCount = questionResults.count { it.isCorrect }
                val score = if (quiz.questions.isNotEmpty()) {
                    (correctCount * 100) / quiz.questions.size
                } else {
                    0
                }

                val result = QuizResult(
                    score = score,
                    totalQuestions = quiz.questions.size,
                    correctAnswers = correctCount,
                    passed = score >= quiz.passingScore,
                    questionResults = questionResults
                )

                Result.success(result)
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    override suspend fun getQuizAttempts(
        userId: String,
        subtopicId: String
    ): Result<List<QuizAttempt>> {
        delay(500)
        // TODO: Implement with database
        return Result.success(emptyList())
    }

    private fun checkAnswer(question: Question, userAnswer: QuizAnswer?): Boolean {
        if (userAnswer == null) return false

        return when (question.questionType) {
            QuestionType.MCQ -> {
                val mcqAnswer = userAnswer as? QuizAnswer.MCQAnswer
                mcqAnswer?.selectedOption?.trim()?.equals(question.correctAnswer.trim(), ignoreCase = true) == true
            }

            QuestionType.STANDALONE -> {
                val standaloneAnswer = userAnswer as? QuizAnswer.StandaloneAnswer
                standaloneAnswer?.answer?.trim()?.equals(question.correctAnswer.trim(), ignoreCase = true) == true
            }

            QuestionType.SECTIONAL -> {
                val sectionalAnswer = userAnswer as? QuizAnswer.SectionalAnswer
                if (sectionalAnswer == null || question.subQuestions == null) return false

                // All sub-questions must be correct
                question.subQuestions.all { subQuestion ->
                    val userSubAnswer = sectionalAnswer.answers[subQuestion.label]
                    userSubAnswer?.trim()?.equals(subQuestion.answer.trim(), ignoreCase = true) == true
                }
            }
        }
    }

    private fun getCorrectAnswerText(question: Question): String {
        return when (question.questionType) {
            QuestionType.MCQ, QuestionType.STANDALONE -> question.correctAnswer
            QuestionType.SECTIONAL -> {
                question.subQuestions?.joinToString(", ") {
                    "${it.label} ${it.answer}"
                } ?: ""
            }
        }
    }
}
