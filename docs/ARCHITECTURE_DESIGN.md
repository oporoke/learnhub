# 2️⃣ ARCHITECTURE & DESIGN DOCUMENTATION

Path: `docs/02_ARCHITECTURE_DESIGN.md`

```markdown
# LearnHub Kenya - Architecture & Design Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Architecture Principles](#architecture-principles)
3. [High-Level Architecture](#high-level-architecture)
4. [Layer Architecture](#layer-architecture)
5. [Component Design](#component-design)
6. [Data Architecture](#data-architecture)
7. [Technology Stack](#technology-stack)
8. [Architecture Decision Records](#architecture-decision-records)
9. [Design Patterns](#design-patterns)
10. [Cross-Cutting Concerns](#cross-cutting-concerns)

---

## System Overview

### Architecture Style
**Clean Architecture with MVVM Pattern**

LearnHub Kenya follows Uncle Bob's Clean Architecture principles combined with Google's recommended MVVM (Model-View-ViewModel) pattern for Android applications.

### Key Characteristics
- **Separation of Concerns**: Clear boundaries between layers
- **Independence**: Core business logic independent of frameworks
- **Testability**: Easy to test at every layer
- **Flexibility**: Easy to swap implementations
- **Maintainability**: Clear structure for future enhancements

### Architecture Goals
1. **Scalability**: Support growth from hundreds to millions of users
2. **Maintainability**: Easy to understand and modify
3. **Testability**: High test coverage achievable
4. **Performance**: Efficient resource usage
5. **Offline-First**: Work without constant connectivity

---

## Architecture Principles

### 1. Dependency Rule
**Dependencies point inward**: Outer layers depend on inner layers, never the reverse.

```
Presentation → Domain ← Data
```

- **Domain Layer**: No dependencies on other layers
- **Data Layer**: Depends only on Domain
- **Presentation Layer**: Depends on Domain, not directly on Data

### 2. Single Responsibility Principle (SRP)
Each class/module has one reason to change.

- ViewModels handle UI state logic
- Use Cases contain business rules
- Repositories manage data access
- DAOs handle database operations

### 3. Open/Closed Principle
Open for extension, closed for modification.

- Use interfaces for abstraction
- Implement new features without modifying existing code
- Leverage inheritance and polymorphism

### 4. Liskov Substitution Principle
Subtypes must be substitutable for their base types.

- Repository implementations are interchangeable
- Mock repositories for testing

### 5. Interface Segregation Principle
Clients shouldn't depend on interfaces they don't use.

- Small, focused repository interfaces
- Separate concerns (e.g., ContentRepository, ProgressRepository)

### 6. Dependency Inversion Principle
Depend on abstractions, not concretions.

- Use Hilt for dependency injection
- Program to interfaces, not implementations

---

## High-Level Architecture

### System Context Diagram (C4 Level 1)

```
┌─────────────────────────────────────────────────────────────┐
│                                                               │
│                    LearnHub Kenya System                      │
│                                                               │
│  ┌──────────────┐         ┌──────────────┐                  │
│  │   Student    │────────▶│   Android    │                  │
│  │   (User)     │         │     App      │                  │
│  └──────────────┘         └──────────────┘                  │
│                                   │                           │
│  ┌──────────────┐                 │                          │
│  │   Teacher    │─────────────────┘                          │
│  │   (User)     │                                            │
│  └──────────────┘                                            │
│                                                               │
└─────────────────────────────────────────────────────────────┘
│
│ HTTPS/REST
▼
┌──────────────────┐
│   Backend API    │
│    (Future)      │
└──────────────────┘
│
▼
┌──────────────────┐
│    Database      │
│   (PostgreSQL)   │
└──────────────────┘
```

### Container Diagram (C4 Level 2)

```
┌─────────────────────────────────────────────────────────────┐
│                     Android Application                       │
│                                                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │                  Presentation Layer                     │ │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐             │ │
│  │  │ Activity │  │  Screens │  │ViewModels│             │ │
│  │  │          │  │(Compose) │  │          │             │ │
│  │  └──────────┘  └──────────┘  └──────────┘             │ │
│  └────────────────────────────────────────────────────────┘ │
│                              │                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │                    Domain Layer                         │ │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐             │ │
│  │  │  Models  │  │Use Cases │  │Repository│             │ │
│  │  │          │  │          │  │Interfaces│             │ │
│  │  └──────────┘  └──────────┘  └──────────┘             │ │
│  └────────────────────────────────────────────────────────┘ │
│                              │                               │
│  ┌────────────────────────────────────────────────────────┐ │
│  │                     Data Layer                          │ │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐             │ │
│  │  │Repository│  │   Room   │  │ Retrofit │             │ │
│  │  │   Impl   │  │ Database │  │   API    │             │ │
│  │  └──────────┘  └──────────┘  └──────────┘             │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## Layer Architecture

### Presentation Layer

