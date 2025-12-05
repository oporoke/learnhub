# 📚 COMPLETE DOCUMENTATION SUITE (Continued)

---

## 4️⃣ CODE DOCUMENTATION

Path: `docs/04_CODE_DOCUMENTATION.md`

```markdown
# LearnHub Kenya - Code Documentation

## Table of Contents
1. [Project Structure](#project-structure)
2. [Coding Standards](#coding-standards)
3. [Module Documentation](#module-documentation)
4. [Key Classes & Interfaces](#key-classes--interfaces)
5. [Utilities & Helpers](#utilities--helpers)
6. [Extension Functions](#extension-functions)
7. [Custom Components](#custom-components)
8. [State Management](#state-management)
9. [Navigation](#navigation)
10. [Database Operations](#database-operations)

---

## Project Structure

### Overview

```
app/src/main/java/com/learnhub/kenya/
├── LearnHubApplication.kt          # Application class (Hilt setup)
├── MainActivity.kt                 # Single activity host
│
├── di/                            # Dependency Injection
│   ├── DatabaseModule.kt          # Database dependencies
│   ├── NetworkModule.kt           # Network dependencies
│   └── RepositoryModule.kt        # Repository bindings
│
├── domain/                        # Business Logic Layer
│   ├── model/                     # Domain models
│   │   ├── User.kt
│   │   ├── Content.kt
│   │   ├── Question.kt
│   │   ├── Progress.kt
│   │   ├── Analytics.kt
│   │   ├── Social.kt
│   │   └── Bookmark.kt
│   ├── repository/                # Repository interfaces
│   │   ├── AuthRepository.kt
│   │   ├── ContentRepository.kt
│   │   ├── QuestionRepository.kt
│   │   ├── ProgressRepository.kt
│   │   ├── TeacherRepository.kt
│   │   ├── BookmarkRepository.kt
│   │   ├── SearchRepository.kt
│   │   ├── AnalyticsRepository.kt
│   │   └── SocialRepository.kt
│   └── usecase/                   # Business operations
│       ├── BaseUseCase.kt
│       ├── auth/
│       ├── content/
│       ├── progress/
│       ├── quiz/
│       ├── teacher/
│       ├── bookmark/
│       ├── search/
│       ├── analytics/
│       └── social/
│
├── data/                          # Data Layer
│   ├── repository/                # Repository implementations
│   │   ├── *RepositoryImpl.kt
│   │   └── Mock*.kt
│   ├── local/                     # Local database
│   │   ├── LearnHubDatabase.kt
│   │   ├── dao/
│   │   ├── entity/
│   │   └── mapper/
│   ├── remote/                    # API services (future)
│   │   ├── api/
│   │   └── dto/
│   ├── preferences/               # DataStore
│   │   ├── ThemePreferences.kt
│   │   └── NotificationPreferences.kt
│   └── notification/
│       └── NotificationHelper.kt
│
├── presentation/                  # UI Layer
│   ├── screens/                   # All screens
│   │   ├── splash/
│   │   ├── login/
│   │   ├── register/
│   │   ├── home/
│   │   ├── classes/
│   │   ├── subjects/
│   │   ├── topics/
│   │   ├── subtopics/
│   │   ├── content/
│   │   ├── quiz/
│   │   ├── search/
│   │   ├── analytics/
│   │   ├── settings/
│   │   ├── social/
│   │   └── teacher/
│   ├── components/                # Reusable UI components
│   │   ├── AsyncImageLoader.kt
│   │   ├── VideoPlayerComposable.kt
│   │   ├── PdfViewerComposable.kt
│   │   ├── ShimmerEffect.kt
│   │   └── EmptyStateComponent.kt
│   ├── navigation/                # Navigation setup
│   │   ├── NavGraph.kt
│   │   └── Screen.kt
│   └── util/                      # UI utilities
│       ├── UiState.kt
│       └── HapticFeedback.kt
│
└── ui/                            # Theme
└── theme/
├── Color.kt
├── Theme.kt
└── Type.kt
```

---

## Coding Standards

### Naming Conventions

#### Classes
```kotlin
// PascalCase for classes
class LoginViewModel : ViewModel()
class ContentRepository : Repository
class UserEntity

// Suffix conventions
*Screen.kt       // Composable screens
*ViewModel.kt    // ViewModels
*UseCase.kt      // Use cases
*Repository.kt   // Repository interfaces
*RepositoryImpl.kt  // Repository implementations
*Entity.kt       // Database entities
*Dao.kt          // Data Access Objects
```

#### Functions & Variables
```kotlin
// camelCase for functions and variables
fun loadContent()
val userName: String
private val _state = MutableStateFlow<UiState>()

// Boolean prefixes
val isLoading: Boolean
val hasError: Boolean
fun isValid(): Boolean
```

#### Constants
```kotlin
// UPPER_SNAKE_CASE for constants
companion object {
    private const val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L
    private const val MAX_RETRY_ATTEMPTS = 3
    const val DEFAULT_PAGE_SIZE = 20
}
```

#### Packages
```kotlin
// lowercase, no underscores
com.learnhub.kenya.domain.model
com.learnhub.kenya.presentation.screens.home
```

### Code Organization

#### File Structure
```kotlin
// 1. Package declaration
package com.learnhub.kenya.presentation.screens.home

// 2. Imports (organized by Android, Third-party, Project)
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.learnhub.kenya.domain.model.User

// 3. File-level constants
private const val TAG = "HomeScreen"

// 4. Main class/function
@Composable
fun HomeScreen(
    onNavigateToClasses: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Implementation
}

// 5. Private helper functions/composables
@Composable
private fun StudentHomeContent(/* params */) {
    // Implementation
}
```

#### Class Structure
```kotlin
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // 1. Companion object (if needed)
    companion object {
        private const val SUBTOPIC_ID = "subtopicId"
    }
    
    // 2. Private mutable state
    private val _contentState = MutableStateFlow<UiState<List<Content>>>(UiState.Idle)
    
    // 3. Public immutable state
    val contentState: StateFlow<UiState<List<Content>>> = _contentState.asStateFlow()
    
    // 4. Init block
    init {
        loadContent()
    }
    
    // 5. Public functions
    fun loadContent() {
        // Implementation
    }
    
    fun refreshContent() {
        // Implementation
    }
    
    // 6. Private functions
    private fun handleError(error: Throwable) {
        // Implementation
    }
}
```

### Documentation Standards

#### Function Documentation
```kotlin
/**
 * Loads content for a specific subtopic.
 * 
 * This function fetches content from the repository and updates the UI state.
 * It handles loading states, success, and error scenarios.
 * 
 * @param subtopicId The unique identifier of the subtopic
 * @return Flow of content list wrapped in UiState
 * @throws NetworkException if network request fails
 * 
 * @see ContentRepository.getContent
 * @sample sampleUsage
 */
