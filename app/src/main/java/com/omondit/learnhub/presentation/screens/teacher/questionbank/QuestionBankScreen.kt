package com.omondit.learnhub.presentation.screens.teacher.questionbank

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankScreen(
    onNavigateBack: () -> Unit,
    onNavigateToForm: (String) -> Unit,
    viewModel: QuestionBankViewModel = hiltViewModel()
) {
    val questionsState by viewModel.questionsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Question Bank") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToForm("new") }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Question"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = questionsState) {
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
                        QuestionsList(
                            questions = state.data,
                            onQuestionClick = { /* TODO: View/Edit */ }
                        )
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadQuestions() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionsList(
    questions: List<Question>,
    onQuestionClick: (Question) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(questions) { question ->
            QuestionCard(question = question)
        }
    }
}

@Composable
private fun QuestionCard(
    question: Question
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Type and difficulty badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = when (question.questionType) {
                        QuestionType.MCQ -> MaterialTheme.colorScheme.primaryContainer
                        QuestionType.STANDALONE -> MaterialTheme.colorScheme.secondaryContainer
                        QuestionType.SECTIONAL -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = when (question.questionType) {
                            QuestionType.MCQ -> "MCQ"
                            QuestionType.STANDALONE -> "Open"
                            QuestionType.SECTIONAL -> "Sectional"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = when (question.difficulty) {
                        Difficulty.EASY -> MaterialTheme.colorScheme.tertiaryContainer
                        Difficulty.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
                        Difficulty.HARD -> MaterialTheme.colorScheme.errorContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = when (question.difficulty) {
                            Difficulty.EASY -> "Easy"
                            Difficulty.MEDIUM -> "Medium"
                            Difficulty.HARD -> "Hard"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question text
            Text(
                text = question.questionText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3
            )

            // Show MCQ options or sub-questions count
            when (question.questionType) {
                QuestionType.MCQ -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${question.options?.size ?: 0} options",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                QuestionType.SECTIONAL -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${question.subQuestions?.size ?: 0} sub-questions",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subject info
            Text(
                text = "📚 Form 3 • Algebra",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
            text = "❓",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Questions Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap the + button to add questions to your bank",
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
