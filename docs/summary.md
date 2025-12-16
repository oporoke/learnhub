● I've thoroughly explored the LearnHub project. Here's my comprehensive understanding:

LearnHub Kenya - Educational Platform Overview

Purpose

LearnHub is a dual-role educational mobile app serving:
- Students: Access structured learning materials (text, video, PDFs), take quizzes, track progress, earn achievements
- Teachers: Create lesson plans, build question banks, generate exams, and manage schemes of work

Supports both Kenyan curricula (844 & CBC) with offline-first architecture.

  ---
Architecture: Clean Architecture + MVVM

📱 Presentation Layer (Jetpack Compose)
├─ Screens (30+ composable screens)
├─ ViewModels (Hilt-injected, StateFlow-based)
└─ Navigation (Compose Navigation)

💼 Domain Layer (Business Logic)
├─ Models (Content, User, Quiz, Analytics, etc.)
├─ Repository Interfaces
└─ Use Cases (Single Responsibility per operation)

💾 Data Layer
├─ Local: Room Database (cached content, progress, bookmarks)
├─ Remote: Retrofit APIs (content, auth, quizzes)
├─ Preferences: DataStore (theme, notifications)
└─ Repository Implementations (cache-first strategy)

  ---
Core App Flow

Student Journey:
Splash → Login/Register → Home
├→ Classes → Subjects → Topics → Subtopics → Content → Quiz
├→ Search (full-text across all content)
├→ Analytics Dashboard (progress, scores, charts)
├→ Leaderboard (competition, achievements)
└→ Settings (theme, notifications)

Teacher Journey:
Home → Teacher Dashboard
├→ Lesson Planner (create lesson plans per topic)
├→ Question Bank (manage MCQ, essay, sectional questions)
└→ Exam Builder (generate custom exams from question pool)

  ---
Key Features

For Students:
- Hierarchical content browsing (Class > Subject > Topic > Subtopic > Content)
- Multi-format content: Text, Images, Videos (ExoPlayer), PDFs
- Interactive quizzes with 3 question types (MCQ, Essay, Sectional)
- Progress tracking with completion percentages
- Bookmarking system
- Global search functionality
- Gamification (leaderboards, achievements, streaks)
- Student analytics dashboard
- Offline content caching (24-hour expiry)

For Teachers:
- Lesson plan creation and management
- Question bank (Easy/Medium/Hard difficulty levels)
- Exam generator (CAT, Midterm, End-Term, Custom)
- Scheme of work auto-generation
- PDF export for exams

  ---
Tech Stack

| Component     | Technology               |
  |---------------|--------------------------|
| UI            | Jetpack Compose          |
| DI            | Hilt/Dagger              |
| Database      | Room                     |
| Networking    | Retrofit 3 + OkHttp 5    |
| Async         | Kotlin Coroutines + Flow |
| Image Loading | Coil 3                   |
| Video         | Media3 ExoPlayer         |
| PDF           | android-pdf-viewer       |
| Navigation    | Compose Navigation       |
| Serialization | Kotlinx Serialization    |

  ---
Data Strategy

Cache-First Architecture:
1. Check Room database (local cache)
2. If expired (>24h) or missing, fetch from API
3. Fallback to mock data if API fails
4. All queries return Flow<T> for reactive updates

Database Tables:
- Content hierarchy (Class, Subject, Topic, Subtopic, Content)
- Progress tracking (per content, subtopic, topic)
- Bookmarks (topics & subtopics)

  ---
Design Patterns

✅ Repository Pattern - Abstract data sources
✅ UseCase Pattern - Encapsulate business logic
✅ MVVM - ViewModels with StateFlow
✅ Sealed Classes - Type-safe UI states (UiState<T>)
✅ Dependency Injection - Hilt for testability
✅ Offline-First - Room + intelligent caching
✅ Reactive Streams - Flow/StateFlow throughout

  ---
User Roles

| Role           | Access Level                                                    |
  |----------------|-----------------------------------------------------------------|
| STUDENT        | Learning content, quizzes, analytics, leaderboard               |
| TEACHER        | Lesson planning, question bank, exam builder + student features |
| ADMIN          | System management (planned)                                     |
| COURSE_CREATOR | Content upload & management (planned)                           |

  ---
I now have complete context of LearnHub's:
- ✅ Clean Architecture with clear layer separation
- ✅ Dual-role system (Student/Teacher workflows)
- ✅ Hierarchical content structure (Class → Subject → Topic → Subtopic → Content)
- ✅ Offline-first caching strategy
- ✅ Modern Android stack (Compose, Hilt, Room, Flow)
- ✅ Gamification & analytics features
- ✅ Quiz system with multiple question types

Current Status: Teacher Suite milestone completed, student flow complete, with 50+ modified/new files in current working state.
