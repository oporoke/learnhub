package com.omondit.learnhub.data.repository

import com.omondit.learnhub.domain.model.GeneratedExam
import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.model.SchemeOfWork
import com.omondit.learnhub.domain.repository.TeacherRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepositoryImpl @Inject constructor(
    // Will add API service later
) : TeacherRepository {

    // Mock lesson plans storage
    private val mockLessonPlans = mutableListOf(
        LessonPlan(
            id = "plan1",
            teacherId = "2", // teacher@test.com user ID
            classId = "form3",
            subjectId = "math",
            topicId = "algebra",
            subtopicId = "linear_eq",
            objectives = "Students will be able to solve linear equations using algebraic methods",
            notes = "Focus on practical examples. Use whiteboard for demonstrations.",
            createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
        ),
        LessonPlan(
            id = "plan2",
            teacherId = "2",
            classId = "form3",
            subjectId = "chem",
            topicId = "organic",
            subtopicId = "organic",
            objectives = "Understand basic concepts of organic chemistry and carbon bonding",
            notes = "Bring molecular model kits. Lab safety review required.",
            createdAt = System.currentTimeMillis() - 172800000 // 2 days ago
        )
    )

    override suspend fun createLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan> {
        delay(500)

        val newPlan = lessonPlan.copy(
            id = "plan_${System.currentTimeMillis()}",
            createdAt = System.currentTimeMillis()
        )
        mockLessonPlans.add(0, newPlan) // Add to beginning

        return Result.success(newPlan)
    }

    override suspend fun getLessonPlans(teacherId: String): Result<List<LessonPlan>> {
        delay(500)

        val plans = mockLessonPlans.filter { it.teacherId == teacherId }
        return Result.success(plans)
    }

    override suspend fun updateLessonPlan(lessonPlan: LessonPlan): Result<LessonPlan> {
        delay(500)

        val index = mockLessonPlans.indexOfFirst { it.id == lessonPlan.id }
        if (index != -1) {
            mockLessonPlans[index] = lessonPlan
            return Result.success(lessonPlan)
        }

        return Result.failure(Exception("Lesson plan not found"))
    }

    override suspend fun deleteLessonPlan(planId: String): Result<Unit> {
        delay(500)

        mockLessonPlans.removeIf { it.id == planId }
        return Result.success(Unit)
    }

    override suspend fun generateSchemeOfWork(
        teacherId: String,
        classId: String,
        subjectId: String,
        term: Int
    ): Result<SchemeOfWork> {
        delay(500)
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun saveSchemeOfWork(scheme: SchemeOfWork): Result<SchemeOfWork> {
        delay(500)
        return Result.success(scheme)
    }

    override suspend fun saveExam(exam: GeneratedExam): Result<GeneratedExam> {
        delay(500)
        return Result.success(exam)
    }

    override suspend fun getExams(teacherId: String): Result<List<GeneratedExam>> {
        delay(500)
        return Result.success(emptyList())
    }

    override suspend fun exportExamToPdf(examId: String): Result<String> {
        delay(500)
        return Result.failure(Exception("Not implemented"))
    }
}
