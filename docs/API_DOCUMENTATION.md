# 3️⃣ API DOCUMENTATION

Path: `docs/03_API_DOCUMENTATION.md`

```markdown
# LearnHub Kenya - API Documentation

## Overview

**Base URL**: `https://api.learnhub.ke/v1` (Future)  
**Current Status**: Mock Implementation  
**Protocol**: HTTPS/REST  
**Authentication**: JWT Bearer Token  
**Content Type**: `application/json`

---

## Authentication

### Overview
All API endpoints (except login/register) require authentication via JWT token.

### Obtaining a Token

#### POST /auth/login

**Description**: Authenticate user and receive JWT token

**Request**:
```http
POST /auth/login
Content-Type: application/json

{
  "email": "student@test.com",
  "password": "password123"
}
```

**Response** (Success - 200):
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "user123",
      "name": "John Doe",
      "email": "student@test.com",
      "role": "STUDENT"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresAt": "2024-12-25T12:00:00Z"
  }
}
```

**Response** (Error - 401):
```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Invalid email or password"
  }
}
```

**Error Codes**:
- `INVALID_CREDENTIALS`: Wrong email/password
- `USER_NOT_FOUND`: Email not registered
- `ACCOUNT_DISABLED`: Account has been disabled

---

#### POST /auth/register

**Description**: Register new user account

**Request**:
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "role": "STUDENT"
}
```

**Validation Rules**:
- `name`: Required, 2-100 characters
- `email`: Required, valid email format
- `password`: Required, minimum 6 characters
- `role`: Required, enum [STUDENT, TEACHER, ADMIN, CREATOR]

**Response** (Success - 201):
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "user456",
      "name": "John Doe",
      "email": "john@example.com",
      "role": "STUDENT"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresAt": "2024-12-25T12:00:00Z"
  }
}
```

**Response** (Error - 400):
```json
{
  "success": false,
  "error": {
    "code": "EMAIL_EXISTS",
    "message": "Email already registered"
  }
}
```

**Error Codes**:
- `EMAIL_EXISTS`: Email already in use
- `INVALID_EMAIL`: Invalid email format
- `WEAK_PASSWORD`: Password doesn't meet requirements
- `VALIDATION_ERROR`: Other validation failures

---

#### POST /auth/logout

**Description**: Invalidate current token

**Request**:
```http
POST /auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response** (Success - 200):
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

---

### Using the Token

Include the JWT token in the `Authorization` header:

