package com.omondit.learnhub.domain.usecase.teacher

import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.repository.TeacherRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class CreateLessonPlanUseCase @Inject constructor(
    private val teacherRepository: TeacherRepository
) : BaseUseCase<LessonPlan, LessonPlan>() {

    override suspend fun invoke(params: LessonPlan): Result<LessonPlan> {
        // Validation
        if (params.objectives.isBlank()) {
            return Result.failure(Exception("Objectives cannot be empty"))
        }

        return teacherRepository.createLessonPlan(params)
    }
}
