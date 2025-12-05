package com.omondit.learnhub.presentation.screens.teacher.questionbank

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionFormScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: QuestionFormViewModel = hiltViewModel()
) {
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    val questionText by viewModel.questionText.collectAsStateWithLifecycle()
    val questionType by viewModel.questionType.collectAsStateWithLifecycle()
    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()
    val correctAnswer by viewModel.correctAnswer.collectAsStateWithLifecycle()

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Question") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Question Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question type selector
            Text("Question Type", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = questionType == QuestionType.MCQ,
                    onClick = { viewModel.onQuestionTypeChanged(QuestionType.MCQ) },
                    label = { Text("MCQ") }
                )
                FilterChip(
                    selected = questionType == QuestionType.STANDALONE,
                    onClick = { viewModel.onQuestionTypeChanged(QuestionType.STANDALONE) },
                    label = { Text("Open") }
                )
                FilterChip(
                    selected = questionType == QuestionType.SECTIONAL,
                    onClick = { viewModel.onQuestionTypeChanged(QuestionType.SECTIONAL) },
                    label = { Text("Sectional") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty selector
            Text("Difficulty", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = difficulty == Difficulty.EASY,
                    onClick = { viewModel.onDifficultyChanged(Difficulty.EASY) },
                    label = { Text("Easy") }
                )
                FilterChip(
                    selected = difficulty == Difficulty.MEDIUM,
                    onClick = { viewModel.onDifficultyChanged(Difficulty.MEDIUM) },
                    label = { Text("Medium") }
                )
                FilterChip(
                    selected = difficulty == Difficulty.HARD,
                    onClick = { viewModel.onDifficultyChanged(Difficulty.HARD) },
                    label = { Text("Hard") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question text
            OutlinedTextField(
                value = questionText,
                onValueChange = { viewModel.onQuestionTextChanged(it) },
                label = { Text("Question Text *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Answer field (simplified - just text input)
            OutlinedTextField(
                value = correctAnswer,
                onValueChange = { viewModel.onCorrectAnswerChanged(it) },
                label = { Text("Correct Answer *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = { viewModel.saveQuestion() },
                enabled = viewModel.isFormValid() && saveState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (saveState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Question", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (saveState is UiState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (saveState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Note: Full hierarchy selection coming in next iteration. For now, questions default to Form 3 Algebra.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