suspend fun loadContent(subtopicId: String): Flow<UiState<List<Content>>> {
    // Implementation
}

private fun sampleUsage() {
    viewModelScope.launch {
        loadContent("linear_eq").collect { state ->
            when (state) {
                is UiState.Success -> println("Loaded ${state.data.size} items")
                is UiState.Error -> println("Error: ${state.message}")
                else -> Unit
            }
        }
    }
}
```

#### Class Documentation
```kotlin
/**
 * ViewModel for managing content display and interaction.
 * 
 * This ViewModel handles:
 * - Loading content for a subtopic
 * - Managing swipe navigation between content items
 * - Tracking content completion progress
 * - Coordinating with quiz navigation
 * 
 * **Architecture:**
 * - Follows MVVM pattern
 * - Uses Clean Architecture use cases
 * - Implements reactive state with StateFlow
 * 
 * **State Management:**
 * - contentState: Current content loading state
 * - currentIndex: Index of displayed content item
 * - completedContent: Set of completed content IDs
 * 
 * @property getContentUseCase Use case for fetching content
 * @property markCompleteUseCase Use case for marking content complete
 * @constructor Creates a ViewModel with subtopicId from SavedStateHandle
 * 
 * @see BaseViewModel
 * @see UiState
 */
@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val markCompleteUseCase: MarkContentCompleteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Implementation
}
```

#### Complex Logic Documentation
```kotlin
/**
 * Calculates topic completion percentage based on subtopic progress.
 * 
 * Algorithm:
 * 1. Fetch all subtopics for the topic
 * 2. Count completed subtopics (progress >= 100%)
 * 3. Calculate percentage: (completed / total) * 100
 * 4. Cache result for 5 minutes
 * 
 * **Performance:**
 * - Time Complexity: O(n) where n = number of subtopics
 * - Space Complexity: O(1) constant space
 * - Cached for 5 minutes to reduce DB queries
 * 
 * **Edge Cases:**
 * - Returns 0% if no subtopics exist
 * - Returns 100% if all subtopics complete
 * - Handles null/missing progress gracefully
 * 
 * @param topicId The topic identifier
 * @return Completion percentage (0.0 to 100.0)
 */
private suspend fun calculateTopicProgress(topicId: String): Float {
    val subtopics = progressDao.getSubtopicProgressByTopic(topicId)
    if (subtopics.isEmpty()) return 0f
    
    val completed = subtopics.count { it.completionPercentage >= 100f }
    return (completed.toFloat() / subtopics.size) * 100f
}
```

---

## Module Documentation

### Domain Layer

#### BaseUseCase

**Purpose**: Abstract base for all use cases with consistent Result handling

**File**: `domain/usecase/BaseUseCase.kt`

```kotlin
/**
 * Base class for all use cases in the application.
 * 
 * Provides a consistent interface for executing business logic operations
 * with Result-based error handling.
 * 
 * @param Params Input parameter type for the use case
 * @param Type Return type wrapped in Result
 * 
 * **Usage Pattern:**
 * ```kotlin
 * class LoginUseCase @Inject constructor(
 *     private val authRepository: AuthRepository
 * ) : BaseUseCase<LoginUseCase.Params, User>() {
 *     
 *     data class Params(val email: String, val password: String)
 *     
 *     override suspend fun invoke(params: Params): Result<User> {
 *         return authRepository.login(params.email, params.password)
 *     }
 * }
 * ```
*
* **Benefits:**
* - Consistent error handling across app
* - Type-safe parameters
* - Testable in isolation
* - Single Responsibility Principle
*
* @see Result
  */
  abstract class BaseUseCase<in Params, out Type> {
  /**
    * Executes the use case with given parameters.
    *
    * @param params Input parameters for the operation
    * @return Result containing success data or failure exception
      */
      abstract suspend operator fun invoke(params: Params): Result<Type>
      }
```

**Subclass Example:**

```kotlin
/**
 * Use case for fetching content items for a subtopic.
 * 
 * Retrieves all published content associated with a subtopic,
 * ordered by creation date.
 * 
 * @see ContentRepository.getContent
 */
class GetContentUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Content>>() {
    
    /**
     * Fetches content for the given subtopic ID.
     * 
     * @param params Subtopic identifier
     * @return Result containing list of content or error
     */
    override suspend fun invoke(params: String): Result<List<Content>> {
        return contentRepository.getContent(params)
    }
}
```

---

#### Domain Models

**File**: `domain/model/Content.kt`

```kotlin
/**
 * Represents a single learning content item.
 * 
 * Content is the atomic unit of learning material in the system.
 * Each content belongs to a subtopic and can contain text, images,
 * videos, or PDFs.
 * 
 * **Lifecycle States:**
 * - DRAFT: Created but not published
 * - PENDING_APPROVAL: Submitted for review
 * - PUBLISHED: Live and accessible to students
 * - REJECTED: Review failed, needs revision
 * 
 * @property id Unique identifier
 * @property subtopicId Parent subtopic ID
 * @property creatorId ID of content creator
 * @property contentType Type of content (TEXT, IMAGE, VIDEO, etc.)
 * @property body Content body with text/media URLs
 * @property status Publication status
 * @property createdAt Timestamp of creation (Unix epoch millis)
 * 
 * @see ContentType
 * @see ContentBody
 * @see ContentStatus
 */
data class Content(
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val contentType: ContentType,
    val body: ContentBody,
    val status: ContentStatus,
    val createdAt: Long
)

/**
 * Content body containing text and media references.
 * 
 * At least one field must be non-null based on contentType:
 * - TEXT: text required
 * - IMAGE: imageUrl required
 * - VIDEO: videoUrl required
 * - TEXT_IMAGE: text + imageUrl required
 * - TEXT_VIDEO: text + videoUrl required
 * - PDF: pdfUrl required
 * - TEXT_PDF: text + pdfUrl required
 * 
 * @property text Markdown-formatted text content
 * @property imageUrl URL to image file
 * @property videoUrl URL to video file (MP4/HLS)
 * @property pdfUrl URL to PDF document
 */
