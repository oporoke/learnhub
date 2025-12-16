package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.Bookmark
import com.omondit.learnhub.domain.model.BookmarkType
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun addBookmark(
        userId: String,
        itemType: BookmarkType,
        itemId: String,
        itemTitle: String,
        itemDescription: String
    ): Result<Unit>

    suspend fun removeBookmark(userId: String, itemType: BookmarkType, itemId: String): Result<Unit>

    fun observeBookmarks(userId: String): Flow<List<Bookmark>>

    suspend fun getBookmarks(userId: String): Result<List<Bookmark>>

    suspend fun isBookmarked(userId: String, itemType: BookmarkType, itemId: String): Boolean

    suspend fun toggleBookmark(
        userId: String,
        itemType: BookmarkType,
        itemId: String,
        itemTitle: String,
        itemDescription: String
    ): Result<Boolean> // Returns true if now bookmarked, false if removed
}
