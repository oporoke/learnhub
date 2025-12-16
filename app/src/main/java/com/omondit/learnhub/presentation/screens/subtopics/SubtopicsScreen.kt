package com.omondit.learnhub.presentation.screens.subtopics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircle
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
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.presentation.util.UiState
import com.omondit.learnhub.util.rememberHapticFeedback
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtopicsScreen(
    onNavigateBack: () -> Unit,
    onSubtopicClick: (String) -> Unit,
    viewModel: SubtopicsViewModel = hiltViewModel()
) {
    val subtopicsState by viewModel.subtopicsState.collectAsStateWithLifecycle()
    val haptic = rememberHapticFeedback()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Subtopic") },
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
            when (val state = subtopicsState) {
                is UiState.Idle -> {
                    // Initial state
                }

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyState(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        val bookmarked by viewModel.bookmarkedSubtopics.collectAsStateWithLifecycle()

                        SubtopicsList(
                            subtopics = state.data,
                            bookmarkedSubtopics = bookmarked,
                            onSubtopicClick = { onSubtopicClick(it) },
                            onBookmarkClick = { subtopic ->
                                haptic.click()
                                viewModel.toggleBookmark(
                                    subtopic.subtopic.id,
                                    subtopic.subtopic.name,
                                    subtopic.subtopic.description
                                )
                            }
                        )
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadSubtopics() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun SubtopicsList(
    subtopics: List<SubtopicWithProgress>,
    bookmarkedSubtopics: Set<String>,
    onSubtopicClick: (String) -> Unit,
    onBookmarkClick: (SubtopicWithProgress) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(subtopics) { subtopicWithProgress ->
            SubtopicCard(
                subtopicWithProgress = subtopicWithProgress,
                isBookmarked = bookmarkedSubtopics.contains(subtopicWithProgress.subtopic.id),
                onClick = { onSubtopicClick(subtopicWithProgress.subtopic.id) },
                onBookmarkClick = {
                    onBookmarkClick(subtopicWithProgress)
                }
            )
        }
    }
}

@Composable
private fun SubtopicCard(
    subtopicWithProgress: SubtopicWithProgress,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (subtopicWithProgress.progress >= 100f)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play icon or checkmark
            Icon(
                imageVector = if (subtopicWithProgress.progress >= 100f)
                    Icons.Default.CheckCircle
                else
                    Icons.Default.PlayCircle,
                contentDescription = if (subtopicWithProgress.progress >= 100f)
                    "Completed"
                else
                    "Start learning",
                tint = if (subtopicWithProgress.progress >= 100f)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Subtopic info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subtopicWithProgress.subtopic.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f)
                    )

                    if (subtopicWithProgress.progress > 0f) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "${subtopicWithProgress.progress.roundToInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtopicWithProgress.subtopic.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )

                if (subtopicWithProgress.progress > 0f && subtopicWithProgress.progress < 100f) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { subtopicWithProgress.progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Bookmark button
            IconButton(
                onClick = onBookmarkClick
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Subtopics Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Subtopics will appear here once added",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
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
