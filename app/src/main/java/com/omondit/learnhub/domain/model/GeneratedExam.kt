package com.omondit.learnhub.domain.model

data class GeneratedExam(
    val id: String,
    val teacherId: String,
    val classId: String,
    val examType: ExamType,
    val questions: List<String>, // Question IDs
    val createdAt: Long,
    val pdfUrl: String? = null
)

enum class ExamType {
    CAT,
    MIDTERM,
    END_TERM,
    CUSTOM
}