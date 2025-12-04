package com.omondit.learnhub.data.local.dao

import com.omondit.learnhub.data.local.entity.TopicProgressEntity
import androidx.room.*
import com.omondit.learnhub.data.local.entity.ContentProgressEntity
import com.omondit.learnhub.data.local.entity.SubtopicProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    // Content Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentProgress(progress: ContentProgressEntity)

    @Query("SELECT * FROM content_progress WHERE userId = :userId AND contentId = :contentId")
    suspend fun getContentProgress(userId: String, contentId: String): ContentProgressEntity?

    @Query("SELECT * FROM content_progress WHERE userId = :userId AND subtopicId = :subtopicId")
    suspend fun getContentProgressForSubtopic(userId: String, subtopicId: String): List<ContentProgressEntity>

    @Query("UPDATE content_progress SET completed = :completed, completedAt = :completedAt WHERE userId = :userId AND contentId = :contentId")
    suspend fun markContentComplete(userId: String, contentId: String, completed: Boolean, completedAt: Long?)

    // Subtopic Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtopicProgress(progress: SubtopicProgressEntity)

    @Query("SELECT * FROM subtopic_progress WHERE userId = :userId AND subtopicId = :subtopicId")
    suspend fun getSubtopicProgress(userId: String, subtopicId: String): SubtopicProgressEntity?

    @Query("SELECT * FROM subtopic_progress WHERE userId = :userId AND subtopicId IN (:subtopicIds)")
    fun getSubtopicProgressFlow(userId: String, subtopicIds: List<String>): Flow<List<SubtopicProgressEntity>>

    // Topic Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopicProgress(progress: TopicProgressEntity)

    @Query("SELECT * FROM topic_progress WHERE userId = :userId AND topicId = :topicId")
    suspend fun getTopicProgress(userId: String, topicId: String): TopicProgressEntity?

    @Query("SELECT * FROM topic_progress WHERE userId = :userId AND topicId IN (:topicIds)")
    fun getTopicProgressFlow(userId: String, topicIds: List<String>): Flow<List<TopicProgressEntity>>

    // Clear all progress (for logout)
    @Query("DELETE FROM content_progress WHERE userId = :userId")
    suspend fun clearUserContentProgress(userId: String)

    @Query("DELETE FROM subtopic_progress WHERE userId = :userId")
    suspend fun clearUserSubtopicProgress(userId: String)

    @Query("DELETE FROM topic_progress WHERE userId = :userId")
    suspend fun clearUserTopicProgress(userId: String)
}
