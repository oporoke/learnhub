package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.local.dao.ContentDao
import com.omondit.learnhub.data.local.mapper.toDomain
import com.omondit.learnhub.data.local.mapper.toEntity
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedContentRepositoryImpl @Inject constructor(
    private val contentDao: ContentDao,
    private val mockContentRepository: MockContentRepositoryImpl // For API simulation
) : ContentRepository {

    companion object {
        private const val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 24 hours
    }

    override suspend fun getClasses(): Result<List<CKlass>> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Try cache first
            val cachedClasses = contentDao.getClasses()

            if (cachedClasses.isNotEmpty() && !isCacheExpired(cachedClasses.first().cachedAt)) {
                // Return from cache
                Result.success(cachedClasses.map { it.toDomain() })
            } else {
                // Fetch from API (mock)
                val apiResult = mockContentRepository.getClasses()

                apiResult.fold(
                    onSuccess = { classes ->
                        // Save to cache
                        contentDao.clearAllClasses()
                        contentDao.insertClasses(classes.map { it.toEntity() })
                        Result.success(classes)
                    },
                    onFailure = { exception ->
                        // If API fails but we have cache, return cache
                        if (cachedClasses.isNotEmpty()) {
                            Result.success(cachedClasses.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSubjects(classId: String): Result<List<Subject>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cachedSubjects = contentDao.getSubjects(classId)

            if (cachedSubjects.isNotEmpty() && !isCacheExpired(cachedSubjects.first().cachedAt)) {
                Result.success(cachedSubjects.map { it.toDomain() })
            } else {
                val apiResult = mockContentRepository.getSubjects(classId)

                apiResult.fold(
                    onSuccess = { subjects ->
                        contentDao.clearSubjects(classId)
                        contentDao.insertSubjects(subjects.map { it.toEntity() })
                        Result.success(subjects)
                    },
                    onFailure = { exception ->
                        if (cachedSubjects.isNotEmpty()) {
                            Result.success(cachedSubjects.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopics(subjectId: String): Result<List<Topic>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cachedTopics = contentDao.getTopics(subjectId)

            if (cachedTopics.isNotEmpty() && !isCacheExpired(cachedTopics.first().cachedAt)) {
                Result.success(cachedTopics.map { it.toDomain() })
            } else {
                val apiResult = mockContentRepository.getTopics(subjectId)

                apiResult.fold(
                    onSuccess = { topics ->
                        contentDao.clearTopics(subjectId)
                        contentDao.insertTopics(topics.map { it.toEntity() })
                        Result.success(topics)
                    },
                    onFailure = { exception ->
                        if (cachedTopics.isNotEmpty()) {
                            Result.success(cachedTopics.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSubtopics(topicId: String): Result<List<Subtopic>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cachedSubtopics = contentDao.getSubtopics(topicId)

            if (cachedSubtopics.isNotEmpty() && !isCacheExpired(cachedSubtopics.first().cachedAt)) {
                Result.success(cachedSubtopics.map { it.toDomain() })
            } else {
                val apiResult = mockContentRepository.getSubtopics(topicId)

                apiResult.fold(
                    onSuccess = { subtopics ->
                        contentDao.clearSubtopics(topicId)
                        contentDao.insertSubtopics(subtopics.map { it.toEntity() })
                        Result.success(subtopics)
                    },
                    onFailure = { exception ->
                        if (cachedSubtopics.isNotEmpty()) {
                            Result.success(cachedSubtopics.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContent(subtopicId: String): Result<List<Content>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cachedContent = contentDao.getContent(subtopicId)

            if (cachedContent.isNotEmpty() && !isCacheExpired(cachedContent.first().cachedAt)) {
                Result.success(cachedContent.map { it.toDomain() })
            } else {
                val apiResult = mockContentRepository.getContent(subtopicId)

                apiResult.fold(
                    onSuccess = { content ->
                        contentDao.clearContent(subtopicId)
                        contentDao.insertContent(content.map { it.toEntity() })
                        Result.success(content)
                    },
                    onFailure = { exception ->
                        if (cachedContent.isNotEmpty()) {
                            Result.success(cachedContent.map { it.toDomain() })
                        } else {
                            Result.failure(exception)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContentById(contentId: String): Result<Content> = withContext(Dispatchers.IO) {
        return@withContext try {
            val cachedContent = contentDao.getContentById(contentId)

            if (cachedContent != null) {
                Result.success(cachedContent.toDomain())
            } else {
                mockContentRepository.getContentById(contentId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createContent(content: Content): Result<Content> {
        return mockContentRepository.createContent(content)
    }

    override suspend fun updateContent(content: Content): Result<Content> {
        return mockContentRepository.updateContent(content)
    }

    override suspend fun submitForApproval(contentId: String): Result<Unit> {
        return mockContentRepository.submitForApproval(contentId)
    }

    private fun isCacheExpired(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt > CACHE_EXPIRY_MS
    }
}
