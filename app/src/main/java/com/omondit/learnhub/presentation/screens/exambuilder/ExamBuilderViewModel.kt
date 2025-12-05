package com.omondit.learnhub.presentation.screens.exambuilder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.ExamType
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.question.GenerateExamQuestionsUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamBuilderViewModel @Inject constructor(
    private val generateExamQuestionsUseCase: GenerateExamQuestionsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _examType = MutableStateFlow(ExamType.CAT)
    val examType: StateFlow<ExamType> = _examType.asStateFlow()

    private val _questionCount = MutableStateFlow(10)
    val questionCount: StateFlow<Int> = _questionCount.asStateFlow()

    private val _selectedQuestions = MutableStateFlow<List<Question>>(emptyList())
    val selectedQuestions: StateFlow<List<Question>> = _selectedQuestions.asStateFlow()

    private val _generateState = MutableStateFlow<UiState<List<Question>>>(UiState.Idle)
    val generateState: StateFlow<UiState<List<Question>>> = _generateState.asStateFlow()

    private var currentTeacherId: String? = null

    init {
        getCurrentTeacher()
    }

    private fun getCurrentTeacher() {
        viewModelScope.launch {
            currentTeacherId = getCurrentUserUseCase()?.id
        }
    }

    fun onExamTypeChanged(type: ExamType) {
        _examType.value = type
    }

    fun onQuestionCountChanged(count: Int) {
        _questionCount.value = count
    }

    fun generateExam() {
        viewModelScope.launch {
            _generateState.value = UiState.Loading

            val result = generateExamQuestionsUseCase(
                GenerateExamQuestionsUseCase.Params(
                    classId = "form3",
                    topicIds = listOf("algebra"),
                    count = _questionCount.value,
                    questionTypes = listOf(QuestionType.MCQ, QuestionType.STANDALONE),
                    difficulty = null
                )
            )

            result.fold(
                onSuccess = { questions ->
                    _selectedQuestions.value = questions
                    _generateState.value = UiState.Success(questions)
                },
                onFailure = { exception ->
                    _generateState.value = UiState.Error(
                        exception.message ?: "Failed to generate exam"
                    )
                }
            )
        }
    }

    fun removeQuestion(question: Question) {
        _selectedQuestions.value = _selectedQuestions.value.filter { it.id != question.id }
    }

    fun clearExam() {
        _selectedQuestions.value = emptyList()
        _generateState.value = UiState.Idle
    }
}
