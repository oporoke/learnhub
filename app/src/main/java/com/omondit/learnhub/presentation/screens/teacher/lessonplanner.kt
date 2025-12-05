package com.omondit.learnhub.presentation.screens.teacher


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.teacher.GetLessonPlansUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlannerViewModel @Inject constructor(
    private val getLessonPlansUseCase: GetLessonPlansUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _lessonPlansState = MutableStateFlow<UiState<List<LessonPlan>>>(UiState.Idle)
    val lessonPlansState: StateFlow<UiState<List<LessonPlan>>> = _lessonPlansState.asStateFlow()

    private var currentTeacherId: String? = null

    init {
        loadLessonPlans()
    }

    fun loadLessonPlans() {
        viewModelScope.launch {
            _lessonPlansState.value = UiState.Loading

            currentTeacherId = getCurrentUserUseCase()?.id

            val teacherId = currentTeacherId
            if (teacherId != null) {
                val result = getLessonPlansUseCase(teacherId)

                result.fold(
                    onSuccess = { plans ->
                        _lessonPlansState.value = UiState.Success(plans)
                    },
                    onFailure = { exception ->
                        _lessonPlansState.value = UiState.Error(
                            exception.message ?: "Failed to load lesson plans"
                        )
                    }
                )
            } else {
                _lessonPlansState.value = UiState.Error("User not logged in")
            }
        }
    }
}
