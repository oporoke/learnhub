package com.omondit.learnhub.presentation.screens.cklasses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Curriculum
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSubjects: (String) -> Unit,
    onClassClick: (String) -> Unit,
    viewModel: ClassesViewModel = hiltViewModel()
) {
    val classesState by viewModel.classesState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Class") },
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
        PullToRefreshBox(
            isRefreshing = classesState is UiState.Loading,
            onRefresh = { viewModel.loadClasses() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val state = classesState) {
                    is UiState.Idle -> {
                        // Should not happen since we load in init
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
                            ClassesList(
                                classes = state.data,
                                onClassClick = onClassClick
                            )
                        }
                    }

                    is UiState.Error -> {
                        ErrorState(
                            message = state.message,
                            onRetry = { viewModel.loadClasses() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ClassesList(
    classes: List<CKlass>,
    onClassClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(classes) { classItem ->
            ClassCard(
                classItem = classItem,
                onClick = { onClassClick(classItem.id) }
            )
        }
    }
}

@Composable
private fun ClassCard(
    classItem: CKlass,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = classItem.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (classItem.curriculum) {
                    Curriculum.CURRICULUM_844 -> "8-4-4 Curriculum"
                    Curriculum.CBC -> "CBC Curriculum"
                },
                fontSize = 14.sp,
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
            text = "No Classes Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please check back later",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