**Responsibility**: UI rendering and user interaction

**Components**:
- **Activity**: Single MainActivity (host for navigation)
- **Screens**: Jetpack Compose UI screens (20+ screens)
- **ViewModels**: State management and business logic coordination
- **Navigation**: NavGraph for screen routing
- **Components**: Reusable UI components
- **Util**: UI utilities (UiState, extensions)

**Key Technologies**:
- Jetpack Compose
- Material 3
- Hilt (for ViewModel injection)
- Navigation Compose
- Lifecycle & StateFlow

**Package Structure**:
```
presentation/
├── screens/
│   ├── splash/
│   │   ├── SplashScreen.kt
│   │   └── SplashViewModel.kt
│   ├── login/
│   ├── home/
│   ├── classes/
│   ├── subjects/
│   ├── topics/
│   ├── subtopics/
│   ├── content/
│   ├── quiz/
│   ├── teacher/
│   ├── search/
│   ├── analytics/
│   ├── settings/
│   └── social/
├── components/
│   ├── AsyncImageLoader.kt
│   ├── VideoPlayerComposable.kt
│   ├── PdfViewerComposable.kt
│   ├── ShimmerEffect.kt
│   └── EmptyStateComponent.kt
├── navigation/
│   ├── NavGraph.kt
│   └── Screen.kt
└── util/
├── UiState.kt
└── HapticFeedback.kt
```

**Design Patterns**:
- **MVVM**: ViewModel mediates between View and Domain
- **Observer Pattern**: StateFlow for reactive UI updates
- **State Pattern**: UiState for loading/success/error states
- **Factory Pattern**: ViewModelFactory via Hilt

### Domain Layer

**Responsibility**: Business logic and rules

**Components**:
- **Models**: Core domain entities (User, Content, Quiz, etc.)
- **Use Cases**: Single-responsibility business operations
- **Repository Interfaces**: Data access contracts

**Key Principles**:
- Pure Kotlin (no Android dependencies)
- Framework-independent
- Testable with unit tests
- Single Responsibility per Use Case

**Package Structure**:
```
domain/
├── model/
│   ├── User.kt
│   ├── Content.kt
│   ├── Question.kt
│   ├── Progress.kt
│   ├── Analytics.kt
│   └── Social.kt
├── repository/
│   ├── AuthRepository.kt
│   ├── ContentRepository.kt
│   ├── QuestionRepository.kt
│   ├── ProgressRepository.kt
│   ├── BookmarkRepository.kt
│   ├── SearchRepository.kt
│   ├── AnalyticsRepository.kt
│   └── SocialRepository.kt
└── usecase/
├── auth/
│   ├── LoginUseCase.kt
│   ├── RegisterUseCase.kt
│   └── LogoutUseCase.kt
├── content/
│   ├── GetClassesUseCase.kt
│   ├── GetSubjectsUseCase.kt
│   └── GetContentUseCase.kt
├── progress/
│   ├── MarkContentCompleteUseCase.kt
│   └── ObserveProgressUseCase.kt
├── quiz/
│   ├── GetQuestionsUseCase.kt
│   └── SubmitQuizUseCase.kt
└── BaseUseCase.kt
```

**Base Use Case Pattern**:
```kotlin
abstract class BaseUseCase<in Params, out Type> {
    abstract suspend operator fun invoke(params: Params): Result<Type>
}
```

### Data Layer

**Responsibility**: Data access and persistence

**Components**:
- **Repository Implementations**: Concrete data operations
- **Local Data Sources**: Room database (DAOs, Entities)
- **Remote Data Sources**: Retrofit API services (future)
- **Mappers**: Entity ↔ Domain model conversion
- **Preferences**: DataStore for settings

**Key Technologies**:
- Room Database
- Retrofit (prepared, mock for now)
- DataStore Preferences
- Kotlin Coroutines

**Package Structure**:
```
data/
├── repository/
│   ├── AuthRepositoryImpl.kt
│   ├── ContentRepositoryImpl.kt
│   ├── CachedContentRepositoryImpl.kt
│   ├── QuestionRepositoryImpl.kt
│   ├── ProgressRepositoryImpl.kt
│   └── MockRepositories...
├── local/
│   ├── LearnHubDatabase.kt
│   ├── dao/
│   │   ├── ProgressDao.kt
│   │   ├── ContentDao.kt
│   │   └── BookmarkDao.kt
│   ├── entity/
│   │   ├── ContentEntities.kt
│   │   ├── ProgressEntities.kt
│   │   └── BookmarkEntity.kt
│   └── mapper/
│       ├── ContentEntityMapper.kt
│       └── ProgressEntityMapper.kt
├── remote/
│   ├── api/
│   │   ├── AuthApiService.kt
│   │   └── ContentApiService.kt
│   └── dto/
│       └── ApiModels.kt
├── preferences/
│   ├── ThemePreferences.kt
│   └── NotificationPreferences.kt
└── notification/
    └── NotificationHelper.kt
```

