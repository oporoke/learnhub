package com.omondit.learnhub.presentation.screens.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.Achievement
import com.omondit.learnhub.domain.model.LeaderboardEntry
import com.omondit.learnhub.domain.model.LeaderboardType
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.social.GetAchievementsUseCase
import com.omondit.learnhub.domain.usecase.social.GetLeaderboardUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
    private val getAchievementsUseCase: GetAchievementsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _leaderboardType = MutableStateFlow(LeaderboardType.COMPLETION)
    val leaderboardType: StateFlow<LeaderboardType> = _leaderboardType.asStateFlow()

    private val _leaderboardState = MutableStateFlow<UiState<List<LeaderboardEntry>>>(UiState.Idle)
    val leaderboardState: StateFlow<UiState<List<LeaderboardEntry>>> = _leaderboardState.asStateFlow()

    private val _achievementsState = MutableStateFlow<UiState<List<Achievement>>>(UiState.Idle)
    val achievementsState: StateFlow<UiState<List<Achievement>>> = _achievementsState.asStateFlow()

    init {
        loadLeaderboard()
        loadAchievements()
    }

    fun onLeaderboardTypeChanged(type: LeaderboardType) {
        _leaderboardType.value = type
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            _leaderboardState.value = UiState.Loading

            val result = getLeaderboardUseCase(
                GetLeaderboardUseCase.Params(
                    type = _leaderboardType.value,
                    limit = 10
                )
            )

            result.fold(
                onSuccess = { entries ->
                    _leaderboardState.value = UiState.Success(entries)
                },
                onFailure = { exception ->
                    _leaderboardState.value = UiState.Error(
                        exception.message ?: "Failed to load leaderboard"
                    )
                }
            )
        }
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            _achievementsState.value = UiState.Loading

            val userId = getCurrentUserUseCase()?.id ?: return@launch

            val result = getAchievementsUseCase(userId)

            result.fold(
                onSuccess = { achievements ->
                    _achievementsState.value = UiState.Success(achievements)
                },
                onFailure = { exception ->
                    _achievementsState.value = UiState.Error(
                        exception.message ?: "Failed to load achievements"
                    )
                }
            )
        }
    }

    fun refresh() {
        loadLeaderboard()
        loadAchievements()
    }
}
