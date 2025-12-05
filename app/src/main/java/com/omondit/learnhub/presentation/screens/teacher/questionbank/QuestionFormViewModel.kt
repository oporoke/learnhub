package com.omondit.learnhub.presentation.screens.teacher.questionbank

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.domain.model.SubQuestion
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.content.GetClassesUseCase
import com.omondit.learnhub.domain.usecase.content.GetSubjectsUseCase
import com.omondit.learnhub.domain.usecase.content.GetSubtopicsUseCase
import com.omondit.learnhub.domain.usecase.content.GetTopicsUseCase
import com.omondit.learnhub.domain.usecase.question.CreateQuestionUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionFormViewModel @Inject constructor(
    private val getClassesUseCase: GetClassesUseCase,
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getSubtopicsUseCase: GetSubtopicsUseCase,
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questionId: String = checkNotNull(savedStateHandle["questionId"])
    val isEditMode = questionId != "new"

    // Hierarchy states
    private val _classesState = MutableStateFlow<UiState<List<CKlass>>>(UiState.Idle)
    val classesState: StateFlow<UiState<List<CKlass>>> = _classesState.asStateFlow()

    private val _subjectsState = MutableStateFlow<UiState<List<Subject>>>(UiState.Idle)
    val subjectsState: StateFlow<UiState<List<Subject>>> = _subjectsState.asStateFlow()

    private val _topicsState = MutableStateFlow<UiState<List<Topic>>>(UiState.Idle)
    val topicsState: StateFlow<UiState<List<Topic>>> = _topicsState.asStateFlow()

    private val _subtopicsState = MutableStateFlow<UiState<List<Subtopic>>>(UiState.Idle)
    val subtopicsState: StateFlow<UiState<List<Subtopic>>> = _subtopicsState.asStateFlow()

    // Selected values
    private val _selectedClass = MutableStateFlow<CKlass?>(null)
    val selectedClass: StateFlow<CKlass?> = _selectedClass.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    val selectedSubject: StateFlow<Subject?> = _selectedSubject.asStateFlow()

    private val _selectedTopic = MutableStateFlow<Topic?>(null)
    val selectedTopic: StateFlow<Topic?> = _selectedTopic.asStateFlow()

    private val _selectedSubtopic = MutableStateFlow<Subtopic?>(null)
    val selectedSubtopic: StateFlow<Subtopic?> = _selectedSubtopic.asStateFlow()

    // Question fields
    private val _questionType = MutableStateFlow(QuestionType.MCQ)
    val questionType: StateFlow<QuestionType> = _questionType.asStateFlow()

    private val _difficulty = MutableStateFlow(Difficulty.EASY)
    val difficulty: StateFlow<Difficulty> = _difficulty.asStateFlow()

    private val _questionText = MutableStateFlow("")
    val questionText: StateFlow<String> = _questionText.asStateFlow()

    // MCQ options
    private val _mcqOptions = MutableStateFlow(listOf("", "", "", ""))
    val mcqOptions: StateFlow<List<String>> = _mcqOptions.asStateFlow()

    private val _correctAnswer = MutableStateFlow("")
    val correctAnswer: StateFlow<String> = _correctAnswer.asStateFlow()

    // Sectional sub-questions
    private val _subQuestions = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val subQuestions: StateFlow<List<Pair<String, String>>> = _subQuestions.asStateFlow()

    // Save state
    private val _saveState = MutableStateFlow<UiState<Question>>(UiState.Idle)
    val saveState: StateFlow<UiState<Question>> = _saveState.asStateFlow()

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

    fun onQuestionTypeChanged(type: QuestionType) {
        _questionType.value = type
    }

    fun onDifficultyChanged(difficulty: Difficulty) {
        _difficulty.value = difficulty
    }

    fun onQuestionTextChanged(text: String) {
        _questionText.value = text
    }

    fun onMcqOptionChanged(index: Int, value: String) {
        val updatedOptions = _mcqOptions.value.toMutableList()
        updatedOptions[index] = value
        _mcqOptions.value = updatedOptions
    }

    fun onCorrectAnswerChanged(answer: String) {
        _correctAnswer.value = answer
    }

    fun addSubQuestion() {
        val label = "(${('a' + _subQuestions.value.size)})"
        _subQuestions.value = _subQuestions.value + (label to "")
    }

    fun onSubQuestionChanged(index: Int, text: String) {
        val updated = _subQuestions.value.toMutableList()
        updated[index] = updated[index].copy(second = text)
        _subQuestions.value = updated
    }

    fun removeSubQuestion(index: Int) {
        _subQuestions.value = _subQuestions.value.filterIndexed { i, _ -> i != index }
    }

    fun saveQuestion() {
        val teacherId = currentTeacherId ?: return
        val classItem = _selectedClass.value ?: return
        val topic = _selectedTopic.value ?: return
        val subtopic = _selectedSubtopic.value ?: return

        if (_questionText.value.isBlank()) {
            _saveState.value = UiState.Error("Question text is required")
            return
        }

        viewModelScope.launch {
            _saveState.value = UiState.Loading

            val question = Question(
                id = if (isEditMode) questionId else "",
                contentId = null,
                teacherId = teacherId,
                classId = classItem.id,
                questionType = _questionType.value,
                questionText = _questionText.value,
                subQuestions = if (_questionType.value == QuestionType.SECTIONAL) {
                    _subQuestions.value.map { (label, text) ->
                        SubQuestion(label = label, text = text, answer = "")
                    }
                } else null,
                options = if (_questionType.value == QuestionType.MCQ) {
                    _mcqOptions.value.filter { it.isNotBlank() }
                } else null,
                correctAnswer = _correctAnswer.value,
                difficulty = _difficulty.value,
                topicId = topic.id,
                subtopicId = subtopic.id
            )

            val result = createQuestionUseCase(question)

            _saveState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Failed to save question") }
            )
        }
    }

    fun isFormValid(): Boolean {
        return _selectedClass.value != null &&
                _selectedTopic.value != null &&
                _selectedSubtopic.value != null &&
                _questionText.value.isNotBlank()
    }
}