**Cache Strategy**:
```
┌─────────────────────────────────────────────────────┐
│              CachedContentRepository                 │
│                                                      │
│  1. Check Room Database (cache)                     │
│     ├─ If exists & not expired → Return from cache  │
│     └─ If missing or expired → Fetch from API       │
│                                                      │
│  2. Fetch from API                                  │
│     ├─ Success → Save to cache → Return data        │
│     └─ Failure → Return cached data (if any)        │
│                                                      │
│  Cache Expiry: 24 hours                             │
└─────────────────────────────────────────────────────┘
```

---

## Component Design

### Authentication Flow

```
┌──────────┐         ┌──────────────┐         ┌────────────┐
│  Login   │────────▶│LoginViewModel│────────▶│LoginUseCase│
│  Screen  │         └──────────────┘         └────────────┘
└──────────┘                │                        │
                            │                        ▼
                            │                  ┌────────────┐
                            │                  │AuthRepo    │
                            │                  │Interface   │
                            │                  └────────────┘
                            │                        │
                            ▼                        ▼
                      ┌──────────┐           ┌────────────┐
                      │ UiState  │           │AuthRepo    │
                      │ Success/ │           │Impl        │
                      │ Error    │           └────────────┘
                      └──────────┘
```

**Sequence**:
1. User enters credentials
2. LoginScreen calls ViewModel
3. ViewModel calls LoginUseCase
4. UseCase calls AuthRepository
5. Repository validates credentials (mock) or calls API
6. Result flows back through layers
7. ViewModel updates UiState
8. UI reacts to state change

### Content Browsing Flow

```
┌──────────┐    ┌──────────────┐    ┌─────────────────┐    ┌──────────┐
│ Classes  │───▶│ Subjects     │───▶│ Topics          │───▶│Subtopics │
│ Screen   │    │ Screen       │    │ Screen          │    │ Screen   │
└──────────┘    └──────────────┘    └─────────────────┘    └──────────┘
     │               │                     │                      │
     ▼               ▼                     ▼                      ▼
┌──────────┐    ┌──────────────┐    ┌─────────────────┐    ┌──────────┐
│ Classes  │    │ Subjects     │    │ Topics          │    │Subtopics │
│ViewModel │    │ ViewModel    │    │ ViewModel       │    │ViewModel │
└──────────┘    └──────────────┘    └─────────────────┘    └──────────┘
     │               │                     │                      │
     └───────────────┴─────────────────────┴──────────────────────┘
                                    │
                                    ▼
                          ┌──────────────────┐
                          │ContentRepository │
                          │(Cached)          │
                          └──────────────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
              ┌──────────┐                   ┌──────────┐
              │  Room    │                   │Retrofit  │
              │ Database │                   │   API    │
              └──────────┘                   └──────────┘
```

### Progress Tracking Flow

```
┌──────────────┐
│Content Screen│
│(User reads)  │
└──────────────┘
        │
        │ Mark Complete
        ▼
┌──────────────┐
│ContentView   │
│Model         │
└──────────────┘
        │
        ▼
┌──────────────────┐
│MarkContent       │
│CompleteUseCase   │
└──────────────────┘
        │
        ▼
┌──────────────────┐
│ProgressRepository│
└──────────────────┘
        │
        ▼
┌──────────────────┐
│ProgressDao       │
│(Room)            │
└──────────────────┘
        │
        ├─ Update ContentProgress
        ├─ Recalculate SubtopicProgress
        └─ Recalculate TopicProgress
```

**Progress Calculation**:
```kotlin
// Content Progress: boolean (complete/incomplete)
// Subtopic Progress: (completed content / total content) * 100
// Topic Progress: (completed subtopics / total subtopics) * 100
```

### Quiz System Flow

```
┌──────────┐    ┌──────────────┐    ┌──────────────┐
│ Subtopic │───▶│ Quiz         │───▶│ Quiz Results │
│ Screen   │    │ Screen       │    │ Screen       │
└──────────┘    └──────────────┘    └──────────────┘
                      │
                      ▼
                ┌──────────────┐
                │QuizViewModel │
                └──────────────┘
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
    ┌─────────┐ ┌─────────┐ ┌─────────┐
    │GetQuiz  │ │Submit   │ │Grade    │
    │UseCase  │ │UseCase  │ │UseCase  │
    └─────────┘ └─────────┘ └─────────┘
          │           │           │
          └───────────┴───────────┘
                      │
                      ▼
              ┌───────────────┐
              │QuestionRepo   │
              └───────────────┘
```

**Grading Logic**:
```kotlin
// Auto-grade on submission
questions.forEach { question ->
    when (question.type) {
        MCQ -> answers[question.id] == question.correctAnswer
        STANDALONE -> answers[question.id] == question.correctAnswer
        SECTIONAL -> subQuestions.all { 
            answers[it.id] == it.correctAnswer 
        }
    }
}

score = (correctAnswers / totalQuestions) * 100
passed = score >= 70
```