```http
GET /content/classes
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Token Expiry**: 7 days (default)  
**Refresh Strategy**: Request new token before expiry

---

## Content Endpoints

### GET /content/classes

**Description**: Retrieve all available classes

**Request**:
```http
GET /content/classes
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "form3",
      "name": "Form 3",
      "curriculum": "8-4-4"
    },
    {
      "id": "form4",
      "name": "Form 4",
      "curriculum": "8-4-4"
    }
  ]
}
```

**Fields**:
- `id`: Unique class identifier
- `name`: Display name
- `curriculum`: Enum [8-4-4, CBC]

---

### GET /content/classes/{classId}/subjects

**Description**: Get subjects for a specific class

**Path Parameters**:
- `classId`: Class identifier (e.g., "form3")

**Request**:
```http
GET /content/classes/form3/subjects
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "math",
      "classId": "form3",
      "name": "Mathematics",
      "iconUrl": "https://cdn.learnhub.ke/icons/math.png"
    },
    {
      "id": "chemistry",
      "classId": "form3",
      "name": "Chemistry",
      "iconUrl": "https://cdn.learnhub.ke/icons/chemistry.png"
    }
  ]
}
```

---

### GET /content/subjects/{subjectId}/topics

**Description**: Get topics for a subject

**Path Parameters**:
- `subjectId`: Subject identifier

**Query Parameters**:
- `sortBy` (optional): Sort order [order, name] (default: order)
- `limit` (optional): Max results (default: 100)
- `offset` (optional): Pagination offset (default: 0)

**Request**:
```http
GET /content/subjects/math/topics?sortBy=order&limit=10
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "algebra",
      "subjectId": "math",
      "name": "Algebra",
      "description": "Fundamental algebraic concepts and operations",
      "order": 1
    },
    {
      "id": "geometry",
      "subjectId": "math",
      "name": "Geometry",
      "description": "Shapes, angles, and spatial relationships",
      "order": 2
    }
  ],
  "pagination": {
    "total": 15,
    "limit": 10,
    "offset": 0,
    "hasMore": true
  }
}
```

---

### GET /content/topics/{topicId}/subtopics

**Description**: Get subtopics for a topic

**Path Parameters**:
- `topicId`: Topic identifier

**Query Parameters**:
- `includeProgress` (optional): Include user progress (default: false)

**Request**:
```http
GET /content/topics/algebra/subtopics?includeProgress=true
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "linear_eq",
      "topicId": "algebra",
      "name": "Linear Equations",
      "description": "Solving equations with one variable",
      "order": 1,
      "progress": {
        "completionPercentage": 75.0,
        "lastAccessedAt": "2024-12-01T10:30:00Z"
      }
    }
  ]
}
```

---

### GET /content/subtopics/{subtopicId}/content

**Description**: Get learning content for a subtopic

**Path Parameters**:
- `subtopicId`: Subtopic identifier

**Request**:
```http
GET /content/subtopics/linear_eq/content
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "content1",
      "subtopicId": "linear_eq",
      "creatorId": "creator123",
      "contentType": "TEXT_IMAGE",
      "body": {
        "text": "Linear equations are fundamental...",
        "imageUrl": "https://cdn.learnhub.ke/content/img1.png",
        "videoUrl": null,
        "pdfUrl": null
      },
      "status": "PUBLISHED",
      "createdAt": "2024-11-15T09:00:00Z"
    },
    {
      "id": "content2",
      "subtopicId": "linear_eq",
      "creatorId": "creator123",
      "contentType": "TEXT_VIDEO",
      "body": {
        "text": "Watch this video explanation...",
        "imageUrl": null,
        "videoUrl": "https://cdn.learnhub.ke/videos/linear1.mp4",
        "pdfUrl": null
      },
      "status": "PUBLISHED",
      "createdAt": "2024-11-15T09:15:00Z"
    }
  ]
}
```

**Content Types**:
- `TEXT`: Text only
- `IMAGE`: Image only
- `VIDEO`: Video only
- `TEXT_IMAGE`: Text with image
- `TEXT_VIDEO`: Text with video
- `PDF`: PDF document
- `TEXT_PDF`: Text with PDF

**Content Status**:
- `DRAFT`: Not published
- `PENDING_APPROVAL`: Awaiting review
- `PUBLISHED`: Live and accessible
- `REJECTED`: Rejected by moderator

---

## Progress Tracking Endpoints

### POST /progress/content/{contentId}/complete

**Description**: Mark content as complete

**Path Parameters**:
- `contentId`: Content identifier

**Request**:
```http
POST /progress/content/content1/complete
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "contentId": "content1",
    "completed": true,
    "completedAt": "2024-12-05T14:30:00Z",
    "subtopicProgress": {
      "subtopicId": "linear_eq",
      "completionPercentage": 25.0
    },
    "topicProgress": {
      "topicId": "algebra",
      "completionPercentage": 10.0
    }
  }
}
```

---

### GET /progress/user/{userId}/topics

**Description**: Get user's progress for all topics

**Path Parameters**:
- `userId`: User identifier (or "me" for current user)

**Request**:
```http
GET /progress/user/me/topics
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "topicId": "algebra",
      "topicName": "Algebra",
      "completionPercentage": 45.5,
      "lastAccessedAt": "2024-12-05T14:30:00Z",
      "subtopics": [
        {
          "subtopicId": "linear_eq",
          "subtopicName": "Linear Equations",
          "completionPercentage": 100.0,
          "contentCompleted": 4,
          "contentTotal": 4
        }
      ]
    }
  ]
}
```

---

## Quiz Endpoints

### GET /quiz/subtopics/{subtopicId}/questions

**Description**: Get quiz questions for a subtopic

**Path Parameters**:
- `subtopicId`: Subtopic identifier

**Query Parameters**:
- `count` (optional): Number of questions (default: 5, max: 20)
- `types` (optional): Comma-separated types [MCQ,STANDALONE,SECTIONAL]

**Request**:
```http
GET /quiz/subtopics/linear_eq/questions?count=5&types=MCQ,STANDALONE
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "quizId": "quiz789",
    "subtopicId": "linear_eq",
    "questions": [
      {
        "id": "q1",
        "type": "MCQ",
        "question": "What is 2x + 4 = 10?",
        "options": ["x = 2", "x = 3", "x = 4", "x = 5"],
        "correctAnswer": "x = 3",
        "difficulty": "EASY"
      },
      {
        "id": "q2",
        "type": "STANDALONE",
        "question": "Solve for y: 3y - 6 = 15",
        "correctAnswer": "y = 7",
        "difficulty": "MEDIUM"
      }
    ]
  }
}
```

**Question Types**:
- `MCQ`: Multiple choice (4 options)
- `STANDALONE`: Single answer
- `SECTIONAL`: Multiple sub-questions

**Difficulty Levels**:
- `EASY`: Basic concepts
- `MEDIUM`: Applied concepts
- `HARD`: Advanced problem-solving

---

### POST /quiz/{quizId}/submit

**Description**: Submit quiz answers for grading

**Path Parameters**:
- `quizId`: Quiz identifier from GET questions

**Request**:
```http
POST /quiz/quiz789/submit
Authorization: Bearer {token}
Content-Type: application/json

