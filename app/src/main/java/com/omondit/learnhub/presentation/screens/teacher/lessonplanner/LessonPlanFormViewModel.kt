package com.omondit.learnhub.presentation.screens.teacher.lessonplanner


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.content.GetClassesUseCase
import com.omondit.learnhub.domain.usecase.content.GetSubjectsUseCase
import com.omondit.learnhub.domain.usecase.content.GetSubtopicsUseCase
import com.omondit.learnhub.domain.usecase.content.GetTopicsUseCase
import com.omondit.learnhub.domain.usecase.teacher.CreateLessonPlanUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonPlanFormViewModel @Inject constructor(
    private val getClassesUseCase: GetClassesUseCase,
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getSubtopicsUseCase: GetSubtopicsUseCase,
    private val createLessonPlanUseCase: CreateLessonPlanUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val planId: String = checkNotNull(savedStateHandle["planId"])
    val isEditMode = planId != "new"

    // Classes
    private val _classesState = MutableStateFlow<UiState<List<CKlass>>>(UiState.Idle)
    val classesState: StateFlow<UiState<List<CKlass>>> = _classesState.asStateFlow()

    // Subjects
    private val _subjectsState = MutableStateFlow<UiState<List<Subject>>>(UiState.Idle)
    val subjectsState: StateFlow<UiState<List<Subject>>> = _subjectsState.asStateFlow()

    // Topics
    private val _topicsState = MutableStateFlow<UiState<List<Topic>>>(UiState.Idle)
    val topicsState: StateFlow<UiState<List<Topic>>> = _topicsState.asStateFlow()

    // Subtopics
    private val _subtopicsState = MutableStateFlow<UiState<List<Subtopic>>>(UiState.Idle)
    val subtopicsState: StateFlow<UiState<List<Subtopic>>> = _subtopicsState.asStateFlow()

    // Form fields
    private val _selectedClass = MutableStateFlow<CKlass?>(null)
    val selectedClass: StateFlow<CKlass?> = _selectedClass.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    val selectedSubject: StateFlow<Subject?> = _selectedSubject.asStateFlow()

    private val _selectedTopic = MutableStateFlow<Topic?>(null)
    val selectedTopic: StateFlow<Topic?> = _selectedTopic.asStateFlow()

    private val _selectedSubtopic = MutableStateFlow<Subtopic?>(null)
    val selectedSubtopic: StateFlow<Subtopic?> = _selectedSubtopic.asStateFlow()

    private val _objectives = MutableStateFlow("")
    val objectives: StateFlow<String> = _objectives.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    // Save state
    private val _saveState = MutableStateFlow<UiState<LessonPlan>>(UiState.Idle)
    val saveState: StateFlow<UiState<LessonPlan>> = _saveState.asStateFlow()

    private var currentTeacherId: String? = null

    init {
        loadClasses()
        getCurrentTeacher()
    }

    private fun getCurrentTeacher() {
        viewModelScope.launch {
            currentTeacherId = getCurrentUserUseCase()?.id
        }
    }

    private fun loadClasses() {
        viewModelScope.launch {
            _classesState.value = UiState.Loading
            val result = getClassesUseCase()
            _classesState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to load classes") }
            )
        }
    }

    fun onClassSelected(classItem: CKlass) {
        _selectedClass.value = classItem
        _selectedSubject.value = null
        _selectedTopic.value = null
        _selectedSubtopic.value = null
        loadSubjects(classItem.id)
    }

    private fun loadSubjects(classId: String) {
        viewModelScope.launch {
            _subjectsState.value = UiState.Loading
            val result = getSubjectsUseCase(classId)
            _subjectsState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to load subjects") }
            )
        }
    }

    fun onSubjectSelected(subject: Subject) {
        _selectedSubject.value = subject
        _selectedTopic.value = null
        _selectedSubtopic.value = null
        loadTopics(subject.id)
    }

    private fun loadTopics(subjectId: String) {
        viewModelScope.launch {
            _topicsState.value = UiState.Loading
            val result = getTopicsUseCase(subjectId)
            _topicsState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to load topics") }
            )
        }
    }

    fun onTopicSelected(topic: Topic) {
        _selectedTopic.value = topic
        _selectedSubtopic.value = null
        loadSubtopics(topic.id)
    }

    private fun loadSubtopics(topicId: String) {
        viewModelScope.launch {
            _subtopicsState.value = UiState.Loading
            val result = getSubtopicsUseCase(topicId)
            _subtopicsState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to load subtopics") }
            )
        }
    }

    fun onSubtopicSelected(subtopic: Subtopic) {
        _selectedSubtopic.value = subtopic
    }

    fun onObjectivesChanged(text: String) {
        _objectives.value = text
    }

    fun onNotesChanged(text: String) {
        _notes.value = text
    }

    fun saveLessonPlan() {
        val teacherId = currentTeacherId ?: return
        val classItem = _selectedClass.value ?: return
        val subject = _selectedSubject.value ?: return
        val topic = _selectedTopic.value ?: return
        val subtopic = _selectedSubtopic.value ?: return

        if (_objectives.value.isBlank()) {
            _saveState.value = UiState.Error("Objectives are required")
            return
        }

        viewModelScope.launch {
            _saveState.value = UiState.Loading

            val lessonPlan = LessonPlan(
                id = if (isEditMode) planId else "",
                teacherId = teacherId,
                classId = classItem.id,
                subjectId = subject.id,
                topicId = topic.id,
                subtopicId = subtopic.id,
                objectives = _objectives.value,
                notes = _notes.value,
                createdAt = System.currentTimeMillis()
            )

            val result = createLessonPlanUseCase(lessonPlan)

            _saveState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to save lesson plan") }
            )
        }
    }

    fun isFormValid(): Boolean {
        return _selectedClass.value != null &&
                _selectedSubject.value != null &&
                _selectedTopic.value != null &&
                _selectedSubtopic.value != null &&
                _objectives.value.isNotBlank()
    }
}
