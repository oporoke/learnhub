package com.omondit.learnhub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClassDto(
    val id: String,
    val name: String,
    val curriculum: String
)

@Serializable
data class SubjectDto(
    val id: String,
    val classId: String,
    val name: String,
    val iconUrl: String? = null
)

@Serializable
data class TopicDto(
    val id: String,
    val subjectId: String,
    val name: String,
    val description: String,
    val order: Int
)

@Serializable
data class SubtopicDto(
    val id: String,
    val topicId: String,
    val name: String,
    val description: String,
    val order: Int
)

@Serializable
data class ContentDto(
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val contentType: String,
    val body: ContentBodyDto,
    val status: String,
    val createdAt: Long
)

@Serializable
data class ContentBodyDto(
    val text: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null
)