{
  "answers": {
    "q1": "x = 3",
    "q2": "y = 7"
  }
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "quizId": "quiz789",
    "score": 100.0,
    "totalQuestions": 2,
    "correctAnswers": 2,
    "passed": true,
    "passingScore": 70.0,
    "results": [
      {
        "questionId": "q1",
        "correct": true,
        "userAnswer": "x = 3",
        "correctAnswer": "x = 3"
      },
      {
        "questionId": "q2",
        "correct": true,
        "userAnswer": "y = 7",
        "correctAnswer": "y = 7"
      }
    ],
    "submittedAt": "2024-12-05T15:00:00Z"
  }
}
```

---

## Teacher Endpoints

### POST /teacher/lesson-plans

**Description**: Create a new lesson plan

**Request**:
```http
POST /teacher/lesson-plans
Authorization: Bearer {token}
Content-Type: application/json

{
  "classId": "form3",
  "subjectId": "math",
  "topicId": "algebra",
  "subtopicId": "linear_eq",
  "objectives": "Students will be able to solve linear equations",
  "notes": "Focus on step-by-step methods"
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "id": "lp123",
    "teacherId": "teacher456",
    "classId": "form3",
    "subjectId": "math",
    "topicId": "algebra",
    "subtopicId": "linear_eq",
    "objectives": "Students will be able to solve linear equations",
    "notes": "Focus on step-by-step methods",
    "createdAt": "2024-12-05T16:00:00Z"
  }
}
```

---

### GET /teacher/lesson-plans

**Description**: Get teacher's lesson plans

**Query Parameters**:
- `classId` (optional): Filter by class
- `subjectId` (optional): Filter by subject
- `limit` (optional): Max results (default: 20)
- `offset` (optional): Pagination offset

**Request**:
```http
GET /teacher/lesson-plans?subjectId=math&limit=10
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "lp123",
      "teacherId": "teacher456",
      "classId": "form3",
      "subjectName": "Mathematics",
      "topicName": "Algebra",
      "subtopicName": "Linear Equations",
      "objectives": "Students will be able to solve linear equations",
      "createdAt": "2024-12-05T16:00:00Z"
    }
  ],
  "pagination": {
    "total": 15,
    "limit": 10,
    "offset": 0
  }
}
```

---

### POST /teacher/questions

**Description**: Add question to teacher's question bank

**Request**:
```http
POST /teacher/questions
Authorization: Bearer {token}
Content-Type: application/json