---

## Data Architecture

### Database Schema (Room)

**Entity Relationship Diagram**:

```
┌────────────────────┐
│   ClassEntity      │
│ ─────────────────  │
│ id (PK)            │
│ name               │
│ curriculum         │
│ cachedAt           │
└────────────────────┘
          │ 1
          │
          │ n
┌────────────────────┐
│  SubjectEntity     │
│ ─────────────────  │
│ id (PK)            │
│ classId (FK)       │
│ name               │
│ iconUrl            │
│ cachedAt           │
└────────────────────┘
          │ 1
          │
          │ n
┌────────────────────┐
│   TopicEntity      │
│ ─────────────────  │
│ id (PK)            │
│ subjectId (FK)     │
│ name               │
│ description        │
│ order              │
│ cachedAt           │
└────────────────────┘
          │ 1
          │
          │ n
┌────────────────────┐
│ SubtopicEntity     │
│ ─────────────────  │
│ id (PK)            │
│ topicId (FK)       │
│ name               │
│ description        │
│ order              │
│ cachedAt           │
└────────────────────┘
          │ 1
          │
          │ n
┌────────────────────┐
│  ContentEntity     │
│ ─────────────────  │
│ id (PK)            │
│ subtopicId (FK)    │
│ creatorId          │
│ contentType        │
│ bodyText           │
│ bodyImageUrl       │
│ bodyVideoUrl       │
│ bodyPdfUrl         │
│ status             │
│ createdAt          │
│ cachedAt           │
└────────────────────┘
```

**Progress Tracking Schema**:

```
┌────────────────────────────┐
│ ContentProgressEntity      │
│ ─────────────────────────  │
│ id (PK)                    │
│ userId                     │
│ contentId                  │
│ completed (Boolean)        │
│ completedAt                │
└────────────────────────────┘

┌────────────────────────────┐
│ SubtopicProgressEntity     │
│ ─────────────────────────  │
│ id (PK)                    │
│ userId                     │
│ subtopicId                 │
│ completionPercentage       │
│ lastAccessedAt             │
└────────────────────────────┘

┌────────────────────────────┐
│ TopicProgressEntity        │
│ ─────────────────────────  │
│ id (PK)                    │
│ userId                     │
│ topicId                    │
│ completionPercentage       │
│ lastAccessedAt             │
└────────────────────────────┘

┌────────────────────────────┐
│ BookmarkEntity             │
│ ─────────────────────────  │
│ id (PK)                    │
│ userId                     │
│ itemType (TOPIC/SUBTOPIC)  │
│ itemId                     │
│ itemTitle                  │
│ itemDescription            │
│ bookmarkedAt               │
└────────────────────────────┘
```

### Data Flow Patterns

**1. Cache-First Pattern**:
```
User Request → ViewModel → UseCase → Repository
                                        ↓
                                    Check Cache
                                        ↓
                              ┌─────────┴─────────┐
                        Cache Hit             Cache Miss
                              │                   │
                        Return Data          Fetch API
                              │                   │
                              └─────────┬─────────┘
                                        ↓
                                   Update UI
```

**2. Reactive Pattern (Flow)**:
```
Database Change (Insert/Update/Delete)
        ↓
   Flow Emission
        ↓
   StateFlow Update
        ↓
   Compose Recomposition
        ↓
   UI Update
```

**3. Repository Pattern**:
```
┌────────────────────────────────────────────┐
│           Repository Interface             │
│  (Domain Layer - Contract)                 │
└────────────────────────────────────────────┘
                    ↑
                    │ implements
                    │
┌────────────────────────────────────────────┐
│       Repository Implementation            │
│  (Data Layer - Concrete)                   │
│                                            │
│  - Coordinates data sources                │
│  - Applies caching strategy                │
│  - Maps entities to domain models          │
└────────────────────────────────────────────┘
            ↓                   ↓
    ┌──────────────┐    ┌──────────────┐
    │  Local Data  │    │ Remote Data  │
    │   (Room)     │    │  (Retrofit)  │
    └──────────────┘    └──────────────┘
```

---

## Technology Stack

### Core Technologies

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| Language | Kotlin | 2.1.0 | Primary language |
| UI Framework | Jetpack Compose | 1.7.5 | Declarative UI |
| Design System | Material 3 | 1.3.1 | UI components |
| Architecture | Clean Architecture + MVVM | - | App structure |
| DI | Hilt | 2.51.1 | Dependency injection |
| Database | Room | 2.6.1 | Local persistence |
| Networking | Retrofit + OkHttp | 2.11.0 | API communication |
| Async | Coroutines + Flow | 1.9.0 | Concurrency |
| Navigation | Navigation Compose | 2.8.4 | Screen routing |
| Image Loading | Coil | 3.0.0 | Image caching |
| Video Player | ExoPlayer (Media3) | 1.4.1 | Video playback |
| PDF Viewer | AndroidPdfViewer | 3.2.0-beta.1 | PDF rendering |
| Preferences | DataStore | 1.1.1 | Settings storage |

