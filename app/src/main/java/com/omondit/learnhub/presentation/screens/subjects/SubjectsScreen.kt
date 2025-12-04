package com.omondit.learnhub.presentation.screens.subjects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.presentation.util.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    onNavigateBack: () -> Unit,
    onSubjectClick: (String) -> Unit,
    viewModel: SubjectsViewModel = hiltViewModel()
) {
    val subjectsState by viewModel.subjectsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Subject") },
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
            when (val state = subjectsState) {
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
                        SubjectsGrid(
                            subjects = state.data,
                            onSubjectClick = onSubjectClick
                        )
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadSubjects() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectsGrid(
    subjects: List<Subject>,
    onSubjectClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(subjects) { subject ->
            SubjectCard(
                subject = subject,
                onClick = { onSubjectClick(subject.id) }
            )
        }
    }
}

@Composable
private fun SubjectCard(
    subject: Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = getSubjectColor(subject.name)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getSubjectEmoji(subject.name),
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subject.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun getSubjectColor(subjectName: String): androidx.compose.ui.graphics.Color {
    return when (subjectName.lowercase()) {
        "mathematics" -> MaterialTheme.colorScheme.primaryContainer
        "chemistry" -> MaterialTheme.colorScheme.secondaryContainer
        "biology" -> MaterialTheme.colorScheme.tertiaryContainer
        "physics" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

private fun getSubjectEmoji(subjectName: String): String {
    return when (subjectName.lowercase()) {
        "mathematics" -> "🔢"
        "chemistry" -> "🧪"
        "biology" -> "🧬"
        "physics" -> "⚛️"
        else -> "📚"
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
            text = "No Subjects Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Subjects will appear here once added",
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
