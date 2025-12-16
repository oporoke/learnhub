package com.omondit.learnhub.data.local.dao

import androidx.room.*
import com.omondit.learnhub.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmarks WHERE userId = :userId ORDER BY bookmarkedAt DESC")
    fun getBookmarksFlow(userId: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE userId = :userId ORDER BY bookmarkedAt DESC")
    suspend fun getBookmarks(userId: String): List<BookmarkEntity>

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: String): BookmarkEntity?

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    suspend fun isBookmarked(id: String): Boolean

    @Query("DELETE FROM bookmarks WHERE userId = :userId")
    suspend fun clearBookmarks(userId: String)
}
