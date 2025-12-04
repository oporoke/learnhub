package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.local.dao.ProgressDao
import com.omondit.learnhub.data.local.entity.ContentProgressEntity
import com.omondit.learnhub.data.local.entity.SubtopicProgressEntity
import com.omondit.learnhub.data.local.entity.TopicProgressEntity
import com.omondit.learnhub.data.local.mapper.toDomain
import com.omondit.learnhub.domain.model.ContentProgress
import com.omondit.learnhub.domain.model.SubtopicProgress
import com.omondit.learnhub.domain.model.TopicProgress
import com.omondit.learnhub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : ProgressRepository {

    override suspend fun markContentComplete(
        userId: String,
        contentId: String,
        subtopicId: String
    ): Result<Unit> {
        return try {
            val entity = ContentProgressEntity(
                userId = userId,
                contentId = contentId,
                subtopicId = subtopicId,
                completed = true,
                completedAt = System.currentTimeMillis()
            )
            progressDao.insertContentProgress(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContentProgress(
        userId: String,
        contentId: String
    ): ContentProgress? {
        return progressDao.getContentProgress(userId, contentId)?.toDomain()
    }

    override suspend fun updateSubtopicProgress(
        userId: String,
        subtopicId: String,
        totalContent: Int,
        completedContent: Int
    ): Result<Unit> {
        return try {
            val percentage = if (totalContent > 0) {
                (completedContent.toFloat() / totalContent) * 100f
            } else {
                0f
            }

            val entity = SubtopicProgressEntity(
                id = "${userId}_${subtopicId}",
                userId = userId,
                subtopicId = subtopicId,
                completionPercentage = percentage,
                lastAccessedAt = System.currentTimeMillis()
            )
            progressDao.insertSubtopicProgress(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSubtopicProgress(
        userId: String,
        subtopicId: String
    ): SubtopicProgress? {
        return progressDao.getSubtopicProgress(userId, subtopicId)?.toDomain()
    }

    override fun observeSubtopicProgress(
        userId: String,
        subtopicIds: List<String>
    ): Flow<List<SubtopicProgress>> {
        return progressDao.getSubtopicProgressFlow(userId, subtopicIds)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun updateTopicProgress(
        userId: String,
        topicId: String,
        completionPercentage: Float
    ): Result<Unit> {
        return try {
            val entity = TopicProgressEntity(
                id = "${userId}_${topicId}",
                userId = userId,
                topicId = topicId,
                completionPercentage = completionPercentage
            )
            progressDao.insertTopicProgress(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopicProgress(
        userId: String,
        topicId: String
    ): TopicProgress? {
        return progressDao.getTopicProgress(userId, topicId)?.toDomain()
    }

    override fun observeTopicProgress(
        userId: String,
        topicIds: List<String>
    ): Flow<List<TopicProgress>> {
        return progressDao.getTopicProgressFlow(userId, topicIds)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun clearUserProgress(userId: String): Result<Unit> {
        return try {
            progressDao.clearUserContentProgress(userId)
            progressDao.clearUserSubtopicProgress(userId)
            progressDao.clearUserTopicProgress(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