data class ContentBody(
    val text: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val pdfUrl: String? = null
)

/**
 * Enum defining types of content.
 */
enum class ContentType {
    TEXT,          // Text only
    IMAGE,         // Image only
    VIDEO,         // Video only
    TEXT_IMAGE,    // Text with image
    TEXT_VIDEO,    // Text with video
    PDF,           // PDF document
    TEXT_PDF       // Text with PDF
}

/**
 * Enum defining content publication states.
 */
enum class ContentStatus {
    DRAFT,              // Not published
    PENDING_APPROVAL,   // Awaiting review
    PUBLISHED,          // Live content
    REJECTED            // Review rejected
}
```

**File**: `domain/model/Question.kt`

```kotlin
/**
 * Represents a quiz question.
 * 
 * Questions are used in quizzes to assess student understanding.
 * Supports three types: MCQ, STANDALONE, and SECTIONAL.
 * 
 * **Question Types:**
 * - MCQ: Multiple choice with 4 options
 * - STANDALONE: Single answer text input
 * - SECTIONAL: Multiple sub-questions (e.g., "a)", "b)", "c)")
 * 
 * @property id Unique identifier
 * @property subtopicId Associated subtopic
 * @property creatorId Question author
 * @property type Question type
 * @property difficulty Difficulty level
 * @property question Question text
 * @property options For MCQ: list of 4 options
 * @property correctAnswer For MCQ/STANDALONE: correct answer
 * @property subQuestions For SECTIONAL: list of sub-questions
 * 
 * @see QuestionType
 * @see DifficultyLevel
 * @see SubQuestion
 */
data class Question(
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val type: QuestionType,
    val difficulty: DifficultyLevel,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String? = null,
    val subQuestions: List<SubQuestion> = emptyList()
)

/**
 * Represents a sub-question within a sectional question.
 * 
 * Example:
 * Main question: "Solve the following equations:"
 * Sub-questions:
 *   a) 2x + 3 = 7
 *   b) 3y - 5 = 10
 * 
 * @property id Unique identifier
 * @property label Sub-question label (e.g., "a)", "b)")
 * @property question Sub-question text
 * @property correctAnswer Expected answer
 */
data class SubQuestion(
    val id: String,
    val label: String,
    val question: String,
    val correctAnswer: String
)

enum class QuestionType {
    MCQ,        // Multiple choice
    STANDALONE, // Single answer
    SECTIONAL   // Multiple sub-questions
}

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD
}
```

---

### Data Layer

#### Repository Implementation

**File**: `data/repository/CachedContentRepositoryImpl.kt`

```kotlin
/**
 * Cache-first implementation of ContentRepository.
 * 
 * Implements offline-first architecture with the following strategy:
 * 1. Check local cache (Room database)
 * 2. If cache valid (< 24 hours), return immediately
 * 3. If cache expired/missing, fetch from API
 * 4. Update cache on successful API response
 * 5. If API fails, return stale cache (if available)
 * 
 * **Cache Expiry:** 24 hours
 * 
 * **Benefits:**
 * - Works offline
 * - Fast loading from cache
 * - Reduced network usage
 * - Graceful degradation
 * 
 * @property contentDao Local database DAO
 * @property mockContentRepository Mock API (replace with real API)
 * 
 * @see ContentRepository
 * @see ContentDao
 */
