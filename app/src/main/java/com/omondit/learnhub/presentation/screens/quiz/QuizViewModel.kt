package com.omondit.learnhub.presentation.screens.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Quiz
import com.omondit.learnhub.domain.model.QuizAnswer
import com.omondit.learnhub.domain.model.QuizAttempt
import com.omondit.learnhub.domain.model.QuizResult
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.quiz.GetQuizForSubtopicUseCase
import com.omondit.learnhub.domain.usecase.quiz.SubmitQuizAttemptUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizForSubtopicUseCase: GetQuizForSubtopicUseCase,
    private val submitQuizAttemptUseCase: SubmitQuizAttemptUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val subtopicId: String? = savedStateHandle["subtopicId"]

    private val _quizState = MutableStateFlow<UiState<Quiz>>(UiState.Idle)
    val quizState: StateFlow<UiState<Quiz>> = _quizState.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _answers = MutableStateFlow<Map<String, QuizAnswer>>(emptyMap())
    val answers: StateFlow<Map<String, QuizAnswer>> = _answers.asStateFlow()

    private val _submitState = MutableStateFlow<UiState<QuizResult>>(UiState.Idle)
    val submitState: StateFlow<UiState<QuizResult>> = _submitState.asStateFlow()

    private var currentUserId: String? = null
    private var quiz: Quiz? = null

    init {
        loadQuiz()
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            _quizState.value = UiState.Loading

            // Validate subtopicId
            if (subtopicId == null) {
                _quizState.value = UiState.Error("Invalid subtopic ID")
                return@launch
            }

            currentUserId = getCurrentUserUseCase()?.id

            val result = getQuizForSubtopicUseCase(subtopicId)

            result.fold(
                onSuccess = { loadedQuiz ->
                    quiz = loadedQuiz
                    _quizState.value = UiState.Success(loadedQuiz)
                },
                onFailure = { exception ->
                    _quizState.value = UiState.Error(
                        exception.message ?: "Failed to load quiz"
                    )
                }
            )
        }
    }

    fun nextQuestion(totalQuestions: Int) {
        if (_currentQuestionIndex.value < totalQuestions - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }

    fun goToQuestion(index: Int) {
        _currentQuestionIndex.value = index
    }

    fun answerMCQ(questionId: String, selectedOption: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionId] = QuizAnswer.MCQAnswer(selectedOption)
        _answers.value = currentAnswers
    }

    fun answerStandalone(questionId: String, answer: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionId] = QuizAnswer.StandaloneAnswer(answer)
        _answers.value = currentAnswers
    }

    fun answerSectional(questionId: String, subQuestionLabel: String, answer: String) {
        val currentAnswers = _answers.value.toMutableMap()
        val existingSectional = currentAnswers[questionId] as? QuizAnswer.SectionalAnswer

        val updatedSectionalAnswers = (existingSectional?.answers ?: emptyMap()).toMutableMap()
        updatedSectionalAnswers[subQuestionLabel] = answer

        currentAnswers[questionId] = QuizAnswer.SectionalAnswer(updatedSectionalAnswers)
        _answers.value = currentAnswers
    }

    fun submitQuiz() {
        val userId = currentUserId ?: return
        val currentQuiz = quiz ?: return

        viewModelScope.launch {
            _submitState.value = UiState.Loading

            subtopicId?.let { id ->
                val attempt = QuizAttempt(
                    userId = userId,
                    quizId = currentQuiz.id,
                    subtopicId = id,
                    answers = _answers.value,
                    totalQuestions = currentQuiz.questions.size
                )

                val result = submitQuizAttemptUseCase(attempt)

                result.fold(
                    onSuccess = { quizResult ->
                        _submitState.value = UiState.Success(quizResult)
                    },
                    onFailure = { exception ->
                        _submitState.value = UiState.Error(
                            exception.message ?: "Failed to submit quiz"
                        )
                    }
                )
            }
        }
    }

    fun getAnswerForQuestion(questionId: String): QuizAnswer? {
        return _answers.value[questionId]
    }

    fun isQuizComplete(): Boolean {
        val currentQuiz = quiz ?: return false
        return _answers.value.size == currentQuiz.questions.size
    }
}
