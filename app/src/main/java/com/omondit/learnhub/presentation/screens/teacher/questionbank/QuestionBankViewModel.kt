package com.omondit.learnhub.presentation.screens.teacher.questionbank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.question.GetTeacherQuestionsUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionBankViewModel @Inject constructor(
    private val getTeacherQuestionsUseCase: GetTeacherQuestionsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _questionsState = MutableStateFlow<UiState<List<Question>>>(UiState.Idle)
    val questionsState: StateFlow<UiState<List<Question>>> = _questionsState.asStateFlow()

    private var currentTeacherId: String? = null

    init {
        loadQuestions()
    }

    fun loadQuestions(
        classId: String? = null,
        topicId: String? = null,
        subtopicId: String? = null
    ) {
        viewModelScope.launch {
            _questionsState.value = UiState.Loading

            currentTeacherId = getCurrentUserUseCase()?.id

            val teacherId = currentTeacherId
            if (teacherId != null) {
                val result = getTeacherQuestionsUseCase(
                    GetTeacherQuestionsUseCase.Params(
                        teacherId = teacherId,
                        classId = classId,
                        topicId = topicId,
                        subtopicId = subtopicId
                    )
                )

                result.fold(
                    onSuccess = { questions ->
                        _questionsState.value = UiState.Success(questions)
                    },
                    onFailure = { exception ->
                        _questionsState.value = UiState.Error(
                            exception.message ?: "Failed to load questions"
                        )
                    }
                )
            } else {
                _questionsState.value = UiState.Error("User not logged in")
            }
        }
    }
}