### Build & Tools

| Tool | Version | Purpose |
|------|---------|---------|
| Gradle | 8.7 | Build system |
| Android Gradle Plugin | 8.7.3 | Android build |
| Compile SDK | 35 | Target Android version |
| Min SDK | 26 (Android 8.0) | Minimum support |
| Target SDK | 35 (Android 15) | Target version |
| Java | JDK 21 | Compilation |

### Libraries Summary

```kotlin
dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01")
    
    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    
    // PDF Viewer
    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
```

---

## Architecture Decision Records (ADRs)

### ADR-001: Clean Architecture with MVVM

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Need to choose architecture pattern for Android app

**Decision**: Implement Clean Architecture with MVVM pattern

**Rationale**:
- **Separation of Concerns**: Clear layer boundaries
- **Testability**: Easy to unit test business logic
- **Maintainability**: Standard pattern familiar to Android developers
- **Scalability**: Easy to add features without affecting existing code
- **Framework Independence**: Core logic not tied to Android

**Consequences**:
- ✅ Highly testable codebase
- ✅ Easy to swap data sources (mock to real API)
- ✅ Clear code organization
- ❌ More boilerplate initially
- ❌ Learning curve for junior developers

**Alternatives Considered**:
- MVC: Too much coupling
- MVP: Requires interfaces for views
- MVI: More complex state management

---

### ADR-002: Jetpack Compose for UI

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Choose UI framework (XML vs Compose)

**Decision**: Use Jetpack Compose for all UI

**Rationale**:
- **Modern**: Google's recommended approach
- **Declarative**: Easier to reason about UI state
- **Less Code**: Reduces boilerplate
- **Type Safety**: Compile-time checks
- **Material 3**: Native support for latest design system
- **Performance**: Efficient recomposition

**Consequences**:
- ✅ Faster UI development
- ✅ Easier state management
- ✅ Better animations support
- ✅ No XML fragmentation
- ❌ Larger APK size initially
- ❌ Some third-party libraries lack Compose support

**Alternatives Considered**:
- XML Views: Legacy, verbose
- Flutter: Different language, separate ecosystem
- React Native: JavaScript overhead

---

### ADR-003: Room for Local Database

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Need local persistence for offline support

**Decision**: Use Room Persistence Library

**Rationale**:
- **Type Safety**: Compile-time SQL verification
- **Coroutines Support**: Native async operations
- **Flow Support**: Reactive data observation
- **Migration Support**: Schema versioning
- **Official**: Backed by Google/Android team
- **Performance**: Optimized for Android

**Consequences**:
- ✅ Robust offline support
- ✅ Type-safe database access
- ✅ Reactive UI updates via Flow
- ✅ Easy testing with in-memory database
- ❌ Learning curve for complex queries
- ❌ Annotation processing overhead

**Alternatives Considered**:
- SQLite directly: No type safety
- Realm: Deprecated for mobile
- ObjectBox: Less mature ecosystem

---

### ADR-004: Hilt for Dependency Injection

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Need DI framework for testability and modularity

**Decision**: Use Hilt (Dagger wrapper)

**Rationale**:
- **Official**: Google's recommended DI for Android
- **Compile-Time Safety**: Errors caught at build time
- **ViewModel Integration**: Native support
- **Less Boilerplate**: Simpler than Dagger
- **Scoping**: Proper lifecycle management

**Consequences**:
- ✅ Easy to test (mock dependencies)
- ✅ Reduced boilerplate
- ✅ Compile-time verification
- ✅ Standard in Android community
- ❌ Build time increase (annotation processing)
- ❌ Learning curve for DI concepts

**Alternatives Considered**:
- Koin: Runtime DI, less type safety
- Manual DI: Too much boilerplate
- Dagger: More complex setup

---

### ADR-005: Cache-First Data Strategy

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Handle intermittent connectivity in Kenya

**Decision**: Implement cache-first with 24-hour expiry

**Rationale**:
- **Offline Support**: App works without internet
- **Performance**: Instant data loading from cache
- **Cost**: Reduces data usage (important in Kenya)
- **UX**: No loading spinners for cached data
- **Reliability**: Graceful degradation

**Cache Strategy**:
1. Check local cache first
2. If cache valid (< 24 hours), return immediately
3. If cache expired/missing, fetch from API
4. Update cache on successful API response
5. If API fails, return stale cache (with indicator)

**Consequences**:
- ✅ Excellent offline experience
- ✅ Fast app performance
- ✅ Lower data costs for users
- ✅ Works in poor connectivity
- ❌ Stale data for 24 hours
- ❌ Cache invalidation complexity
- ❌ Storage space usage