@Singleton
class CachedContentRepositoryImpl @Inject constructor(
    private val contentDao: ContentDao,
    private val mockContentRepository: MockContentRepositoryImpl
) : ContentRepository {
    
    companion object {
        /** Cache expiry time in milliseconds (24 hours) */
        private const val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L
    }
    
    /**
     * Fetches classes with cache-first strategy.
     * 
     * Flow:
     * 1. Query cache for classes
     * 2. Check cache expiry (< 24 hours)
     * 3. If valid cache exists, return immediately
     * 4. If cache expired, fetch from API
     * 5. On API success, update cache
     * 6. On API failure, return stale cache (if exists)
     * 
     * @return Result containing list of classes or error
     */
    override suspend fun getClasses(): Result<List<Class>> = 
        withContext(Dispatchers.IO) {
            try {
                // Step 1: Try cache first
                val cachedClasses = contentDao.getClasses()
                
                // Step 2: Check cache validity
                if (cachedClasses.isNotEmpty() && 
                    !isCacheExpired(cachedClasses.first().cachedAt)) {
                    return@withContext Result.success(
                        cachedClasses.map { it.toDomain() }
                    )
                }
                
                // Step 3: Fetch from API (mock)
                val apiResult = mockContentRepository.getClasses()
                
                // Step 4: Handle API response
                apiResult.fold(
                    onSuccess = { classes ->
                        // Update cache
                        contentDao.clearAllClasses()
                        contentDao.insertClasses(classes.map { it.toEntity() })
                        Result.success(classes)
                    },
                    onFailure = { exception ->
                        // Fallback to stale cache
                        if (cachedClasses.isNotEmpty()) {
                            Result.success(cachedClasses.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    /**
     * Checks if cached data has expired.
     * 
     * @param cachedAt Timestamp when data was cached
     * @return true if cache is older than CACHE_EXPIRY_MS
     */
    private fun isCacheExpired(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt > CACHE_EXPIRY_MS
    }
}
```

---

#### Database DAO

**File**: `data/local/dao/ProgressDao.kt`

```kotlin
/**
 * Data Access Object for progress tracking operations.
 * 
 * Handles CRUD operations for three levels of progress:
 * - Content Progress: Individual content completion
 * - Subtopic Progress: Aggregated subtopic completion percentage
 * - Topic Progress: Aggregated topic completion percentage
 * 
 * **Progress Calculation:**
 * - Content: Boolean (complete/incomplete)
 * - Subtopic: (completed content / total content) * 100
 * - Topic: (completed subtopics / total subtopics) * 100
 * 
 * **Reactive Updates:**
 * - Flow-returning queries auto-update UI on data changes
 * - Use for real-time progress displays
 * 
 * @see ContentProgressEntity
 * @see SubtopicProgressEntity
 * @see TopicProgressEntity
 */
@Dao
interface ProgressDao {
    
    /**
     * Marks a content item as complete.
     * 
     * Side effects:
     * - Updates subtopic progress percentage
     * - Updates topic progress percentage
     * - Triggers Flow emissions for reactive UI
     * 
     * @param progress Content progress entity to insert/update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentProgress(progress: ContentProgressEntity)
    
    /**
     * Observes content progress for a user.
     * 
     * Returns a Flow that emits updates whenever progress changes.
     * Useful for real-time progress tracking in UI.
     * 
     * **Use Case:** Display checkmarks on completed content
     * 
     * @param userId User identifier
     * @return Flow emitting list of content progress on updates
     */
    @Query("SELECT * FROM content_progress WHERE userId = :userId")
    fun observeContentProgress(userId: String): Flow<List<ContentProgressEntity>>
    
    /**
     * Gets content progress for a specific subtopic.
     * 
     * Used to determine which content items are complete
     * when displaying a subtopic's content list.
     * 
     * @param userId User identifier
     * @param subtopicId Subtopic identifier
     * @return List of progress for content in subtopic
     */
    @Query("""
        SELECT cp.* FROM content_progress cp
        INNER JOIN content c ON cp.contentId = c.id
        WHERE cp.userId = :userId AND c.subtopicId = :subtopicId
    """)
    suspend fun getContentProgressForSubtopic(
        userId: String,
        subtopicId: String
    ): List<ContentProgressEntity>
    
    /**
     * Observes subtopic progress with reactive updates.
     * 
     * Emits updates when:
     * - Content is marked complete
     * - Subtopic progress recalculated
     * - New subtopics accessed
     * 
     * **Use Case:** Display progress bars on subtopic cards
     * 
     * @param userId User identifier
     * @param topicId Topic identifier
     * @return Flow emitting subtopic progress list
     */
    @Query("""
        SELECT * FROM subtopic_progress 
        WHERE userId = :userId AND subtopicId IN (
            SELECT id FROM subtopics WHERE topicId = :topicId
        )
        ORDER BY subtopicId
    """)
    fun observeSubtopicProgress(
        userId: String,
        topicId: String
    ): Flow<List<SubtopicProgressEntity>>
    
    /**
     * Updates subtopic progress percentage.
     * 
     * Called after content completion to recalculate
     * the subtopic's overall progress.
     * 
     * **Calculation:**
     * ```
     * percentage = (completedContent / totalContent) * 100
     * ```
     * 
     * @param progress Updated subtopic progress
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtopicProgress(progress: SubtopicProgressEntity)
    
    /**
     * Observes topic progress for a subject.
     * 
     * Used to display topic cards with completion indicators.
     * 
     * @param userId User identifier
     * @param subjectId Subject identifier
     * @return Flow of topic progress list
     */
    @Query("""
        SELECT * FROM topic_progress 
        WHERE userId = :userId AND topicId IN (
            SELECT id FROM topics WHERE subjectId = :subjectId
        )
        ORDER BY topicId
    """)
    fun observeTopicProgress(
        userId: String,
        subjectId: String
    ): Flow<List<TopicProgressEntity>>
    
    /**
     * Batch retrieval for analytics.
     * 
     * Used by AnalyticsRepository to calculate overall stats.
     * 
     * @param userId User identifier
     * @return All content progress for user
     */
    @Query("SELECT * FROM content_progress WHERE userId = :userId")
    suspend fun getContentProgressByUser(userId: String): List<ContentProgressEntity>
    
    @Query("SELECT * FROM subtopic_progress WHERE userId = :userId")
    suspend fun getSubtopicProgressByUser(userId: String): List<SubtopicProgressEntity>
    
    @Query("SELECT * FROM topic_progress WHERE userId = :userId")
    suspend fun getTopicProgressByUser(userId: String): List<TopicProgressEntity>
}
```

---

### Presentation Layer

#### ViewModel Example

**File**: `presentation/screens/content/ContentViewModel.kt`

```kotlin
/**
 * ViewModel for content display and navigation.
 * 
 * Manages the content viewing experience including:
 * - Loading content for a subtopic
 * - Swipe navigation between content items
 * - Marking content as complete
 * - Tracking current position
 * - Navigating to quiz after content completion
 * 
 * **State Management:**
 * - Uses StateFlow for reactive UI updates
 * - Implements UiState pattern for loading/success/error
 * - Maintains current index for swipe position
 * - Tracks completed content IDs
 * 
 * **User Flow:**
 * 1. Content loads from repository
 * 2. User swipes through content items
 * 3. User marks content as complete
 * 4. After all content complete, quiz button appears
 * 5. Navigate to quiz
 * 
 * @property getContentUseCase Fetches content for subtopic
 * @property markCompleteUseCase Marks content as complete
 * @property getCurrentUserUseCase Gets current user ID
 * @property observeContentProgressUseCase Observes completion status
 * @property savedStateHandle Provides subtopicId from navigation
 * 
 * @see Content
 * @see UiState
 */
@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val markCompleteUseCase: MarkContentCompleteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeContentProgressUseCase: ObserveContentProgressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Extract subtopicId from navigation arguments
    private val subtopicId: String = 
        checkNotNull(savedStateHandle[SUBTOPIC_ID_KEY])
    
    private val currentUserId: String? = getCurrentUserUseCase()?.id
    
    /** Current content loading state */
    private val _contentState = MutableStateFlow<UiState<List<Content>>>(UiState.Idle)
    val contentState: StateFlow<UiState<List<Content>>> = _contentState.asStateFlow()
    
    /** Current swipe position index */
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    /** Set of completed content IDs for current user */
    private val _completedContent = MutableStateFlow<Set<String>>(emptySet())
    val completedContent: StateFlow<Set<String>> = _completedContent.asStateFlow()
    
    init {
        loadContent()
        observeProgress()
    }
    
    /**
     * Loads content for the subtopic.
     * 
     * Sets loading state, calls use case, and updates state based on result.
     * Handles errors gracefully with error state.
     */
    fun loadContent() {
        viewModelScope.launch {
            _contentState.value = UiState.Loading
            
            val result = getContentUseCase(subtopicId)
            
            result.fold(
                onSuccess = { content ->
                    _contentState.value = if (content.isEmpty()) {
                        UiState.Error("No content available")
                    } else {
                        UiState.Success(content)
                    }
                },
                onFailure = { exception ->
                    _contentState.value = UiState.Error(
                        exception.message ?: "Failed to load content"
                    )
                }
            )
        }
    }
    
    /**
     * Observes content progress in real-time.
     * 
     * Listens to database changes and updates completed content set.
     * Triggers UI recomposition when progress changes.
     */
    private fun observeProgress() {
        val userId = currentUserId ?: return
        
        viewModelScope.launch {
            observeContentProgressUseCase(
                ObserveContentProgressUseCase.Params(userId, subtopicId)
            ).collect { progressList ->
                _completedContent.value = progressList
                    .filter { it.completed }
                    .map { it.contentId }
                    .toSet()
            }
        }
    }
    
    /**
     * Updates current swipe index.
     * 
     * Called by PagerState observer in UI.
     * 
     * @param index New page index (0-based)
     */
    fun onPageChanged(index: Int) {
        _currentIndex.value = index
    }
    
    /**
     * Marks current content as complete.
     * 
     * Updates database and triggers progress recalculation
     * for subtopic and topic levels.
     * 
     * Side effects:
     * - Updates content progress
     * - Recalculates subtopic progress
     * - Recalculates topic progress
     * - Triggers Flow emissions for UI updates
     */
    fun markCurrentContentComplete() {
        val userId = currentUserId ?: return
        val content = (_contentState.value as? UiState.Success)?.data ?: return
        val currentContent = content.getOrNull(_currentIndex.value) ?: return
        
        viewModelScope.launch {
            val result = markCompleteUseCase(
                MarkContentCompleteUseCase.Params(
                    userId = userId,
                    contentId = currentContent.id,
                    subtopicId = subtopicId
                )
            )
            
            result.onFailure { exception ->
                // Log error (could show Snackbar in UI)
                println("Failed to mark complete: ${exception.message}")
            }
        }
    }
    
    /**
     * Checks if all content is complete.
     * 
     * Used to determine whether to show quiz button.
     * 
     * @return true if all content items are marked complete
     */
    fun isAllContentComplete(): Boolean {
        val content = (_contentState.value as? UiState.Success)?.data ?: return false
        return content.all { it.id in _completedContent.value }
    }
    
    companion object {
        private const val SUBTOPIC_ID_KEY = "subtopicId"
    }
}
```

---

## Utilities & Helpers

### UiState

**File**: `presentation/util/UiState.kt`

```kotlin
/**
 * Sealed class representing UI states.
 * 
 * Provides a type-safe way to represent loading, success, and error states
 * in ViewModels. Enables exhaustive when expressions for state handling.
 * 
 * **Usage:**
 * ```kotlin
 * // ViewModel
 * private val _state = MutableStateFlow<UiState<List<Item>>>(UiState.Idle)
 * val state: StateFlow<UiState<List<Item>>> = _state.asStateFlow()
 * 
 * fun loadData() {
 *     viewModelScope.launch {
 *         _state.value = UiState.Loading
 *         val result = repository.getData()
 *         _state.value = result.fold(
 *             onSuccess = { UiState.Success(it) },
 *             onFailure = { UiState.Error(it.message ?: "Error") }
 *         )
 *     }
 * }
 * 
 * // UI
 * when (val state = viewModel.state.collectAsState().value) {
 *     is UiState.Idle -> { /* Show initial state */ }
 *     is UiState.Loading -> CircularProgressIndicator()
 *     is UiState.Success -> ShowContent(state.data)
 *     is UiState.Error -> ShowError(state.message)
 * }
 * ```
*
* @param T Type of data in Success state
  */
  sealed class UiState<out T> {
  /**
    * Initial state before any operation.
    *
    * Use for screens that start with empty state.
      */
      data object Idle : UiState<Nothing>()

  /**
    * Loading state during async operations.
    *
    * Show loading indicators (spinner, skeleton, shimmer).
      */
      data object Loading : UiState<Nothing>()

  /**
    * Success state with data.
    *
    * @property data The successfully loaded data
      */
      data class Success<T>(val data: T) : UiState<T>()

  /**
    * Error state with message.
    *
    * @property message Human-readable error message
      */
      data class Error(val message: String) : UiState<Nothing>()
      }
```

---

### Haptic Feedback

**File**: `presentation/util/HapticFeedback.kt`

```kotlin
/**
 * Utility for haptic feedback (vibration).
 * 
 * Provides a simple API for triggering different types of haptic feedback
 * to enhance user experience.
 * 
 * **Usage:**
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val haptic = rememberHapticFeedback()
 *     
 *     Button(onClick = { 
 *         haptic.click()
 *         // perform action
 *     }) {
 *         Text("Click Me")
 *     }
 * }
 * ```
*
* **Haptic Types:**
* - click(): Light tap for button presses
* - longPress(): Strong feedback for long press
* - success(): Confirmation feedback
* - error(): Error feedback (rejection)
*
* @property view Android View for performing haptic feedback
  */
  @Composable
  fun rememberHapticFeedback(): HapticFeedback {
  val view = LocalView.current
  return remember { HapticFeedback(view) }
  }

class HapticFeedback(private val view: View) {

    /**
     * Light haptic feedback for clicks.
     * 
     * Use for: Button clicks, item selection, navigation.
     */
    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }
    
    /**
     * Strong haptic feedback for long press.
     * 
     * Use for: Long press actions, drag initiation.
     */
    fun longPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
    
    /**
     * Success confirmation haptic (Android 11+).
     * 
     * Use for: Successful completion, quiz pass, achievement unlock.
     * Fallback to click on older Android versions.
     */
    fun success() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            click()
        }
    }
    
    /**
     * Error/rejection haptic (Android 11+).
     * 
     * Use for: Failed validation, quiz fail, error actions.
     * Fallback to long press on older Android versions.
     */
    fun error() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            longPress()
        }
    }
}
```

---

## Extension Functions

**File**: `presentation/util/Extensions.kt`

```kotlin
/**
 * Extension functions for common operations.
 */

