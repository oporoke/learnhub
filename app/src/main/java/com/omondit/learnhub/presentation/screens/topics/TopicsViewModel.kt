package com.omondit.learnhub.presentation.screens.topics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.usecase.content.GetTopicsUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val getTopicsUseCase: GetTopicsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subjectId: String = checkNotNull(savedStateHandle["subjectId"])

    private val _topicsState = MutableStateFlow<UiState<List<Topic>>>(UiState.Idle)
    val topicsState: StateFlow<UiState<List<Topic>>> = _topicsState.asStateFlow()

    init {
        loadTopics()
    }

    fun loadTopics() {
        viewModelScope.launch {
            _topicsState.value = UiState.Loading

            val result = getTopicsUseCase(subjectId)

            result.fold(
                onSuccess = { topics ->
                    _topicsState.value = UiState.Success(topics)
                },
                onFailure = { exception ->
                    _topicsState.value = UiState.Error(
                        exception.message ?: "Failed to load topics"
                    )
                }
            )
        }
    }
}
