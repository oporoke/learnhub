package com.omondit.learnhub.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val curriculum: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "subjects",
    indices = [Index(value = ["classId"])]
)
data class SubjectEntity(
    @PrimaryKey
    val id: String,
    val classId: String,
    val name: String,
    val iconUrl: String?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "topics",
    indices = [Index(value = ["subjectId"]), Index(value = ["order"])]
)
data class TopicEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val name: String,
    val description: String,
    val order: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "subtopics",
    indices = [Index(value = ["topicId"]), Index(value = ["order"])]
)
data class SubtopicEntity(
    @PrimaryKey
    val id: String,
    val topicId: String,
    val name: String,
    val description: String,
    val order: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "content",
    indices = [Index(value = ["subtopicId"]), Index(value = ["creatorId"])]
)
data class ContentEntity(
    @PrimaryKey
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val contentType: String,
    val bodyText: String?,
    val bodyImageUrl: String?,
    val bodyVideoUrl: String?,
    val bodyPdfUrl: String?,
    val status: String,
    val createdAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)