/**
 * Formats timestamp to readable date string.
 * 
 * @param pattern Date format pattern (default: "MMM dd, yyyy")
 * @return Formatted date string
 * 
 * Example: 1638316800000L.toDateString() -> "Dec 01, 2021"
 */
fun Long.toDateString(pattern: String = "MMM dd, yyyy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(Date(this))
}

/**
 * Formats timestamp to time string.
 * 
 * @param pattern Time format pattern (default: "HH:mm")
 * @return Formatted time string
 * 
 * Example: 1638316800000L.toTimeString() -> "14:30"
 */
fun Long.toTimeString(pattern: String = "HH:mm"): String {
    val timeFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return timeFormat.format(Date(this))
}

/**
 * Rounds float to specified decimal places.
 * 
 * @param decimals Number of decimal places (default: 1)
 * @return Rounded float value
 * 
 * Example: 75.6789f.roundTo(2) -> 75.68f
 */
fun Float.roundTo(decimals: Int = 1): Float {
    val multiplier = 10.0.pow(decimals).toFloat()
    return (this * multiplier).roundToInt() / multiplier
}

/**
 * Capitalizes first letter of string.
 * 
 * @return String with first letter capitalized
 * 
 * Example: "hello".capitalize() -> "Hello"
 */
fun String.capitalize(): String {
    return this.replaceFirstChar { 
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
    }
}

