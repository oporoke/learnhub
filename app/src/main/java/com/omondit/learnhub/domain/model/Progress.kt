package com.omondit.learnhub.domain.model

data class ContentProgress(
    val userId: String,
    val contentId: String,
    val subtopicId: String,
    val completed: Boolean = false,
    val completedAt: Long? = null
)

data class SubtopicProgress(
    val userId: String,
    val subtopicId: String,
    val completionPercentage: Float = 0f,
    val lastAccessedAt: Long = System.currentTimeMillis()
)

data class TopicProgress(
    val userId: String,
    val topicId: String,
    val completionPercentage: Float = 0f
)
