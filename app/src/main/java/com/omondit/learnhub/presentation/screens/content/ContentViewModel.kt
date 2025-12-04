package com.omondit.learnhub.presentation.screens.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.omondit.learnhub.domain.usecase.content.GetContentUseCase
import com.omondit.learnhub.presentation.util.UiState

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subtopicId: String = checkNotNull(savedStateHandle["subtopicId"])

    private val _contentState = MutableStateFlow<UiState<List<Content>>>(UiState.Idle)
    val contentState: StateFlow<UiState<List<Content>>> = _contentState.asStateFlow()

    private val _currentContentIndex = MutableStateFlow(0)
    val currentContentIndex: StateFlow<Int> = _currentContentIndex.asStateFlow()

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _contentState.value = UiState.Loading

            val result = getContentUseCase(subtopicId)

            result.fold(
                onSuccess = { content ->
                    _contentState.value = UiState.Success(content)
                },
                onFailure = { exception ->
                    _contentState.value = UiState.Error(
                        exception.message ?: "Failed to load content"
                    )
                }
            )
        }
    }

    fun nextContent(totalContent: Int) {
        if (_currentContentIndex.value < totalContent - 1) {
            _currentContentIndex.value++
        }
    }

    fun previousContent() {
        if (_currentContentIndex.value > 0) {
            _currentContentIndex.value--
        }
    }

    fun goToContent(index: Int) {
        _currentContentIndex.value = index
    }
}
