package com.omondit.learnhub.domain.model


data class SchemeOfWork(
    val id: String,
    val teacherId: String,
    val classId: String,
    val subjectId: String,
    val term: Int,
    val weeks: List<WeekPlan>
)
data class WeekPlan(
    val weekNumber: Int,
    val topicId: String,
    val objectives: String
)
