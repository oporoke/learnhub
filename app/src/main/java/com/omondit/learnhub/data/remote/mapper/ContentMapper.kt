package com.omondit.learnhub.data.remote.mapper

import com.omondit.learnhub.data.remote.dto.ClassDto
import com.omondit.learnhub.data.remote.dto.ContentBodyDto
import com.omondit.learnhub.data.remote.dto.ContentDto
import com.omondit.learnhub.data.remote.dto.SubjectDto
import com.omondit.learnhub.data.remote.dto.SubtopicDto
import com.omondit.learnhub.data.remote.dto.TopicDto
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.ContentBody
import com.omondit.learnhub.domain.model.ContentStatus
import com.omondit.learnhub.domain.model.ContentType
import com.omondit.learnhub.domain.model.Curriculum
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic

// Class Mapper
fun ClassDto.toDomain(): CKlass {
    return CKlass(
        id = this.id,
        name = this.name,
        curriculum = when (this.curriculum.uppercase()) {
            "844", "8-4-4", "CURRICULUM_844" -> Curriculum.CURRICULUM_844
            "CBC" -> Curriculum.CBC
            else -> throw IllegalArgumentException("Unknown curriculum: ${this.curriculum}")
        }
    )
}

fun CKlass.toDto(): ClassDto {
    return ClassDto(
        id = this.id,
        name = this.name,
        curriculum = this.curriculum.name
    )
}

// Subject Mapper
fun SubjectDto.toDomain(): Subject {
    return Subject(
        id = this.id,
        classId = this.classId,
        name = this.name,
        iconUrl = this.iconUrl
    )
}

fun Subject.toDto(): SubjectDto {
    return SubjectDto(
        id = this.id,
        classId = this.classId,
        name = this.name,
        iconUrl = this.iconUrl
    )
}

// Topic Mapper
fun TopicDto.toDomain(): Topic {
    return Topic(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

fun Topic.toDto(): TopicDto {
    return TopicDto(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

// Subtopic Mapper
fun SubtopicDto.toDomain(): Subtopic {
    return Subtopic(
        id = this.id,
        topicId = this.topicId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

fun Subtopic.toDto(): SubtopicDto {
    return SubtopicDto(
        id = this.id,
        topicId = this.topicId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

// Content Mapper
fun ContentDto.toDomain(): Content {
    return Content(
        id = this.id,
        subtopicId = this.subtopicId,
        creatorId = this.creatorId,
        contentType = when (this.contentType.uppercase()) {
            "TEXT" -> ContentType.TEXT
            "IMAGE" -> ContentType.IMAGE
            "VIDEO" -> ContentType.VIDEO
            "TEXT_IMAGE" -> ContentType.TEXT_IMAGE
            "TEXT_VIDEO" -> ContentType.TEXT_VIDEO
            else -> throw IllegalArgumentException("Unknown content type: ${this.contentType}")
        },
        body = this.body.toDomain(),
        status = when (this.status.uppercase()) {
            "DRAFT" -> ContentStatus.DRAFT
            "PENDING_APPROVAL", "PENDING" -> ContentStatus.PENDING_APPROVAL
            "PUBLISHED" -> ContentStatus.PUBLISHED
            "REJECTED" -> ContentStatus.REJECTED
            else -> throw IllegalArgumentException("Unknown status: ${this.status}")
        },
        createdAt = this.createdAt
    )
}

fun ContentBodyDto.toDomain(): ContentBody {
    return ContentBody(
        text = this.text,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl
    )
}

fun Content.toDto(): ContentDto {
    return ContentDto(
        id = this.id,
        subtopicId = this.subtopicId,
        creatorId = this.creatorId,
        contentType = this.contentType.name,
        body = this.body.toDto(),
        status = this.status.name,
        createdAt = this.createdAt
    )
}

fun ContentBody.toDto(): ContentBodyDto {
    return ContentBodyDto(
        text = this.text,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl
    )
}
