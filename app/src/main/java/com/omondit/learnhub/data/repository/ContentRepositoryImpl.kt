package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.remote.api.ContentApiService
import com.omondit.learnhub.data.remote.mapper.toDomain
import com.omondit.learnhub.data.remote.mapper.toDto
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.repository.ContentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val contentApiService: ContentApiService
) : ContentRepository {

    override suspend fun getClasses(): Result<List<CKlass>> {
        return try {
            val response = contentApiService.getClasses()

            if (response.success && response.data != null) {
                val classes = response.data.map { it.toDomain() }
                Result.success(classes)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch classes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSubjects(classId: String): Result<List<Subject>> {
        return try {
            val response = contentApiService.getSubjects(classId)

            if (response.success && response.data != null) {
                val subjects = response.data.map { it.toDomain() }
                Result.success(subjects)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch subjects"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopics(subjectId: String): Result<List<Topic>> {
        return try {
            val response = contentApiService.getTopics(subjectId)

            if (response.success && response.data != null) {
                val topics = response.data.map { it.toDomain() }
                Result.success(topics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch topics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSubtopics(topicId: String): Result<List<Subtopic>> {
        return try {
            val response = contentApiService.getSubtopics(topicId)

            if (response.success && response.data != null) {
                val subtopics = response.data.map { it.toDomain() }
                Result.success(subtopics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch subtopics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContent(subtopicId: String): Result<List<Content>> {
        return try {
            val response = contentApiService.getContent(subtopicId)

            if (response.success && response.data != null) {
                val content = response.data.map { it.toDomain() }
                Result.success(content)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch content"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContentById(contentId: String): Result<Content> {
        return try {
            val response = contentApiService.getContentById(contentId)

            if (response.success && response.data != null) {
                val content = response.data.toDomain()
                Result.success(content)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch content"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createContent(content: Content): Result<Content> {
        return try {
            val response = contentApiService.createContent(content.toDto())

            if (response.success && response.data != null) {
                val createdContent = response.data.toDomain()
                Result.success(createdContent)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create content"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateContent(content: Content): Result<Content> {
        return try {
            val response = contentApiService.updateContent(content.id, content.toDto())

            if (response.success && response.data != null) {
                val updatedContent = response.data.toDomain()
                Result.success(updatedContent)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update content"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitForApproval(contentId: String): Result<Unit> {
        return try {
            // This would need a specific API endpoint
            // For now, returning success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