/**
 * Converts minutes to hours and minutes string.
 * 
 * @return Formatted string (e.g., "2h 30m")
 * 
 * Example: 150.toHoursMinutes() -> "2h 30m"
 */
fun Int.toHoursMinutes(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours == 0 -> "${minutes}m"
        minutes == 0 -> "${hours}h"
        else -> "${hours}h ${minutes}m"
    }
}

/**
 * Checks if string is a valid email.
 * 
 * @return true if valid email format
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Truncates string to max length with ellipsis.
 * 
 * @param maxLength Maximum string length
 * @return Truncated string with "..." if needed
 * 
 * Example: "Long text".truncate(5) -> "Long..."
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) {
        this
    } else {
        "${this.take(maxLength)}..."
    }
}
```

---

## Custom Components

### AsyncImageLoader

**File**: `presentation/components/AsyncImageLoader.kt`

```kotlin
/**
 * Composable for loading images asynchronously with Coil.
 * 
 * Features:
 * - Loading placeholder (shimmer effect)
 * - Error state with retry
 * - Automatic caching (memory + disk)
 * - Crossfade animation
 * - Content scale options
 * 
 * **Usage:**
 * ```kotlin
 * AsyncImageLoader(
 *     imageUrl = "https://example.com/image.png",
 *     contentDescription = "Profile picture",
 *     modifier = Modifier
 *         .size(100.dp)
 *         .clip(CircleShape)
 * )
 * ```
*
* @param imageUrl URL of image to load
* @param contentDescription Accessibility description
* @param modifier Modifier for sizing and styling
* @param contentScale How to scale image (default: Crop)
*
* @see SubcomposeAsyncImage
  */
  @Composable
  fun AsyncImageLoader(
  imageUrl: String,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop
  ) {
  SubcomposeAsyncImage(
  model = imageUrl,
  contentDescription = contentDescription,
  modifier = modifier,
  contentScale = contentScale
  ) {
  when (painter.state) {
  // Show loading indicator while fetching
  is AsyncImagePainter.State.Loading -> {
  Box(
  modifier = Modifier.fillMaxSize(),
  contentAlignment = Alignment.Center
  ) {
  CircularProgressIndicator(
  modifier = Modifier.size(40.dp)
  )
  }
  }

           // Show error state if load fails
           is AsyncImagePainter.State.Error -> {
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
                           text = "🖼️",
                           style = MaterialTheme.typography.displayMedium
                       )
                       Spacer(modifier = Modifier.height(8.dp))
                       Text(
                           text = "Failed to load image",
                           style = MaterialTheme.typography.bodySmall,
                           color = MaterialTheme.colorScheme.error
                       )
                   }
               }
           }
           
           // Show image when loaded
           else -> {
               SubcomposeAsyncImageContent()
           }
       }
  }
  }
```

---

### ShimmerEffect

**File**: `presentation/components/ShimmerEffect.kt`

```kotlin
/**
 * Shimmer loading effect for skeleton screens.
 * 
 * Creates animated shimmer effect for loading placeholders.
 * Provides better UX than static spinners for content loading.
 * 
 * **Usage:**
 * ```kotlin
 * // Apply to any Box
 * Box(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(20.dp)
 *         .shimmerEffect()
 * )
 * 
 * // Pre-built list item
 * ShimmerListItem()
 * ```
*
* **Benefits:**
* - Shows content structure while loading
* - Reduces perceived loading time
* - More engaging than spinners
* - Indicates data is coming
    */

/**
* Creates animated shimmer brush.
*
* @param showShimmer Whether to show shimmer or transparent
* @param targetValue Animation end value
* @return Brush with shimmer gradient
  */
  @Composable
  fun shimmerBrush(
  showShimmer: Boolean = true,
  targetValue: Float = 1000f
  ): Brush {
  return if (showShimmer) {
  val shimmerColors = listOf(
  Color.LightGray.copy(alpha = 0.6f),
  Color.LightGray.copy(alpha = 0.2f),
  Color.LightGray.copy(alpha = 0.6f),
  )

       val transition = rememberInfiniteTransition(label = "shimmer")
       val translateAnimation = transition.animateFloat(
           initialValue = 0f,
           targetValue = targetValue,
           animationSpec = infiniteRepeatable(
               animation = tween(800),
               repeatMode = RepeatMode.Restart
           ),
           label = "shimmer"
       )

       Brush.linearGradient(
           colors = shimmerColors,
           start = Offset.Zero,
           end = Offset(
               x = translateAnimation.value,
               y = translateAnimation.value
           )
       )
  } else {
  Brush.linearGradient(
  colors = listOf(Color.Transparent, Color.Transparent),
  start = Offset.Zero,
  end = Offset.Zero
  )
  }
  }

/**
* Modifier extension for shimmer effect.
*
* Apply to any Composable to add shimmer during loading.
  */
  fun Modifier.shimmerEffect(): Modifier = composed {
  background(shimmerBrush())
  }

