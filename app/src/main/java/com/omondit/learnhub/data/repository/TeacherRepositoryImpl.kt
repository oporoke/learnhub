package com.omondit.learnhub.data.repository

import com.omondit.learnhub.domain.model.GeneratedExam
import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.model.SchemeOfWork
import com.omondit.learnhub.domain.repository.TeacherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepositoryImpl @Inject constructor(
    // Will add API service later
) : TeacherRepository {

    override suspend fun createLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan> {
        // TODO: Implement with API
        return Result.success(lessonPlan)
    }

    override suspend fun getLessonPlans(teacherId: String): Result<List<LessonPlan>> {
        // TODO: Implement with API
        return Result.success(emptyList())
    }

    override suspend fun updateLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan> {
        // TODO: Implement with API
        return Result.success(lessonPlan)
    }

    override suspend fun deleteLessonPlan(planId: String): Result<Unit> {
        // TODO: Implement with API
        return Result.success(Unit)
    }

    override suspend fun generateSchemeOfWork(
        teacherId: String,
        classId: String,
        subjectId: String,
        term: Int
    ): Result<SchemeOfWork> {
        // TODO: Implement with API
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun saveSchemeOfWork(scheme: SchemeOfWork): Result<SchemeOfWork> {
        // TODO: Implement with API
        return Result.success(scheme)
    }

    override suspend fun saveExam(exam: GeneratedExam): Result<GeneratedExam> {
        // TODO: Implement with API
        return Result.success(exam)
    }

    override suspend fun getExams(teacherId: String): Result<List<GeneratedExam>> {
        // TODO: Implement with API
        return Result.success(emptyList())
    }

    override suspend fun exportExamToPdf(examId: String): Result<String> {
        // TODO: Implement with API
        return Result.failure(Exception("Not implemented"))
    }
}
