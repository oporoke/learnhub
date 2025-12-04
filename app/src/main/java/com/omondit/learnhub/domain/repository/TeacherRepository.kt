package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.GeneratedExam
import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.model.SchemeOfWork

interface TeacherRepository {
    // Lesson planning
    suspend fun createLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan>
    suspend fun getLessonPlans(teacherId: String): Result<List<LessonPlan>>
    suspend fun updateLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan>
    suspend fun deleteLessonPlan(planId: String): Result<Unit>

    // Scheme of work
    suspend fun generateSchemeOfWork(
        teacherId: String,
        classId: String,
        subjectId: String,
        term: Int
    ): Result<SchemeOfWork>

    suspend fun saveSchemeOfWork(scheme: SchemeOfWork): Result<SchemeOfWork>

    // Exam management
    suspend fun saveExam(exam: GeneratedExam): Result<GeneratedExam>
    suspend fun getExams(teacherId: String): Result<List<GeneratedExam>>
    suspend fun exportExamToPdf(examId: String): Result<String> // Returns PDF URL
}