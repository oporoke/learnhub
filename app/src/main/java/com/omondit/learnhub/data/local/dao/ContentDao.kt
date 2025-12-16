package com.omondit.learnhub.data.local.dao

import androidx.room.*
import com.omondit.learnhub.data.local.entity.ClassEntity
import com.omondit.learnhub.data.local.entity.ContentEntity
import com.omondit.learnhub.data.local.entity.SubjectEntity
import com.omondit.learnhub.data.local.entity.SubtopicEntity
import com.omondit.learnhub.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

    // Classes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classes: List<ClassEntity>)

    @Query("SELECT * FROM classes")
    suspend fun getClasses(): List<ClassEntity>

    @Query("SELECT * FROM classes")
    fun getClassesFlow(): Flow<List<ClassEntity>>

    @Query("DELETE FROM classes")
    suspend fun clearClasses()

    // Subjects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Query("SELECT * FROM subjects WHERE classId = :classId")
    suspend fun getSubjects(classId: String): List<SubjectEntity>

    @Query("SELECT * FROM subjects WHERE classId = :classId")
    fun getSubjectsFlow(classId: String): Flow<List<SubjectEntity>>

    @Query("DELETE FROM subjects WHERE classId = :classId")
    suspend fun clearSubjects(classId: String)

    // Topics
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY `order`")
    suspend fun getTopics(subjectId: String): List<TopicEntity>

    @Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY `order`")
    fun getTopicsFlow(subjectId: String): Flow<List<TopicEntity>>

    @Query("DELETE FROM topics WHERE subjectId = :subjectId")
    suspend fun clearTopics(subjectId: String)

    // Subtopics
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtopics(subtopics: List<SubtopicEntity>)

    @Query("SELECT * FROM subtopics WHERE topicId = :topicId ORDER BY `order`")
    suspend fun getSubtopics(topicId: String): List<SubtopicEntity>

    @Query("SELECT * FROM subtopics WHERE topicId = :topicId ORDER BY `order`")
    fun getSubtopicsFlow(topicId: String): Flow<List<SubtopicEntity>>

    @Query("DELETE FROM subtopics WHERE topicId = :topicId")
    suspend fun clearSubtopics(topicId: String)

    // Content
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: List<ContentEntity>)

    @Query("SELECT * FROM content WHERE subtopicId = :subtopicId")
    suspend fun getContent(subtopicId: String): List<ContentEntity>

    @Query("SELECT * FROM content WHERE subtopicId = :subtopicId")
    fun getContentFlow(subtopicId: String): Flow<List<ContentEntity>>

    @Query("SELECT * FROM content WHERE id = :contentId")
    suspend fun getContentById(contentId: String): ContentEntity?

    @Query("DELETE FROM content WHERE subtopicId = :subtopicId")
    suspend fun clearContent(subtopicId: String)

    // Cache management
    @Query("DELETE FROM classes")
    suspend fun clearAllClasses()

    @Query("DELETE FROM subjects")
    suspend fun clearAllSubjects()

    @Query("DELETE FROM topics")
    suspend fun clearAllTopics()

    @Query("DELETE FROM subtopics")
    suspend fun clearAllSubtopics()

    @Query("DELETE FROM content")
    suspend fun clearAllContent()
}