{
  "classId": "form3",
  "subjectId": "math",
  "topicId": "algebra",
  "subtopicId": "linear_eq",
  "type": "MCQ",
  "difficulty": "MEDIUM",
  "question": "Solve: 2x + 5 = 13",
  "options": ["x = 2", "x = 4", "x = 6", "x = 8"],
  "correctAnswer": "x = 4"
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "id": "tq456",
    "teacherId": "teacher456",
    "type": "MCQ",
    "difficulty": "MEDIUM",
    "question": "Solve: 2x + 5 = 13",
    "correctAnswer": "x = 4",
    "createdAt": "2024-12-05T17:00:00Z"
  }
}
```

---

### POST /teacher/exams/generate

**Description**: Auto-generate exam from question pool

**Request**:
```http
POST /teacher/exams/generate
Authorization: Bearer {token}
Content-Type: application/json

{
  "classId": "form3",
  "topicIds": ["algebra", "geometry"],
  "count": 10,
  "questionTypes": ["MCQ", "STANDALONE"],
  "difficulty": "MEDIUM"
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "examId": "exam789",
    "questions": [
      {
        "id": "q1",
        "type": "MCQ",
        "question": "...",
        "options": ["...", "...", "...", "..."]
      }
    ],
    "metadata": {
      "totalQuestions": 10,
      "mcqCount": 7,
      "standaloneCount": 3,
      "averageDifficulty": "MEDIUM"
    }
  }
}
```

---

## Search Endpoint

### GET /search

**Description**: Search across topics, subtopics, and content

**Query Parameters**:
- `q` (required): Search query (min 2 characters)
- `type` (optional): Filter by type [TOPIC,SUBTOPIC,CONTENT]
- `classId` (optional): Filter by class
- `limit` (optional): Max results (default: 20, max: 50)

**Request**:
```http
GET /search?q=linear&type=SUBTOPIC&limit=10
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "type": "SUBTOPIC",
      "id": "linear_eq",
      "title": "Linear Equations",
      "description": "Solving equations with one variable",
      "classId": "form3",
      "subjectId": "math",
      "topicId": "algebra",
      "subtopicId": "linear_eq"
    },
    {
      "type": "CONTENT",
      "id": "content5",
      "title": "Content in Linear Equations",
      "description": "To solve a linear equation, isolate the variable...",
      "subtopicId": "linear_eq"
    }
  ],
  "metadata": {
    "query": "linear",
    "totalResults": 5,
    "searchTimeMs": 23
  }
}
```

---

## Analytics Endpoints

### GET /analytics/user/{userId}

**Description**: Get comprehensive user analytics

**Path Parameters**:
- `userId`: User identifier (or "me")

**Request**:
```http
GET /analytics/user/me
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "userId": "user123",
    "totalTopics": 15,
    "completedTopics": 5,
    "totalSubtopics": 45,
    "completedSubtopics": 12,
    "totalContent": 180,
    "completedContent": 45,
    "quizzesTaken": 8,
    "averageQuizScore": 78.5,
    "studyStreak": 7,
    "totalStudyTimeMinutes": 320,
    "topicProgress": [
      {
        "topicId": "algebra",
        "topicName": "Algebra",
        "subjectName": "Mathematics",
        "completionPercentage": 75.0,
        "contentCompleted": 15,
        "contentTotal": 20
      }
    ]
  }
}
```

---

## Social Endpoints

### GET /social/leaderboard

**Description**: Get leaderboard rankings

**Query Parameters**:
- `type` (required): Leaderboard type [COMPLETION,STREAK,WEEKLY]
- `limit` (optional): Max entries (default: 10, max: 50)

**Request**:
```http
GET /social/leaderboard?type=COMPLETION&limit=10
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "type": "COMPLETION",
    "entries": [
      {
        "rank": 1,
        "userId": "user789",
        "userName": "Alice Mwangi",
        "score": 95,
        "streak": 14,
        "completedContent": 87
      },
      {
        "rank": 2,
        "userId": "user456",
        "userName": "Brian Odhiambo",
        "score": 89,
        "streak": 10,
        "completedContent": 78
      }
    ],
    "currentUser": {
      "rank": 15,
      "score": 65
    }
  }
}
```

---

### GET /social/achievements/{userId}

**Description**: Get user's achievements

**Path Parameters**:
- `userId`: User identifier (or "me")

**Request**:
```http
GET /social/achievements/me
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "first_lesson",
      "title": "First Steps",
      "description": "Complete your first lesson",
      "icon": "🎯",
      "isUnlocked": true,
      "earnedAt": "2024-11-15T10:00:00Z"
    },
    {
      "id": "streak_7",
      "title": "Week Warrior",
      "description": "Maintain a 7-day streak",
      "icon": "🔥",
      "isUnlocked": false,
      "earnedAt": null
    }
  ]
}
```

---

## Bookmarks Endpoint

### POST /bookmarks

**Description**: Add bookmark

**Request**:
```http
POST /bookmarks
Authorization: Bearer {token}
Content-Type: application/json

