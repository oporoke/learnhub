package com.omondit.learnhub.domain.repository

data class SearchResult(
    val type: SearchResultType,
    val id: String,
    val title: String,
    val description: String,
    val classId: String,
    val subjectId: String?,
    val topicId: String?,
    val subtopicId: String?
)

enum class SearchResultType {
    TOPIC,
    SUBTOPIC,
    CONTENT
}

interface SearchRepository {
    suspend fun search(query: String, classId: String? = null): Result<List<SearchResult>>
}