**Alternatives Considered**:
- Network-first: Poor offline support
- Cache-only: Stale data issues
- Network-only: Doesn't work offline

---

### ADR-006: Mock Repositories for MVP

**Status**: Accepted (Temporary)  
**Date**: 2024-11  
**Context**: Backend API not ready, need to develop app

**Decision**: Use mock repository implementations

**Rationale**:
- **Unblock Development**: Can build app without backend
- **Testing**: Easy to test with predictable data
- **Demo**: Can demonstrate app to stakeholders
- **Architecture**: Proves repository pattern works
- **Flexibility**: Easy to swap to real implementation

**Implementation**:
```kotlin
// Interface (Domain Layer)
interface ContentRepository {
    suspend fun getClasses(): Result<List<Class>>
}

// Mock Implementation (Data Layer)
class MockContentRepositoryImpl : ContentRepository {
    override suspend fun getClasses() = Result.success(mockClasses)
}

// Future Real Implementation
class ContentRepositoryImpl(
    private val api: ContentApiService,
    private val dao: ContentDao
) : ContentRepository {
    override suspend fun getClasses() = /* real implementation */
}
```

**Migration Path**:
1. Implement backend API
2. Create real repository implementations
3. Update Hilt module binding
4. Remove mock implementations
5. Zero changes to ViewModels/Use Cases

**Consequences**:
- ✅ Development proceeds without backend
- ✅ Easy to switch to real API
- ✅ Good for testing and demos
- ❌ Must maintain mock data
- ❌ Mock behavior might not match real API

---

### ADR-007: Material 3 Design System

**Status**: Accepted  
**Date**: 2024-11  
**Context**: Choose design system for consistent UI

**Decision**: Use Material 3 (Material You)

**Rationale**:
- **Modern**: Latest design language from Google
- **Consistent**: Standard patterns users recognize
- **Accessible**: Built-in accessibility support
- **Theming**: Dynamic color theming
- **Components**: Rich set of pre-built components
- **Compose Integration**: First-class Compose support

**Features Used**:
- Dynamic color (Material You)
- Light/Dark theme support
- Elevation and shadows
- Motion and transitions
- Typography scales
- Color roles (primary, secondary, tertiary)

**Consequences**:
- ✅ Professional, modern appearance
- ✅ Consistent with Android ecosystem
- ✅ Accessibility built-in
- ✅ Easy theming and customization
- ❌ Limited to Material Design patterns
- ❌ May look too "generic" without customization

---

## Design Patterns

### 1. Repository Pattern

**Purpose**: Abstract data sources

**Structure**:
```
Interface (Domain) → Implementation (Data) → Data Sources
```

**Example**:
```kotlin
// Domain Layer
interface ContentRepository {
    suspend fun getContent(subtopicId: String): Result<List<Content>>
}

// Data Layer
class CachedContentRepositoryImpl(
    private val contentDao: ContentDao,
    private val mockRepo: MockContentRepositoryImpl
) : ContentRepository {
    override suspend fun getContent(subtopicId: String): Result<List<Content>> {
        // Check cache first
        val cached = contentDao.getContent(subtopicId)
        if (cached.isNotEmpty() && !isCacheExpired(cached)) {
            return Result.success(cached.map { it.toDomain() })
        }
        
        // Fetch from API (mock)
        return mockRepo.getContent(subtopicId).also { result ->
            result.onSuccess { content ->
                contentDao.insertContent(content.map { it.toEntity() })
            }
        }
    }
}
```

**Benefits**:
- Single source of truth
- Easy to swap implementations
- Testable with mocks
- Hides data source complexity

---

### 2. Use Case Pattern

**Purpose**: Single-responsibility business operations

**Structure**:
```kotlin
abstract class BaseUseCase<in Params, out Type> {
    abstract suspend operator fun invoke(params: Params): Result<Type>
}

class GetContentUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) : BaseUseCase<String, List<Content>>() {
    override suspend fun invoke(params: String): Result<List<Content>> {
        return contentRepository.getContent(params)
    }
}
```

**Benefits**:
- Clear business logic encapsulation
- Reusable across ViewModels
- Easy to test in isolation
- Single Responsibility Principle

---

### 3. Observer Pattern (Flow)

**Purpose**: Reactive state updates