{
  "itemType": "SUBTOPIC",
  "itemId": "linear_eq"
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "id": "bm123",
    "userId": "user123",
    "itemType": "SUBTOPIC",
    "itemId": "linear_eq",
    "itemTitle": "Linear Equations",
    "bookmarkedAt": "2024-12-05T18:00:00Z"
  }
}
```

---

### DELETE /bookmarks/{bookmarkId}

**Description**: Remove bookmark

**Path Parameters**:
- `bookmarkId`: Bookmark identifier

**Request**:
```http
DELETE /bookmarks/bm123
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Bookmark removed"
}
```

---

### GET /bookmarks

**Description**: Get user's bookmarks

**Query Parameters**:
- `type` (optional): Filter by type [TOPIC,SUBTOPIC]

**Request**:
```http
GET /bookmarks?type=SUBTOPIC
Authorization: Bearer {token}
```

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "id": "bm123",
      "itemType": "SUBTOPIC",
      "itemId": "linear_eq",
      "itemTitle": "Linear Equations",
      "itemDescription": "Solving equations with one variable",
      "bookmarkedAt": "2024-12-05T18:00:00Z"
    }
  ]
}
```

---

## Error Responses

### Standard Error Format

All errors follow this format:

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": {
      "field": "Additional context"
    }
  }
}
```

### Common HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET/PUT/DELETE |
| 201 | Created | Successful POST |
| 400 | Bad Request | Validation error, malformed request |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Valid token but insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Resource already exists |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server-side error |
| 503 | Service Unavailable | Temporary service disruption |

### Error Codes

| Code | Description |
|------|-------------|
| `INVALID_CREDENTIALS` | Wrong email/password |
| `TOKEN_EXPIRED` | JWT token has expired |
| `TOKEN_INVALID` | JWT token is malformed |
| `USER_NOT_FOUND` | User doesn't exist |
| `EMAIL_EXISTS` | Email already registered |
| `VALIDATION_ERROR` | Request validation failed |
| `RESOURCE_NOT_FOUND` | Requested resource not found |
| `UNAUTHORIZED` | Not authenticated |
| `FORBIDDEN` | Insufficient permissions |
| `RATE_LIMIT_EXCEEDED` | Too many requests |
| `SERVER_ERROR` | Internal server error |

---

## Rate Limiting

**Limits**:
- Authenticated requests: 1000 requests/hour
- Unauthenticated requests: 100 requests/hour
- Search endpoint: 100 requests/hour

**Headers**:
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1701788400
```

