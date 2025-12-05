package com.omondit.learnhub.presentation.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val subtopicId: String = checkNotNull(savedStateHandle["subtopicId"])
    val score: Int = checkNotNull(savedStateHandle["score"]) { "score" }//.toInt()
    val totalQuestions: Int = checkNotNull(savedStateHandle["totalQuestions"]) { "totalQuestions" }//.toInt()
    val passed: Boolean = checkNotNull(savedStateHandle["passed"]) { "passed" }//.toBoolean()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    onNavigateBack: () -> Unit,
    onRetakeQuiz: () -> Unit,
    onContinueLearning: () -> Unit,
    viewModel: QuizResultViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Results") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Result Icon
            if (viewModel.passed) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Passed",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(120.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Failed",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Result Text
            Text(
                text = if (viewModel.passed) "Congratulations! 🎉" else "Keep Practicing! 💪",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (viewModel.passed)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (viewModel.passed)
                    "You passed the quiz!"
                else
                    "You didn't pass this time, but don't give up!",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (viewModel.passed)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Score",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${viewModel.score}%",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (viewModel.passed)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${calculateCorrectAnswers(viewModel.score, viewModel.totalQuestions)} out of ${viewModel.totalQuestions} correct",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    label = "Questions",
                    value = viewModel.totalQuestions.toString(),
                    icon = "📝"
                )

                StatCard(
                    label = "Correct",
                    value = calculateCorrectAnswers(viewModel.score, viewModel.totalQuestions).toString(),
                    icon = "✅"
                )

                StatCard(
                    label = "Status",
                    value = if (viewModel.passed) "Pass" else "Fail",
                    icon = if (viewModel.passed) "🎯" else "⚠️"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Actions
            if (!viewModel.passed) {
                Button(
                    onClick = onRetakeQuiz,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Retake Quiz",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = onContinueLearning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (viewModel.passed) "Continue Learning" else "Review Content",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Topics")
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: String
) {
    Card(
        modifier = Modifier.width(100.dp)
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun calculateCorrectAnswers(score: Int, totalQuestions: Int): Int {
    return (score * totalQuestions) / 100
}
