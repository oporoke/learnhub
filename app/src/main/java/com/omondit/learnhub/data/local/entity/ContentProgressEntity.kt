package com.omondit.learnhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_progress")
data class ContentProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val contentId: String,
    val subtopicId: String,
    val completed: Boolean = false,
    val completedAt: Long? = null
)

@Entity(tableName = "subtopic_progress")
data class SubtopicProgressEntity(
    @PrimaryKey
    val id: String, // Format: "${userId}_${subtopicId}"
    val userId: String,
    val subtopicId: String,
    val completionPercentage: Float = 0f,
    val lastAccessedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey
    val id: String, // Format: "${userId}_${topicId}"
    val userId: String,
    val topicId: String,
    val completionPercentage: Float = 0f
)
