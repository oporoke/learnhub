package com.omondit.learnhub.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.StudentAnalytics
import com.omondit.learnhub.domain.usecase.analytics.GetStudentAnalyticsUseCase
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getStudentAnalyticsUseCase: GetStudentAnalyticsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _analyticsState = MutableStateFlow<UiState<StudentAnalytics>>(UiState.Idle)
    val analyticsState: StateFlow<UiState<StudentAnalytics>> = _analyticsState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _analyticsState.value = UiState.Loading

            val userId = getCurrentUserUseCase()?.id

            if (userId != null) {
                val result = getStudentAnalyticsUseCase(userId)

                result.fold(
                    onSuccess = { analytics ->
                        _analyticsState.value = UiState.Success(analytics)
                    },
                    onFailure = { exception ->
                        _analyticsState.value = UiState.Error(
                            exception.message ?: "Failed to load analytics"
                        )
                    }
                )
            } else {
                _analyticsState.value = UiState.Error("User not logged in")
            }
        }
    }
}