/**
* Pre-built shimmer list item skeleton.
*
* Mimics structure of typical list item card.
  */
  @Composable
  fun ShimmerListItem(modifier: Modifier = Modifier) {
  Card(
  modifier = modifier.fillMaxWidth(),
  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
  Row(
  modifier = Modifier
  .fillMaxWidth()
  .padding(16.dp)
  ) {
  // Icon placeholder
  Box(
  modifier = Modifier
  .size(60.dp)
  .shimmerEffect()
  )

           Spacer(modifier = Modifier.width(16.dp))
           
           // Text placeholders
           Column(modifier = Modifier.weight(1f)) {
               Box(
                   modifier = Modifier
                       .fillMaxWidth(0.7f)
                       .height(20.dp)
                       .shimmerEffect()
               )
               
               Spacer(modifier = Modifier.height(8.dp))
               
               Box(
                   modifier = Modifier
                       .fillMaxWidth(0.9f)
                       .height(16.dp)
                       .shimmerEffect()
               )
           }
       }
  }
  }
```

---

## State Management

### StateFlow Best Practices

```kotlin
/**
 * ViewModel state management best practices.
 */
class ExampleViewModel : ViewModel() {
    
    // ✅ CORRECT: Private mutable, public immutable
    private val _state = MutableStateFlow<UiState<Data>>(UiState.Idle)
    val state: StateFlow<UiState<Data>> = _state.asStateFlow()
    
    // ❌ INCORRECT: Exposing mutable state
    // val state = MutableStateFlow<UiState<Data>>(UiState.Idle)
    
    // ✅ CORRECT: StateFlow for single values
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // ✅ CORRECT: StateFlow for UI state
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()
    
    // ❌ INCORRECT: LiveData in new code (prefer StateFlow)
    // val userName: LiveData<String> = MutableLiveData()
    
    fun loadData() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            
            try {
                val result = repository.fetchData()
                _state.value = UiState.Success(result)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
    
    // ✅ CORRECT: Update state in ViewModel only
    fun updateUserName(name: String) {
        _userName.value = name
    }
}
```

### Collecting State in Compose

```kotlin
/**
 * Best practices for collecting StateFlow in Compose UI.
 */
@Composable
fun ExampleScreen(viewModel: ExampleViewModel = hiltViewModel()) {
    
    // ✅ CORRECT: collectAsStateWithLifecycle (lifecycle-aware)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    // ❌ INCORRECT: collectAsState (not lifecycle-aware)
    // val state by viewModel.state.collectAsState()
    
    // ✅ CORRECT: Handle all states exhaustively
    when (state) {
        is UiState.Idle -> IdleContent()
        is UiState.Loading -> LoadingContent()
        is UiState.Success -> SuccessContent((state as UiState.Success).data)
        is UiState.Error -> ErrorContent((state as UiState.Error).message)
    }
    
    // ✅ CORRECT: LaunchedEffect for one-time events
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    
    // ❌ INCORRECT: Collecting in composition (creates new job each recomposition)
    // viewModel.state.collect { /* ... */ }
}
```

---

## Navigation

### Navigation Setup

**File**: `presentation/navigation/Screen.kt`

```kotlin
/**
 * Sealed class defining all navigation destinations.
 * 
 * Each screen is represented as an object with a route string.
 * Screens with arguments include createRoute() helper functions.
 * 
 * **Benefits:**
 * - Type-safe navigation
 * - Centralized route management
 * - Easy to add new screens
 * - Compile-time verification
 * 
 * **Usage:**
 * ```kotlin
 * // Navigate to screen without args
 * navController.navigate(Screen.Home.route)
 * 
 * // Navigate to screen with args
 * navController.navigate(Screen.Content.createRoute(subtopicId))
 * ```
*/
sealed class Screen(val route: String) {
data object Splash : Screen("splash")
data object Login : Screen("login")
data object Register : Screen("register")
data object Home : Screen("home")
data object Classes : Screen("classes")

    /**
     * Subjects screen.
     * 
     * Route: subjects/{classId}
     * Argument: classId (String)
     */
    data object Subjects : Screen("subjects/{classId}") {
        fun createRoute(classId: String) = "subjects/$classId"
    }
    
    /**
     * Topics screen.
     * 
     * Route: topics/{subjectId}
     * Argument: subjectId (String)
     */
    data object Topics : Screen("topics/{subjectId}") {
        fun createRoute(subjectId: String) = "topics/$subjectId"
    }
    
    /**
     * Subtopics screen.
     * 
     * Route: subtopics/{topicId}
     * Argument: topicId (String)
     */
    data object Subtopics : Screen("subtopics/{topicId}") {
        fun createRoute(topicId: String) = "subtopics/$topicId"
    }
    
    /**
     * Content screen.
     * 
     * Route: content/{subtopicId}
     * Argument: subtopicId (String)
     */
    data object Content : Screen("content/{subtopicId}") {
        fun createRoute(subtopicId: String) = "content/$subtopicId"
    }
    
    /**
     * Quiz screen.
     * 
     * Route: quiz/{subtopicId}
     * Argument: subtopicId (String)
     */
    data object Quiz : Screen("quiz/{subtopicId}") {
        fun createRoute(subtopicId: String) = "quiz/$subtopicId"
    }
    
    /**
     * Quiz results screen.
     * 
     * Route: quiz_results/{quizId}
     * Argument: quizId (String)
     */
    data object QuizResults : Screen("quiz_results/{quizId}") {
        fun createRoute(quizId: String) = "quiz_results/$quizId"
    }
    
    data object Search : Screen("search")
    data object Analytics : Screen("analytics")
    data object Settings : Screen("settings")
    data object Leaderboard : Screen("leaderboard")
    
