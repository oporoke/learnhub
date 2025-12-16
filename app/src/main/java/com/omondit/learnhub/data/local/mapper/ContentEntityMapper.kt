package com.omondit.learnhub.data.local.mapper

import com.omondit.learnhub.data.local.entity.ClassEntity
import com.omondit.learnhub.data.local.entity.ContentEntity
import com.omondit.learnhub.data.local.entity.SubjectEntity
import com.omondit.learnhub.data.local.entity.SubtopicEntity
import com.omondit.learnhub.data.local.entity.TopicEntity
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.ContentBody
import com.omondit.learnhub.domain.model.ContentStatus
import com.omondit.learnhub.domain.model.ContentType
import com.omondit.learnhub.domain.model.Curriculum
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic

// Class mappers
fun ClassEntity.toDomain(): CKlass {
    return CKlass(
        id = this.id,
        name = this.name,
        curriculum = when (this.curriculum.uppercase()) {
            "844", "8-4-4", "CURRICULUM_844" -> Curriculum.CURRICULUM_844
            "CBC" -> Curriculum.CBC
            else -> Curriculum.CURRICULUM_844
        }
    )
}

fun CKlass.toEntity(): ClassEntity {
    return ClassEntity(
        id = this.id,
        name = this.name,
        curriculum = this.curriculum.name
    )
}

// Subject mappers
fun SubjectEntity.toDomain(): Subject {
    return Subject(
        id = this.id,
        classId = this.classId,
        name = this.name,
        iconUrl = this.iconUrl
    )
}

fun Subject.toEntity(): SubjectEntity {
    return SubjectEntity(
        id = this.id,
        classId = this.classId,
        name = this.name,
        iconUrl = this.iconUrl
    )
}

// Topic mappers
fun TopicEntity.toDomain(): Topic {
    return Topic(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

fun Topic.toEntity(): TopicEntity {
    return TopicEntity(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

// Subtopic mappers
fun SubtopicEntity.toDomain(): Subtopic {
    return Subtopic(
        id = this.id,
        topicId = this.topicId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

fun Subtopic.toEntity(): SubtopicEntity {
    return SubtopicEntity(
        id = this.id,
        topicId = this.topicId,
        name = this.name,
        description = this.description,
        order = this.order
    )
}

// Content mappers
fun ContentEntity.toDomain(): Content {
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
            else -> ContentType.TEXT
        },
        body = ContentBody(
            text = this.bodyText,
            imageUrl = this.bodyImageUrl,
            videoUrl = this.bodyVideoUrl,
            pdfUrl = this.bodyPdfUrl
        ),
        status = when (this.status.uppercase()) {
            "DRAFT" -> ContentStatus.DRAFT
            "PENDING_APPROVAL", "PENDING" -> ContentStatus.PENDING_APPROVAL
            "PUBLISHED" -> ContentStatus.PUBLISHED
            "REJECTED" -> ContentStatus.REJECTED
            else -> ContentStatus.PUBLISHED
        },
        createdAt = this.createdAt
    )
}

fun Content.toEntity(): ContentEntity {
    return ContentEntity(
        id = this.id,
        subtopicId = this.subtopicId,
        creatorId = this.creatorId,
        contentType = this.contentType.name,
        bodyText = this.body.text,
        bodyImageUrl = this.body.imageUrl,
        bodyVideoUrl = this.body.videoUrl,
        status = this.status.name,
        bodyPdfUrl = this.body.pdfUrl,
        createdAt = this.createdAt
    )
}
