package com.omondit.learnhub.presentation.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.omondit.learnhub.domain.model.Quiz
import com.omondit.learnhub.domain.model.QuizAnswer
import com.omondit.learnhub.domain.model.QuizResult
import com.omondit.learnhub.presentation.util.UiState
import com.omondit.learnhub.util.rememberHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResults: (String, Int, Int, Boolean) -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()


    // Navigate to results when quiz is submitted
    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) {
            val result = (submitState as UiState.Success<QuizResult>).data
            onNavigateToResults(
                viewModel.subtopicId,
                result.score,
                result.totalQuestions,
                result.passed
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice Quiz") },
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
            when (val state = quizState) {
                is UiState.Idle -> {
                    // Initial state
                }

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    if (state.data.questions.isEmpty()) {
                        EmptyQuizState(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        QuizContent(
                            quiz = state.data,
                            currentIndex = currentIndex,
                            viewModel = viewModel,
                            submitState = submitState
                        )
                    }
                }

                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = onNavigateBack,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizContent(
    quiz: Quiz,
    currentIndex: Int,
    viewModel: QuizViewModel,
    submitState: UiState<QuizResult>
) {
    val pagerState = rememberPagerState(
        initialPage = currentIndex,
        pageCount = { quiz.questions.size }
    )
    val haptic = rememberHapticFeedback()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.goToQuestion(pagerState.currentPage)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = { (pagerState.currentPage + 1).toFloat() / quiz.questions.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${pagerState.currentPage + 1} of ${quiz.questions.size}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Passing: ${quiz.passingScore}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Questions pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            QuestionCard(
                question = quiz.questions[page],
                viewModel = viewModel
            )
        }

        // Submit button (only on last question)
        if (pagerState.currentPage == quiz.questions.size - 1) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (!viewModel.isQuizComplete()) {
                        Text(
                            text = "⚠️ Please answer all questions before submitting",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            haptic.success()
                            viewModel.submitQuiz() },
                        enabled = viewModel.isQuizComplete() && submitState !is UiState.Loading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (submitState is UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Submit Quiz",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionCard(
    question: Question,
    viewModel: QuizViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Question type badge
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
                    Difficulty.EASY -> "🟢 Easy"
                    Difficulty.MEDIUM -> "🟡 Medium"
                    Difficulty.HARD -> "🔴 Hard"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question text
        Text(
            text = question.questionText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Answer section based on type
        when (question.questionType) {
            QuestionType.MCQ -> {
                MCQAnswerSection(question, viewModel)
            }
            QuestionType.STANDALONE -> {
                StandaloneAnswerSection(question, viewModel)
            }
            QuestionType.SECTIONAL -> {
                SectionalAnswerSection(question, viewModel)
            }
        }
    }
}

@Composable
private fun MCQAnswerSection(
    question: Question,
    viewModel: QuizViewModel
) {
    val currentAnswer = viewModel.getAnswerForQuestion(question.id) as? QuizAnswer.MCQAnswer

    question.options?.forEach { option ->
        val isSelected = currentAnswer?.selectedOption == option

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .selectable(
                    selected = isSelected,
                    onClick = { viewModel.answerMCQ(question.id, option) }
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            ),
            border = if (isSelected)
                CardDefaults.outlinedCardBorder()
            else
                null
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { viewModel.answerMCQ(question.id, option) }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = option,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun StandaloneAnswerSection(
    question: Question,
    viewModel: QuizViewModel
) {
    val currentAnswer = viewModel.getAnswerForQuestion(question.id) as? QuizAnswer.StandaloneAnswer
    var answerText by remember { mutableStateOf(currentAnswer?.answer ?: "") }

    OutlinedTextField(
        value = answerText,
        onValueChange = {
            answerText = it
            viewModel.answerStandalone(question.id, it)
        },
        label = { Text("Your Answer") },
        placeholder = { Text("Type your answer here...") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        minLines = 3
    )
}

@Composable
private fun SectionalAnswerSection(
    question: Question,
    viewModel: QuizViewModel
) {
    val currentAnswer = viewModel.getAnswerForQuestion(question.id) as? QuizAnswer.SectionalAnswer

    question.subQuestions?.forEach { subQuestion ->
        var answerText by remember {
            mutableStateOf(currentAnswer?.answers?.get(subQuestion.label) ?: "")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${subQuestion.label} ${subQuestion.text}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = answerText,
            onValueChange = {
                answerText = it
                viewModel.answerSectional(question.id, subQuestion.label, it)
            },
            label = { Text("Answer ${subQuestion.label}") },
            placeholder = { Text("Type your answer...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Composable
private fun EmptyQuizState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📝",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Quiz Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Practice questions will be added soon",
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
            Text("Go Back")
        }
    }
}
