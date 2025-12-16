package com.omondit.learnhub.domain.model

data class Bookmark(
    val id: String,
    val userId: String,
    val itemType: BookmarkType,
    val itemId: String,
    val itemTitle: String,
    val itemDescription: String,
    val bookmarkedAt: Long
)

enum class BookmarkType {
    TOPIC,
    SUBTOPIC
}
