package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.local.dao.ContentDao
import com.omondit.learnhub.domain.repository.SearchRepository
import com.omondit.learnhub.domain.repository.SearchResult
import com.omondit.learnhub.domain.repository.SearchResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val contentDao: ContentDao
) : SearchRepository {

    override suspend fun search(query: String, classId: String?): Result<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                if (query.isBlank()) {
                    return@withContext Result.success(emptyList())
                }

                val results = mutableListOf<SearchResult>()
                val lowerQuery = query.lowercase()

                // Search topics
                val allTopics = contentDao.getTopics("") // Gets all topics
                allTopics.forEach { topicEntity ->
                    if (topicEntity.name.lowercase().contains(lowerQuery) ||
                        topicEntity.description.lowercase().contains(lowerQuery)
                    ) {
                        results.add(
                            SearchResult(
                                type = SearchResultType.TOPIC,
                                id = topicEntity.id,
                                title = topicEntity.name,
                                description = topicEntity.description,
                                classId = "", // Would need to join with subject
                                subjectId = topicEntity.subjectId,
                                topicId = topicEntity.id,
                                subtopicId = null
                            )
                        )
                    }
                }

                // Search subtopics
                val allSubtopics = contentDao.getSubtopics("") // Gets all subtopics
                allSubtopics.forEach { subtopicEntity ->
                    if (subtopicEntity.name.lowercase().contains(lowerQuery) ||
                        subtopicEntity.description.lowercase().contains(lowerQuery)
                    ) {
                        results.add(
                            SearchResult(
                                type = SearchResultType.SUBTOPIC,
                                id = subtopicEntity.id,
                                title = subtopicEntity.name,
                                description = subtopicEntity.description,
                                classId = "",
                                subjectId = null,
                                topicId = subtopicEntity.topicId,
                                subtopicId = subtopicEntity.id
                            )
                        )
                    }
                }

                // Search content
                val allContent = contentDao.getContent("") // Gets all content
                allContent.forEach { contentEntity ->
                    contentEntity.bodyText?.let { text ->
                        if (text.lowercase().contains(lowerQuery)) {
                            results.add(
                                SearchResult(
                                    type = SearchResultType.CONTENT,
                                    id = contentEntity.id,
                                    title = "Content in ${contentEntity.subtopicId}",
                                    description = text.take(100) + if (text.length > 100) "..." else "",
                                    classId = "",
                                    subjectId = null,
                                    topicId = null,
                                    subtopicId = contentEntity.subtopicId
                                )
                            )
                        }
                    }
                }

                Result.success(results.take(20)) // Limit to 20 results
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
