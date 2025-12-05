package com.omondit.learnhub.presentation.screens.teacher.lessonplanner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlanFormScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: LessonPlanFormViewModel = hiltViewModel()
) {
    val classesState by viewModel.classesState.collectAsStateWithLifecycle()
    val subjectsState by viewModel.subjectsState.collectAsStateWithLifecycle()
    val topicsState by viewModel.topicsState.collectAsStateWithLifecycle()
    val subtopicsState by viewModel.subtopicsState.collectAsStateWithLifecycle()

    val selectedClass by viewModel.selectedClass.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val selectedTopic by viewModel.selectedTopic.collectAsStateWithLifecycle()
    val selectedSubtopic by viewModel.selectedSubtopic.collectAsStateWithLifecycle()

    val objectives by viewModel.objectives.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    // Navigate back on success
    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (viewModel.isEditMode) "Edit Lesson Plan" else "New Lesson Plan")
                },
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
                text = "Lesson Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Class dropdown
            when (val state = classesState) {
                is UiState.Success -> {
                    DropdownSelector(
                        label = "Class",
                        items = state.data,
                        selectedItem = selectedClass,
                        onItemSelected = { viewModel.onClassSelected(it) },
                        itemText = { it.name }
                    )
                }
                else -> {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subject dropdown
            if (selectedClass != null) {
                when (val state = subjectsState) {
                    is UiState.Success -> {
                        DropdownSelector(
                            label = "Subject",
                            items = state.data,
                            selectedItem = selectedSubject,
                            onItemSelected = { viewModel.onSubjectSelected(it) },
                            itemText = { it.name }
                        )
                    }
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Topic dropdown
            if (selectedSubject != null) {
                when (val state = topicsState) {
                    is UiState.Success -> {
                        DropdownSelector(
                            label = "Topic",
                            items = state.data,
                            selectedItem = selectedTopic,
                            onItemSelected = { viewModel.onTopicSelected(it) },
                            itemText = { it.name }
                        )
                    }
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtopic dropdown
            if (selectedTopic != null) {
                when (val state = subtopicsState) {
                    is UiState.Success -> {
                        DropdownSelector(
                            label = "Subtopic",
                            items = state.data,
                            selectedItem = selectedSubtopic,
                            onItemSelected = { viewModel.onSubtopicSelected(it) },
                            itemText = { it.name }
                        )
                    }
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Objectives field
            OutlinedTextField(
                value = objectives,
                onValueChange = { viewModel.onObjectivesChanged(it) },
                label = { Text("Learning Objectives *") },
                placeholder = { Text("What will students learn in this lesson?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Notes field
            OutlinedTextField(
                value = notes,
                onValueChange = { viewModel.onNotesChanged(it) },
                label = { Text("Teacher Notes (Optional)") },
                placeholder = { Text("Additional notes, materials needed, etc.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = { viewModel.saveLessonPlan() },
                enabled = viewModel.isFormValid() && saveState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (saveState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Save Lesson Plan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Error message
            if (saveState is UiState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (saveState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownSelector(
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemText: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemText(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemText(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
