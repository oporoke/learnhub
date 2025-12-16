package com.omondit.learnhub.data.repository


import com.omondit.learnhub.data.local.dao.BookmarkDao
import com.omondit.learnhub.data.local.entity.BookmarkEntity
import com.omondit.learnhub.domain.model.Bookmark
import com.omondit.learnhub.domain.model.BookmarkType
import com.omondit.learnhub.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override suspend fun addBookmark(
        userId: String,
        itemType: BookmarkType,
        itemId: String,
        itemTitle: String,
        itemDescription: String
    ): Result<Unit> {
        return try {
            val id = "${userId}_${itemType.name}_${itemId}"
            val bookmark = BookmarkEntity(
                id = id,
                userId = userId,
                itemType = itemType.name,
                itemId = itemId,
                itemTitle = itemTitle,
                itemDescription = itemDescription
            )
            bookmarkDao.insertBookmark(bookmark)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeBookmark(
        userId: String,
        itemType: BookmarkType,
        itemId: String
    ): Result<Unit> {
        return try {
            val id = "${userId}_${itemType.name}_${itemId}"
            bookmarkDao.deleteBookmarkById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeBookmarks(userId: String): Flow<List<Bookmark>> {
        return bookmarkDao.getBookmarksFlow(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getBookmarks(userId: String): Result<List<Bookmark>> {
        return try {
            val bookmarks = bookmarkDao.getBookmarks(userId).map { it.toDomain() }
            Result.success(bookmarks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isBookmarked(
        userId: String,
        itemType: BookmarkType,
        itemId: String
    ): Boolean {
        return try {
            val id = "${userId}_${itemType.name}_${itemId}"
            bookmarkDao.isBookmarked(id)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun toggleBookmark(
        userId: String,
        itemType: BookmarkType,
        itemId: String,
        itemTitle: String,
        itemDescription: String
    ): Result<Boolean> {
        return try {
            val id = "${userId}_${itemType.name}_${itemId}"
            val isCurrentlyBookmarked = bookmarkDao.isBookmarked(id)

            if (isCurrentlyBookmarked) {
                bookmarkDao.deleteBookmarkById(id)
                Result.success(false)
            } else {
                val bookmark = BookmarkEntity(
                    id = id,
                    userId = userId,
                    itemType = itemType.name,
                    itemId = itemId,
                    itemTitle = itemTitle,
                    itemDescription = itemDescription
                )
                bookmarkDao.insertBookmark(bookmark)
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun BookmarkEntity.toDomain(): Bookmark {
        return Bookmark(
            id = this.id,
            userId = this.userId,
            itemType = BookmarkType.valueOf(this.itemType),
            itemId = this.itemId,
            itemTitle = this.itemTitle,
            itemDescription = this.itemDescription,
            bookmarkedAt = this.bookmarkedAt
        )
    }
}