**Structure**:
```kotlin
// ViewModel
class SubtopicsViewModel @Inject constructor(
    private val observeSubtopicProgressUseCase: ObserveSubtopicProgressUseCase
) : ViewModel() {
    
    private val _subtopics = MutableStateFlow<UiState<List<SubtopicWithProgress>>>(UiState.Idle)
    val subtopics: StateFlow<UiState<List<SubtopicWithProgress>>> = _subtopics.asStateFlow()
    
    init {
        observeProgress()
    }
    
    private fun observeProgress() {
        viewModelScope.launch {
            observeSubtopicProgressUseCase(userId, topicId)
                .collect { progressList ->
                    // Update UI state
                    _subtopics.value = UiState.Success(progressList)
                }
        }
    }
}

// Compose UI
@Composable
fun SubtopicsScreen(viewModel: SubtopicsViewModel) {
    val subtopics by viewModel.subtopics.collectAsStateWithLifecycle()
    
    when (val state = subtopics) {
        is UiState.Success -> ShowSubtopics(state.data)
        is UiState.Loading -> ShowLoading()
        is UiState.Error -> ShowError(state.message)
    }
}
```

**Benefits**:
- Reactive UI updates
- Automatic recomposition
- Clean separation of concerns
- Lifecycle-aware

---

### 4. State Pattern (UiState)

**Purpose**: Manage UI states consistently

**Structure**:
```kotlin
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

**Usage**:
```kotlin
// ViewModel
viewModelScope.launch {
    _state.value = UiState.Loading
    
    val result = useCase(params)
    
    _state.value = result.fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { UiState.Error(it.message ?: "Unknown error") }
    )
}

// UI
when (state) {
    UiState.Idle -> { /* Initial state */ }
    UiState.Loading -> CircularProgressIndicator()
    is UiState.Success -> ShowContent(state.data)
    is UiState.Error -> ShowError(state.message)
}
```

**Benefits**:
- Consistent error handling
- Clear loading states
- Type-safe state management
- Exhaustive when expressions

---

### 5. Factory Pattern (ViewModelFactory)

**Purpose**: Create ViewModels with dependencies

**Implementation** (via Hilt):
```kotlin
@HiltViewModel
class ContentViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val markCompleteUseCase: MarkContentCompleteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // ViewModel implementation
}

// Hilt automatically provides factory
@Composable
fun ContentScreen(viewModel: ContentViewModel = hiltViewModel()) {
    // ViewModel injected automatically
}
```

**Benefits**:
- No manual factory code
- Automatic dependency injection
- Scoped to lifecycle
- Testable with custom factories

---

### 6. Mapper Pattern

**Purpose**: Convert between entity and domain models

**Structure**:
```kotlin
// Entity (Data Layer)
data class ContentEntity(
    val id: String,
    val bodyText: String?,
    val bodyImageUrl: String?
)

// Domain Model
data class Content(
    val id: String,
    val body: ContentBody
)

// Mapper
fun ContentEntity.toDomain(): Content {
    return Content(
        id = this.id,
        body = ContentBody(
            text = this.bodyText,
            imageUrl = this.bodyImageUrl
        )
    )
}

fun Content.toEntity(): ContentEntity {
    return ContentEntity(
        id = this.id,
        bodyText = this.body.text,
        bodyImageUrl = this.body.imageUrl
    )
}
```

**Benefits**:
- Clean separation of concerns
- Type safety across layers
- Easy to modify data structures
- Clear transformation logic

---

### 7. Strategy Pattern (Repository Implementations)

**Purpose**: Swap algorithms/implementations

**Example**:
```kotlin
// Multiple repository strategies
class MockContentRepositoryImpl : ContentRepository { /* mock data */ }
class CachedContentRepositoryImpl : ContentRepository { /* cache-first */ }
class NetworkOnlyContentRepositoryImpl : ContentRepository { /* network-only */ }

// Module controls which strategy is used
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindContentRepository(
        impl: CachedContentRepositoryImpl // Change here to swap strategy
    ): ContentRepository
}
```

**Benefits**:
- Easy to swap implementations
- Can switch per build variant
- Clean abstraction
- Open/Closed Principle

---

## Cross-Cutting Concerns

### Error Handling

**Strategy**: Consistent error handling across app

**Implementation**:
```kotlin
// Use Result type for operations
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}

// Repository level
try {
    val data = api.fetchData()
    Result.Success(data)
} catch (e: Exception) {
    Result.Failure(e)
}

// ViewModel level
result.fold(
    onSuccess = { data -> _state.value = UiState.Success(data) },
    onFailure = { error -> _state.value = UiState.Error(error.message ?: "Error") }
)

