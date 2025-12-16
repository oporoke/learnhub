package com.omondit.learnhub.domain.model

data class StudentAnalytics(
    val userId: String,
    val totalTopics: Int,
    val completedTopics: Int,
    val totalSubtopics: Int,
    val completedSubtopics: Int,
    val totalContent: Int,
    val completedContent: Int,
    val quizzesTaken: Int,
    val averageQuizScore: Float,
    val studyStreak: Int, // Days
    val totalStudyTimeMinutes: Int,
    val topicProgress: List<TopicAnalytics>
)

data class TopicAnalytics(
    val topicId: String,
    val topicName: String,
    val subjectName: String,
    val completionPercentage: Float,
    val contentCompleted: Int,
    val contentTotal: Int,
    val lastAccessedAt: Long?
)

data class WeeklyProgress(
    val dayOfWeek: String,
    val contentCompleted: Int,
    val minutesStudied: Int
)
