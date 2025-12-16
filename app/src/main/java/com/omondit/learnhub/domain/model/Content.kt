package com.omondit.learnhub.domain.model

data class Content(
    val id: String,
    val subtopicId: String,
    val creatorId: String,
    val contentType: ContentType,
    val body: ContentBody,
    val status: ContentStatus,
    val createdAt: Long
)

enum class ContentType {
    TEXT,
    IMAGE,
    VIDEO,
    TEXT_IMAGE,
    TEXT_VIDEO,
    PDF, // Add this
    TEXT_PDF // Add this
}

data class ContentBody(
    val text: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val pdfUrl: String? = null // Add this
)

enum class ContentStatus {
    DRAFT,
    PENDING_APPROVAL,
    PUBLISHED,
    REJECTED
}