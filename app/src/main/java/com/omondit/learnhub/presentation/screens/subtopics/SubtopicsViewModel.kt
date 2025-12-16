package com.omondit.learnhub.presentation.screens.subtopics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.BookmarkType
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.bookmark.IsBookmarkedUseCase
import com.omondit.learnhub.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.omondit.learnhub.domain.usecase.content.GetSubtopicsUseCase
import com.omondit.learnhub.domain.usecase.progress.ObserveSubtopicProgressUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SubtopicWithProgress(
    val subtopic: Subtopic,
    val progress: Float = 0f
)

//private val toggleBookmarkUseCase: ToggleBookmarkUseCase
//private val isBookmarkedUseCase: IsBookmarkedUseCase

@HiltViewModel
class SubtopicsViewModel @Inject constructor(
    private val getSubtopicsUseCase: GetSubtopicsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeSubtopicProgressUseCase: ObserveSubtopicProgressUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val isBookmarkedUseCase: IsBookmarkedUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _bookmarkedSubtopics = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedSubtopics: StateFlow<Set<String>> = _bookmarkedSubtopics.asStateFlow()

    fun toggleBookmark(subtopicId: String, title: String, description: String) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val result = toggleBookmarkUseCase(
                ToggleBookmarkUseCase.Params(
                    userId = userId,
                    itemType = BookmarkType.SUBTOPIC,
                    itemId = subtopicId,
                    itemTitle = title,
                    itemDescription = description
                )
            )

            result.fold(
                onSuccess = { isBookmarked ->
                    if (isBookmarked) {
                        _bookmarkedSubtopics.value = _bookmarkedSubtopics.value + subtopicId
                    } else {
                        _bookmarkedSubtopics.value = _bookmarkedSubtopics.value - subtopicId
                    }
                },
                onFailure = { /* Handle error */ }
            )
        }
    }

    private fun checkBookmarks(subtopics: List<Subtopic>) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val bookmarked = mutableSetOf<String>()
            subtopics.forEach { subtopic ->
                if (isBookmarkedUseCase(userId, BookmarkType.SUBTOPIC, subtopic.id)) {
                    bookmarked.add(subtopic.id)
                }
            }
            _bookmarkedSubtopics.value = bookmarked
        }
    }
    private val topicId: String = checkNotNull(savedStateHandle["topicId"])

    private val _subtopicsState = MutableStateFlow<UiState<List<SubtopicWithProgress>>>(UiState.Idle)
    val subtopicsState: StateFlow<UiState<List<SubtopicWithProgress>>> = _subtopicsState.asStateFlow()

    private var currentUserId: String? = null

    init {
        loadSubtopics()
    }

    fun loadSubtopics() {
        viewModelScope.launch {
            _subtopicsState.value = UiState.Loading

            // Get current user
            currentUserId = getCurrentUserUseCase()?.id

            val result = getSubtopicsUseCase(topicId)

            result.fold(
                onSuccess = { subtopicsList ->
                    val userId = currentUserId
                    if (userId != null && subtopicsList.isNotEmpty()) {
                        // Observe progress for all subtopics
                        checkBookmarks(subtopicsList)
                        observeSubtopicProgressUseCase(
                            userId = userId,
                            subtopicIds = subtopicsList.map { it.id }
                        ).collect { progressList ->
                            val subtopicsWithProgress = subtopicsList.map { subtopic ->
                                val progress = progressList.find { it.subtopicId == subtopic.id }
                                SubtopicWithProgress(
                                    subtopic = subtopic,
                                    progress = progress?.completionPercentage ?: 0f
                                )
                            }
                            _subtopicsState.value = UiState.Success(subtopicsWithProgress)
                        }
                    } else {
                        // No user or no subtopics, just show subtopics without progress
                        val subtopicsWithProgress = subtopicsList.map { SubtopicWithProgress(it, 0f) }
                        _subtopicsState.value = UiState.Success(subtopicsWithProgress)
                    }
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