**429 Response**:
```json
{
  "success": false,
  "error": {
    "code": "RATE_LIMIT_EXCEEDED",
    "message": "Rate limit exceeded. Try again in 3600 seconds.",
    "retryAfter": 3600
  }
}
```

---

## Pagination

For endpoints returning lists, use pagination:

**Query Parameters**:
- `limit`: Results per page (default: 20, max: 100)
- `offset`: Skip N results (default: 0)

**Response includes**:
```json
{
  "data": [...],
  "pagination": {
    "total": 150,
    "limit": 20,
    "offset": 0,
    "hasMore": true
  }
}
```

---

## Versioning

**Current Version**: v1  
**Base URL**: `https://api.learnhub.ke/v1`

**Version Header** (optional):
```http
Accept: application/vnd.learnhub.v1+json
```

**Deprecated Endpoints**: Will be supported for 6 months after deprecation announcement

---

## Code Examples

### cURL

```bash
# Login
curl -X POST https://api.learnhub.ke/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@test.com","password":"password123"}'

# Get classes
curl -X GET https://api.learnhub.ke/v1/content/classes \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Submit quiz
curl -X POST https://api.learnhub.ke/v1/quiz/quiz789/submit \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"answers":{"q1":"x = 3","q2":"y = 7"}}'
```

### JavaScript (Fetch)

```javascript
// Login
const login = async (email, password) => {
  const response = await fetch('https://api.learnhub.ke/v1/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, password })
  });
  
  const data = await response.json();
  if (data.success) {
    localStorage.setItem('token', data.data.token);
    return data.data.user;
  }
  throw new Error(data.error.message);
};

// Get classes
const getClasses = async () => {
  const token = localStorage.getItem('token');
  const response = await fetch('https://api.learnhub.ke/v1/content/classes', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const data = await response.json();
  return data.data;
};
```

### Python (Requests)

```python
import requests

# Login
def login(email, password):
    url = 'https://api.learnhub.ke/v1/auth/login'
    payload = {'email': email, 'password': password}
    response = requests.post(url, json=payload)
    data = response.json()
    
    if data['success']:
        return data['data']['token']
    raise Exception(data['error']['message'])

# Get classes
def get_classes(token):
    url = 'https://api.learnhub.ke/v1/content/classes'
    headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(url, headers=headers)
    data = response.json()
    return data['data']
```

### Kotlin (Retrofit)

```kotlin
interface LearnHubApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>
    
    @GET("content/classes")
    suspend fun getClasses(): Response<ApiResponse<List<Class>>>
    
    @POST("quiz/{quizId}/submit")
    suspend fun submitQuiz(
        @Path("quizId") quizId: String,
        @Body answers: Map<String, String>
    ): Response<ApiResponse<QuizResult>>
}

// Usage with Coroutines
val token = authRepository.login(email, password)
val classes = contentRepository.getClasses()
```

---

## Webhooks (Future)

**Coming in v1.1**: Webhook support for real-time events

**Events**:
- `content.published`: New content available
- `user.progress.milestone`: User reaches milestone
- `quiz.completed`: Quiz submission processed
- `achievement.earned`: New achievement unlocked

---

## SDK/Libraries (Future)

**Official SDKs**:
- Kotlin/Android SDK (planned)
- JavaScript SDK (planned)
- Python SDK (planned)

---

## Support

**Documentation**: https://docs.learnhub.ke  
**API Status**: https://status.learnhub.ke  
**Developer Forum**: https://community.learnhub.ke  
**Email**: api@learnhub.ke

---

**API Version**: 1.0  
**Last Updated**: December 2024  
**Status**: Mock Implementation (Production API Coming Soon)
```

---
