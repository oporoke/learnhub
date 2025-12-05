package com.omondit.learnhub.presentation.screens.teacher.exambuilder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.ExamType
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.presentation.screens.exambuilder.ExamBuilderViewModel
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamBuilderScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPreview: (String) -> Unit,
    viewModel: ExamBuilderViewModel = hiltViewModel()
) {
    val examType by viewModel.examType.collectAsStateWithLifecycle()
    val questionCount by viewModel.questionCount.collectAsStateWithLifecycle()
    val selectedQuestions by viewModel.selectedQuestions.collectAsStateWithLifecycle()
    val generateState by viewModel.generateState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam Builder") },
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
                .padding(16.dp)
        ) {
            if (selectedQuestions.isEmpty()) {
                // Configuration screen
                ConfigurationSection(
                    examType = examType,
                    questionCount = questionCount,
                    onExamTypeChanged = { viewModel.onExamTypeChanged(it) },
                    onQuestionCountChanged = { viewModel.onQuestionCountChanged(it) },
                    onGenerate = { viewModel.generateExam() },
                    generateState = generateState
                )
            } else {
                // Exam preview
                ExamPreviewSection(
                    examType = examType,
                    questions = selectedQuestions,
                    onRemoveQuestion = { viewModel.removeQuestion(it) },
                    onClear = { viewModel.clearExam() },
                    onExport = {
                        // TODO: Implement PDF export
                        onNavigateToPreview("exam_${System.currentTimeMillis()}")
                    }
                )
            }
        }
    }
}

@Composable
private fun ConfigurationSection(
    examType: ExamType,
    questionCount: Int,
    onExamTypeChanged: (ExamType) -> Unit,
    onQuestionCountChanged: (Int) -> Unit,
    onGenerate: () -> Unit,
    generateState: UiState<List<Question>>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Configure Exam",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exam type selector
        Text("Exam Type", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ExamType.entries.forEach { type ->
                FilterChip(
                    selected = examType == type,
                    onClick = { onExamTypeChanged(type) },
                    label = {
                        Text(when (type) {
                            ExamType.CAT -> "CAT"
                            ExamType.MIDTERM -> "Midterm"
                            ExamType.END_TERM -> "End Term"
                            ExamType.CUSTOM -> "Custom"
                        })
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question count
        Text("Number of Questions: $questionCount", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = questionCount.toFloat(),
            onValueChange = { onQuestionCountChanged(it.toInt()) },
            valueRange = 5f..20f,
            steps = 14
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📝 Auto-Generate",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Automatically select questions from your question bank",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onGenerate,
                    enabled = generateState !is UiState.Loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (generateState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Generate Exam", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (generateState is UiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (generateState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ExamPreviewSection(
    examType: ExamType,
    questions: List<Question>,
    onRemoveQuestion: (Question) -> Unit,
    onClear: () -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when (examType) {
                        ExamType.CAT -> "CAT Exam"
                        ExamType.MIDTERM -> "Midterm Exam"
                        ExamType.END_TERM -> "End Term Exam"
                        ExamType.CUSTOM -> "Custom Exam"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${questions.size} questions",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onClear) {
                Text("Clear All")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(questions) { index, question ->
                ExamQuestionCard(
                    index = index + 1,
                    question = question,
                    onRemove = { onRemoveQuestion(question) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExport,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export to PDF", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ExamQuestionCard(
    index: Int,
    question: Question,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "$index.",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = question.questionText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = when (question.questionType) {
                                QuestionType.MCQ -> "MCQ"
                                QuestionType.STANDALONE -> "Open"
                                QuestionType.SECTIONAL -> "Sectional"
                            },
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
