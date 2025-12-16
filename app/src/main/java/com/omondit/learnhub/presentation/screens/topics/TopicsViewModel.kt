package com.omondit.learnhub.presentation.screens.topics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.content.GetTopicsUseCase
import com.omondit.learnhub.domain.usecase.progress.ObserveTopicProgressUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopicWithProgress(
    val topic: Topic,
    val progress: Float = 0f
)

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeTopicProgressUseCase: ObserveTopicProgressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subjectId: String? = savedStateHandle["subjectId"]

    private val _topicsState = MutableStateFlow<UiState<List<TopicWithProgress>>>(UiState.Idle)
    val topicsState: StateFlow<UiState<List<TopicWithProgress>>> = _topicsState.asStateFlow()

    private var currentUserId: String? = null
    private var topics: List<Topic> = emptyList()

    init {
        loadTopics()
    }

    fun loadTopics() {
        viewModelScope.launch {
            _topicsState.value = UiState.Loading

            // Validate subjectId
            if (subjectId == null) {
                _topicsState.value = UiState.Error("Invalid subject ID")
                return@launch
            }

            // Get current user
            currentUserId = getCurrentUserUseCase()?.id

            val result = getTopicsUseCase(subjectId)

            result.fold(
                onSuccess = { topicsList ->
                    topics = topicsList

                    val userId = currentUserId
                    if (userId != null && topicsList.isNotEmpty()) {
                        // Observe progress for all topics
                        observeTopicProgressUseCase(
                            userId = userId,
                            topicIds = topicsList.map { it.id }
                        ).collect { progressList ->
                            val topicsWithProgress = topicsList.map { topic ->
                                val progress = progressList.find { it.topicId == topic.id }
                                TopicWithProgress(
                                    topic = topic,
                                    progress = progress?.completionPercentage ?: 0f
                                )
                            }
                            _topicsState.value = UiState.Success(topicsWithProgress)
                        }
                    } else {
                        // No user or no topics, just show topics without progress
                        val topicsWithProgress = topicsList.map { TopicWithProgress(it, 0f) }
                        _topicsState.value = UiState.Success(topicsWithProgress)
                    }
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
