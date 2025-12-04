package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic

interface ContentRepository {
    // Hierarchy navigation
    suspend fun getClasses(): Result<List<CKlass>>
    suspend fun getSubjects(classId: String): Result<List<Subject>>
    suspend fun getTopics(subjectId: String): Result<List<Topic>>
    suspend fun getSubtopics(topicId: String): Result<List<Subtopic>>

    // Content retrieval
    suspend fun getContent(subtopicId: String): Result<List<Content>>
    suspend fun getContentById(contentId: String): Result<Content>

    // Course creator operations
    suspend fun createContent(content: Content): Result<Content>
    suspend fun updateContent(content: Content): Result<Content>
    suspend fun submitForApproval(contentId: String): Result<Unit>
}