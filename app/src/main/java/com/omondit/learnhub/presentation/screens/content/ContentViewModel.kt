package com.omondit.learnhub.presentation.screens.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.omondit.learnhub.domain.usecase.content.GetContentUseCase
import com.omondit.learnhub.domain.usecase.progress.GetContentProgressUseCase
import com.omondit.learnhub.domain.usecase.progress.MarkContentCompleteUseCase
import com.omondit.learnhub.domain.usecase.progress.UpdateSubtopicProgressUseCase
import com.omondit.learnhub.presentation.util.UiState

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val markContentCompleteUseCase: MarkContentCompleteUseCase,
    private val updateSubtopicProgressUseCase: UpdateSubtopicProgressUseCase,
    private val getContentProgressUseCase: GetContentProgressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val subtopicId: String? = savedStateHandle["subtopicId"]

    private val _contentState = MutableStateFlow<UiState<List<Content>>>(UiState.Idle)
    val contentState: StateFlow<UiState<List<Content>>> = _contentState.asStateFlow()

    private val _currentContentIndex = MutableStateFlow(0)
    val currentContentIndex: StateFlow<Int> = _currentContentIndex.asStateFlow()

    private val _currentContentCompleted = MutableStateFlow(false)
    val currentContentCompleted: StateFlow<Boolean> = _currentContentCompleted.asStateFlow()

    private val _markCompleteLoading = MutableStateFlow(false)
    val markCompleteLoading: StateFlow<Boolean> = _markCompleteLoading.asStateFlow()

    private var currentUserId: String? = null
    private var contentList: List<Content> = emptyList()

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _contentState.value = UiState.Loading

            // Validate subtopicId
            if (subtopicId == null) {
                _contentState.value = UiState.Error("Invalid subtopic ID")
                return@launch
            }

            // Get current user
            currentUserId = getCurrentUserUseCase()?.id

            val result = getContentUseCase(subtopicId)

            result.fold(
                onSuccess = { content ->
                    contentList = content
                    _contentState.value = UiState.Success(content)

                    // Check if first content is completed
                    if (content.isNotEmpty()) {
                        checkContentCompletion(content[0].id)
                    }
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
            checkContentCompletion(contentList[_currentContentIndex.value].id)
        }
    }

    fun previousContent() {
        if (_currentContentIndex.value > 0) {
            _currentContentIndex.value--
            checkContentCompletion(contentList[_currentContentIndex.value].id)
        }
    }

    fun goToContent(index: Int) {
        _currentContentIndex.value = index
        if (index < contentList.size) {
            checkContentCompletion(contentList[index].id)
        }
    }

    fun markCurrentContentComplete() {
        val userId = currentUserId ?: return
        val currentContent = contentList.getOrNull(_currentContentIndex.value) ?: return

        viewModelScope.launch {
            _markCompleteLoading.value = true

            subtopicId?.let { id ->
                val result = markContentCompleteUseCase(
                    MarkContentCompleteUseCase.Params(
                        userId = userId,
                        contentId = currentContent.id,
                        subtopicId = id
                    )
                )

                result.onSuccess {
                    _currentContentCompleted.value = true

                    // Update subtopic progress
                    val completedCount = getCompletedContentCount(userId)
                    updateSubtopicProgressUseCase(
                        UpdateSubtopicProgressUseCase.Params(
                            userId = userId,
                            subtopicId = id,
                            totalContent = contentList.size,
                            completedContent = completedCount
                        )
                    )
                }
            }

            _markCompleteLoading.value = false
        }
    }

    private fun checkContentCompletion(contentId: String) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val progress = getContentProgressUseCase(userId, contentId)
            _currentContentCompleted.value = progress?.completed == true
        }
    }

    private suspend fun getCompletedContentCount(userId: String): Int {
        return contentList.count { content ->
            getContentProgressUseCase(userId, content.id)?.completed == true
        }
    }
}

