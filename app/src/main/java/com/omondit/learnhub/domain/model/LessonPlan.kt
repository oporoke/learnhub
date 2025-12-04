package com.omondit.learnhub.domain.model

data class LessonPlan(
    val id: String,
    val teacherId: String,
    val classId: String,
    val subjectId: String,
    val topicId: String,
    val subtopicId: String,
    val objectives: String,
    val notes: String,
    val createdAt: Long
)
