package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.ContentProgress
import com.omondit.learnhub.domain.model.SubtopicProgress
import com.omondit.learnhub.domain.model.TopicProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    // Content Progress
    suspend fun markContentComplete(
        userId: String,
        contentId: String,
        subtopicId: String
    ): Result<Unit>

    suspend fun getContentProgress(
        userId: String,
        contentId: String
    ): ContentProgress?

    // Subtopic Progress
    suspend fun updateSubtopicProgress(
        userId: String,
        subtopicId: String,
        totalContent: Int,
        completedContent: Int
    ): Result<Unit>

    suspend fun getSubtopicProgress(
        userId: String,
        subtopicId: String
    ): SubtopicProgress?

    fun observeSubtopicProgress(
        userId: String,
        subtopicIds: List<String>
    ): Flow<List<SubtopicProgress>>

    // Topic Progress
    suspend fun updateTopicProgress(
        userId: String,
        topicId: String,
        completionPercentage: Float
    ): Result<Unit>

    suspend fun getTopicProgress(
        userId: String,
        topicId: String
    ): TopicProgress?

    fun observeTopicProgress(
        userId: String,
        topicIds: List<String>
    ): Flow<List<TopicProgress>>

    // Clear progress
    suspend fun clearUserProgress(userId: String): Result<Unit>
}
