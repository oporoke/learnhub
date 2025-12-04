package com.omondit.learnhub.presentation.screens.subtopics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.usecase.content.GetSubtopicsUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    private val getSubtopicsUseCase: GetSubtopicsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val topicId: String = checkNotNull(savedStateHandle["topicId"])

    private val _subtopicsState = MutableStateFlow<UiState<List<Subtopic>>>(UiState.Idle)
    val subtopicsState: StateFlow<UiState<List<Subtopic>>> = _subtopicsState.asStateFlow()

    init {
        loadSubtopics()
    }

    fun loadSubtopics() {
        viewModelScope.launch {
            _subtopicsState.value = UiState.Loading

            val result = getSubtopicsUseCase(topicId)

            result.fold(
                onSuccess = { subtopics ->
                    _subtopicsState.value = UiState.Success(subtopics)
                },
                onFailure = { exception ->
                    _subtopicsState.value = UiState.Error(
                        exception.message ?: "Failed to load subtopics"
                    )
                }
            )
        }
    }
}
