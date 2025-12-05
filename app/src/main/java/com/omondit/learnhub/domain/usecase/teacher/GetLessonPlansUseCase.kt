package com.omondit.learnhub.domain.usecase.teacher

import com.omondit.learnhub.domain.model.LessonPlan
import com.omondit.learnhub.domain.repository.TeacherRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetLessonPlansUseCase @Inject constructor(
    private val teacherRepository: TeacherRepository
) : BaseUseCase<String, List<LessonPlan>>() {

    override suspend fun invoke(params: String): Result<List<LessonPlan>> {
        return teacherRepository.getLessonPlans(params)
    }
}
