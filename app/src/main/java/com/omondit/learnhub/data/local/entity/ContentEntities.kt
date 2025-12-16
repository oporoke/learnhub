package com.omondit.learnhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val curriculum: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey
    val id: String,
    val classId: String,
    val name: String,
    val iconUrl: String?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val name: String,
    val description: String,
    val order: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "subtopics")
data class SubtopicEntity(
    @PrimaryKey
    val id: String,
    val topicId: String,
    val name: String,
    val description: String,
    val order: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "content")
data class ContentEntity(
    @PrimaryKey
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val contentType: String,
    val bodyText: String?,
    val bodyImageUrl: String?,
    val bodyVideoUrl: String?,
    val bodyPdfUrl: String?, // Add this
    val status: String,
    val createdAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)
