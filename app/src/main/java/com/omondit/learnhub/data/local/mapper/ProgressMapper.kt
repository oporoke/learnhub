package com.omondit.learnhub.data.local.mapper

import com.omondit.learnhub.data.local.entity.ContentProgressEntity
import com.omondit.learnhub.data.local.entity.SubtopicProgressEntity
import com.omondit.learnhub.data.local.entity.TopicProgressEntity
import com.omondit.learnhub.domain.model.ContentProgress
import com.omondit.learnhub.domain.model.SubtopicProgress
import com.omondit.learnhub.domain.model.TopicProgress

// Content Progress
fun ContentProgressEntity.toDomain(): ContentProgress {
    return ContentProgress(
        userId = this.userId,
        contentId = this.contentId,
        subtopicId = this.subtopicId,
        completed = this.completed,
        completedAt = this.completedAt
    )
}

fun ContentProgress.toEntity(): ContentProgressEntity {
    return ContentProgressEntity(
        userId = this.userId,
        contentId = this.contentId,
        subtopicId = this.subtopicId,
        completed = this.completed,
        completedAt = this.completedAt
    )
}

// Subtopic Progress
fun SubtopicProgressEntity.toDomain(): SubtopicProgress {
    return SubtopicProgress(
        userId = this.userId,
        subtopicId = this.subtopicId,
        completionPercentage = this.completionPercentage,
        lastAccessedAt = this.lastAccessedAt
    )
}

fun SubtopicProgress.toEntity(): SubtopicProgressEntity {
    return SubtopicProgressEntity(
        id = "${this.userId}_${this.subtopicId}",
        userId = this.userId,
        subtopicId = this.subtopicId,
        completionPercentage = this.completionPercentage,
        lastAccessedAt = this.lastAccessedAt
    )
}

// Topic Progress
fun TopicProgressEntity.toDomain(): TopicProgress {
    return TopicProgress(
        userId = this.userId,
        topicId = this.topicId,
        completionPercentage = this.completionPercentage
    )
}

fun TopicProgress.toEntity(): TopicProgressEntity {
    return TopicProgressEntity(
        id = "${this.userId}_${this.topicId}",
        userId = this.userId,
        topicId = this.topicId,
        completionPercentage = this.completionPercentage
    )
}