// UI level
when (state) {
    is UiState.Error -> {
        ErrorMessage(
            message = state.message,
            onRetry = { viewModel.retry() }
        )
    }
}
```

**Error Categories**:
- **Network Errors**: No internet, timeout, server error
- **Database Errors**: Corruption, migration failure
- **Validation Errors**: Invalid input
- **Authentication Errors**: Token expired, unauthorized
- **Business Logic Errors**: Custom domain errors

---

### Logging

**Strategy**: Structured logging with Timber (future)

**Levels**:
- **Debug**: Development info
- **Info**: Important events (login, logout)
- **Warning**: Recoverable issues
- **Error**: Exceptions and failures

**Implementation**:
```kotlin
// Future implementation
Timber.d("Loading content for subtopic: $subtopicId")
Timber.i("User logged in: ${user.email}")
Timber.w("Cache expired, fetching from API")
Timber.e(exception, "Failed to load content")
```

---

### Security

**Current Implementation**:
- Input validation in ViewModels
- SQL injection prevention (Room parameterized queries)
- Secure password storage (future: hashed)

**Future Enhancements**:
- HTTPS for all network requests
- Certificate pinning
- JWT token management
- Encrypted local storage (for sensitive data)
- Biometric authentication
- ProGuard obfuscation

---

### Performance Optimization

**Strategies**:
1. **Lazy Loading**: Load content on demand
2. **Pagination**: Load data in chunks
3. **Caching**: 24-hour cache to reduce API calls
4. **Image Loading**: Coil with memory/disk cache
5. **Database Indexing**: Indexed columns for fast queries
6. **Coroutines**: Non-blocking async operations
7. **Compose**: Efficient recomposition

**Monitoring**:
- LaunchedEffect with time tracking
- Database query profiling
- Memory leak detection (LeakCanary)

---

### Accessibility

**Implementation**:
- Content descriptions on all interactive elements
- Semantic properties for screen readers
- Sufficient color contrast (WCAG AA)
- Touch target sizes (48dp minimum)
- Focus management
- Haptic feedback

**Example**:
```kotlin
IconButton(
    onClick = { /* action */ },
    modifier = Modifier.semantics {
        contentDescription = "Bookmark this topic"
    }
) {
    Icon(Icons.Default.Bookmark, contentDescription = null)
}
```

---

## Diagrams

### Class Diagram (Domain Layer)

```
┌─────────────────────┐
│   <<interface>>     │
│   ContentRepository │
│ ─────────────────── │
│ +getClasses()       │
│ +getSubjects()      │
│ +getTopics()        │
│ +getSubtopics()     │
│ +getContent()       │
└─────────────────────┘
           △
           │ implements
           │
┌─────────────────────┐
│CachedContentRepo    │
│Impl                 │
│ ─────────────────── │
│ -contentDao         │
│ -mockRepo           │
│ +getClasses()       │
│ +getSubjects()      │
└─────────────────────┘
```

### Sequence Diagram (Login Flow)

```
User    LoginScreen   LoginViewModel   LoginUseCase   AuthRepository
 │           │               │               │               │
 │─Enter─────▶               │               │               │
 │Credentials│               │               │               │
 │           │─Click Login──▶│               │               │
 │           │               │─invoke()─────▶│               │
 │           │               │               │─login()──────▶│
 │           │               │               │               │
 │           │               │               │◀─Result───────│
 │           │               │◀─Result───────│               │
 │           │◀─UiState──────│               │               │
 │           │  Success      │               │               │
 │◀Navigate──│               │               │               │
 │  to Home  │               │               │               │
```

---

## Future Improvements

### Architecture Enhancements

1. **Multi-Module Architecture**:
    - Split into feature modules
    - Core module for shared code
    - Improve build times

2. **Repository Composition**:
    - Combine multiple data sources elegantly
    - Strategy pattern for online/offline modes

3. **State Management**:
    - Consider MVI for complex state
    - Redux-like pattern for predictability

4. **Testing**:
    - Add unit tests (target 70%+ coverage)
    - Integration tests
    - UI tests with Compose Testing

5. **Performance**:
    - Baseline profiles for better startup
    - R8/ProGuard optimization
    - App startup metrics

### Technology Updates

1. **Kotlin Multiplatform**:
    - Share domain layer with iOS
    - Unified business logic

2. **Compose Multiplatform**:
    - Share UI code across platforms
    - Desktop version

3. **Server-Driven UI**:
    - Dynamic UI from backend
    - A/B testing capability

---

## Appendix

### Package Structure (Complete)

```
com.learnhub.kenya/
├── LearnHubApplication.kt
├── MainActivity.kt
│
├── di/
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
│
├── presentation/
│   ├── screens/
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
│   │       ├── TeacherDashboardScreen.kt
│   │       ├── lessonplanner/
│   │       ├── questionbank/
│   │       └── exambuilder/
│   ├── components/
│   ├── navigation/
│   └── util/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
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
├── data/
│   ├── repository/
│   ├── local/
│   │   ├── LearnHubDatabase.kt
│   │   ├── dao/
│   │   ├── entity/
│   │   └── mapper/
│   ├── remote/
│   │   ├── api/
│   │   └── dto/
│   ├── preferences/
│   └── notification/
│
└── ui/
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

### Key Metrics

| Metric | Value |
|--------|-------|
| Total Screens | 20+ |
| ViewModels | 15+ |
| Use Cases | 30+ |
| Repositories | 8 interfaces |
| Database Tables | 11 tables |
| Lines of Code | ~12,000+ |
| Development Time | ~10 hours |

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Author**: LearnHub Kenya Development Team  
**Status**: Approved
```

---
