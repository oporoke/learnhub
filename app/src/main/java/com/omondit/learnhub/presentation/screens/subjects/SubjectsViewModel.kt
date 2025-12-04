package com.omondit.learnhub.presentation.screens.subjects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.usecase.content.GetSubjectsUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val classId: String = checkNotNull(savedStateHandle["classId"])

    private val _subjectsState = MutableStateFlow<UiState<List<Subject>>>(UiState.Idle)
    val subjectsState: StateFlow<UiState<List<Subject>>> = _subjectsState.asStateFlow()

    init {
        loadSubjects()
    }

    fun loadSubjects() {
        viewModelScope.launch {
            _subjectsState.value = UiState.Loading

            val result = getSubjectsUseCase(classId)

            result.fold(
                onSuccess = { subjects ->
                    _subjectsState.value = UiState.Success(subjects)
                },
                onFailure = { exception ->
                    _subjectsState.value = UiState.Error(
                        exception.message ?: "Failed to load subjects"
                    )
                }
            )
        }
    }
}
