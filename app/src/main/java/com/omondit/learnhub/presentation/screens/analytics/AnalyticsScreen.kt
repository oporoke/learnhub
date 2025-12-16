package com.omondit.learnhub.presentation.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.omondit.learnhub.domain.model.StudentAnalytics
import com.omondit.learnhub.domain.model.TopicAnalytics
import com.omondit.learnhub.presentation.util.UiState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val analyticsState by viewModel.analyticsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Progress") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = analyticsState) {
                is UiState.Idle -> {
                    // Initial state
                }

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    AnalyticsContent(analytics = state.data)
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadAnalytics() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyticsContent(analytics: StudentAnalytics) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overview stats
        item {
            Text(
                text = "Overview",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Study Streak",
                    value = "${analytics.studyStreak}",
                    subtitle = "days",
                    modifier = Modifier.weight(1f),
                    icon = "🔥"
                )

                StatCard(
                    title = "Study Time",
                    value = "${analytics.totalStudyTimeMinutes}",
                    subtitle = "minutes",
                    modifier = Modifier.weight(1f),
                    icon = "⏱️"
                )
            }
        }

        // Progress cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressCard(
                    title = "Topics",
                    completed = analytics.completedTopics,
                    total = analytics.totalTopics,
                    modifier = Modifier.weight(1f)
                )

                ProgressCard(
                    title = "Content",
                    completed = analytics.completedContent,
                    total = analytics.totalContent,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Overall completion
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Overall Completion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val overallProgress = if (analytics.totalContent > 0) {
                        (analytics.completedContent.toFloat() / analytics.totalContent * 100)
                    } else 0f

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { overallProgress / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .height(12.dp),
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${overallProgress.roundToInt()}%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Topic breakdown
        item {
            Text(
                text = "Topics Progress",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(analytics.topicProgress) { topicAnalytics ->
            TopicProgressCard(topicAnalytics = topicAnalytics)
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun ProgressCard(
    title: String,
    completed: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$completed",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "of $total",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TopicProgressCard(topicAnalytics: TopicAnalytics) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topicAnalytics.topicName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${topicAnalytics.contentCompleted}/${topicAnalytics.contentTotal} lessons",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${topicAnalytics.completionPercentage.roundToInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { topicAnalytics.completionPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

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
