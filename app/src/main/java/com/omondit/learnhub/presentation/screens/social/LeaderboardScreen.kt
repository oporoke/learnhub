package com.omondit.learnhub.presentation.screens.social

import com.omondit.learnhub.domain.model.Achievement
import com.omondit.learnhub.domain.model.LeaderboardEntry
import com.omondit.learnhub.domain.model.LeaderboardType
import com.omondit.learnhub.presentation.util.UiState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val leaderboardType by viewModel.leaderboardType.collectAsStateWithLifecycle()
    val leaderboardState by viewModel.leaderboardState.collectAsStateWithLifecycle()
    val achievementsState by viewModel.achievementsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard & Achievements") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Leaderboard type selector
            item {
                Text(
                    text = "Leaderboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LeaderboardType.entries.forEach { type ->
                        FilterChip(
                            selected = leaderboardType == type,
                            onClick = { viewModel.onLeaderboardTypeChanged(type) },
                            label = {
                                Text(when (type) {
                                    LeaderboardType.COMPLETION -> "Completion"
                                    LeaderboardType.STREAK -> "Streak"
                                    LeaderboardType.WEEKLY -> "This Week"
                                })
                            }
                        )
                    }
                }
            }

            // Leaderboard content
            when (val state = leaderboardState) {
                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is UiState.Success -> {
                    itemsIndexed(state.data) { index, entry ->
                        LeaderboardCard(
                            entry = entry,
                            leaderboardType = leaderboardType
                        )
                    }
                }

                is UiState.Error -> {
                    item {
                        ErrorState(
                            message = state.message,
                            onRetry = { viewModel.refresh() }
                        )
                    }
                }

                else -> {}
            }

            // Achievements section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "My Achievements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            when (val state = achievementsState) {
                is UiState.Success -> {
                    items(state.data) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun LeaderboardCard(
    entry: LeaderboardEntry,
    leaderboardType: LeaderboardType
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (entry.rank) {
                1 -> MaterialTheme.colorScheme.primaryContainer
                2 -> MaterialTheme.colorScheme.secondaryContainer
                3 -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = when (entry.rank) {
                        1 -> "🥇"
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "#${entry.rank}"
                    },
                    fontSize = if (entry.rank <= 3) 28.sp else 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = when (leaderboardType) {
                        LeaderboardType.COMPLETION -> "${entry.completedContent} lessons completed"
                        LeaderboardType.STREAK -> "${entry.streak} day streak 🔥"
                        LeaderboardType.WEEKLY -> "${entry.score} points this week"
                    },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Score
            Text(
                text = "${entry.score}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = achievement.icon,
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = achievement.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (achievement.isUnlocked) {
                Text(
                    text = "✓",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "🔒",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