    // Teacher screens
    data object TeacherDashboard : Screen("teacher_dashboard")
    data object LessonPlanner : Screen("lesson_planner")
    data object LessonPlanForm : Screen("lesson_plan_form/{planId}") {
        fun createRoute(planId: String = "new") = "lesson_plan_form/$planId"
    }
    data object QuestionBank : Screen("question_bank")
    data object QuestionForm : Screen("question_form/{questionId}") {
        fun createRoute(questionId: String = "new") = "question_form/$questionId"
    }
    data object ExamBuilder : Screen("exam_builder")
    data object ExamPreview : Screen("exam_preview/{examId}") {
        fun createRoute(examId: String) = "exam_preview/$examId"
    }
}
```

---

## Database Operations

### Room Query Examples

```kotlin
/**
 * Common Room query patterns used in the app.
 */

// ✅ Simple query
@Query("SELECT * FROM content WHERE subtopicId = :subtopicId")
suspend fun getContent(subtopicId: String): List<ContentEntity>

// ✅ Query with Flow (reactive)
@Query("SELECT * FROM content WHERE subtopicId = :subtopicId")
fun getContentFlow(subtopicId: String): Flow<List<ContentEntity>>

// ✅ Query with JOIN
@Query("""
    SELECT cp.* FROM content_progress cp
    INNER JOIN content c ON cp.contentId = c.id
    WHERE cp.userId = :userId AND c.subtopicId = :subtopicId
""")
suspend fun getProgressForSubtopic(
    userId: String,
    subtopicId: String
): List<ContentProgressEntity>

// ✅ Query with subquery
@Query("""
    SELECT * FROM topic_progress 
    WHERE userId = :userId AND topicId IN (
        SELECT id FROM topics WHERE subjectId = :subjectId
    )
""")
fun observeTopicProgress(
    userId: String,
    subjectId: String
): Flow<List<TopicProgressEntity>>

// ✅ Query with ORDER BY
@Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY `order` ASC")
suspend fun getTopicsOrdered(subjectId: String): List<TopicEntity>

// ✅ Count query
@Query("SELECT COUNT(*) FROM content WHERE subtopicId = :subtopicId AND completed = 1")
suspend fun getCompletedCount(subtopicId: String): Int

// ✅ Update query
@Query("UPDATE content SET status = :status WHERE id = :contentId")
suspend fun updateStatus(contentId: String, status: String)

// ✅ Delete query
@Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
suspend fun deleteBookmark(bookmarkId: String)

// ✅ Insert with conflict strategy
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertContent(content: ContentEntity)

// ✅ Batch insert
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertAllContent(content: List<ContentEntity>)

// ✅ Transaction for multiple operations
@Transaction
suspend fun markContentCompleteAndUpdateProgress(
    contentProgress: ContentProgressEntity,
    subtopicProgress: SubtopicProgressEntity,
    topicProgress: TopicProgressEntity
) {
    insertContentProgress(contentProgress)
    insertSubtopicProgress(subtopicProgress)
    insertTopicProgress(topicProgress)
}
```

---

## Testing Guidelines

### Unit Test Example

```kotlin
/**
 * Example unit test for ViewModel.
 * 
 * Tests business logic in isolation using mocked dependencies.
 */
@ExperimentalCoroutinesTest
class ContentViewModelTest {
    
    // Mock dependencies
    private lateinit var getContentUseCase: GetContentUseCase
    private lateinit var markCompleteUseCase: MarkContentCompleteUseCase
    private lateinit var viewModel: ContentViewModel
    
    // Test dispatcher for coroutines
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Create mocks
        getContentUseCase = mockk()
        markCompleteUseCase = mockk()
        
        // Create ViewModel with mocks
        viewModel = ContentViewModel(
            getContentUseCase = getContentUseCase,
            markCompleteUseCase = markCompleteUseCase,
            savedStateHandle = SavedStateHandle(mapOf("subtopicId" to "test"))
        )
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadContent success updates state to Success`() = runTest {
        // Given
        val mockContent = listOf(
            Content(/* ... */)
        )
        coEvery { getContentUseCase("test") } returns Result.success(mockContent)
        
        // When
        viewModel.loadContent()
        
        // Then
        val state = viewModel.contentState.value
        assertTrue(state is UiState.Success)
        assertEquals(mockContent, (state as UiState.Success).data)
    }
    
    @Test
    fun `loadContent error updates state to Error`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getContentUseCase("test") } returns 
            Result.failure(Exception(errorMessage))
        
        // When
        viewModel.loadContent()
        
        // Then
        val state = viewModel.contentState.value
        assertTrue(state is UiState.Error)
        assertEquals(errorMessage, (state as UiState.Error).message)
    }
}
```

---

## Common Pitfalls & Solutions

### 1. Memory Leaks

```kotlin
// ❌ WRONG: Leaks context
class MyViewModel(private val context: Context) : ViewModel()

// ✅ CORRECT: Use Application context
class MyViewModel(
    @ApplicationContext private val appContext: Context
) : ViewModel()

// ❌ WRONG: Leaks View reference
class MyViewModel : ViewModel() {
    var view: MyView? = null  // Memory leak!
}

// ✅ CORRECT: Don't hold View references in ViewModel
class MyViewModel : ViewModel() {
    // Use StateFlow to communicate with View
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()
}
```

### 2. Coroutine Cancellation

```kotlin
// ❌ WRONG: Not cancellable
fun loadData() {
    GlobalScope.launch {
        // This keeps running even after ViewModel cleared
    }
}

// ✅ CORRECT: Use viewModelScope
fun loadData() {
    viewModelScope.launch {
        // Automatically cancelled when ViewModel cleared
    }
}

// ✅ CORRECT: Handle cancellation
fun longRunningTask() {
    viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                // Check for cancellation
                ensureActive()
                // Long running work
            }
        } catch (e: CancellationException) {
            // Cleanup
            throw e  // Re-throw to propagate cancellation
        }
    }
}
```

### 3. Compose Recomposition

```kotlin
// ❌ WRONG: New object on every recomposition
@Composable
fun MyScreen() {
    val viewModel = MyViewModel()  // Creates new instance every time!
}

// ✅ CORRECT: Use hiltViewModel() or viewModel()
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    // ViewModel survives recomposition
}

// ❌ WRONG: Unstable parameter causes recomposition
@Composable
fun ItemList(items: List<Item>) {
    // items is unstable, causes unnecessary recomposition
}

// ✅ CORRECT: Use stable/immutable collections
@Composable
fun ItemList(items: ImmutableList<Item>) {
    // Or mark with @Stable/@Immutable annotation
}
```

---

## Version History

**Version 1.0** (December 2024)
- Initial documentation
- Complete code coverage
- All modules documented

---

## Contributors

**Primary Developer**: LearnHub Kenya Development Team  
**Documentation**: Technical Writing Team  
**Review**: Architecture Review Board

---

## Related Documents

- [01_PROJECT_OVERVIEW.md](01_PROJECT_OVERVIEW.md) - Requirements & scope
- [02_ARCHITECTURE_DESIGN.md](02_ARCHITECTURE_DESIGN.md) - System architecture
- [03_API_DOCUMENTATION.md](03_API_DOCUMENTATION.md) - API reference
- [05_TEST_DOCUMENTATION.md](05_TEST_DOCUMENTATION.md) - Testing guide (coming next)

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Status**: Complete
```
