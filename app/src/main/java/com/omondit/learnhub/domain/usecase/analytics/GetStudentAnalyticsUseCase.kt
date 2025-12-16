package com.omondit.learnhub.domain.usecase.analytics

import com.omondit.learnhub.domain.model.StudentAnalytics
import com.omondit.learnhub.domain.repository.AnalyticsRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetStudentAnalyticsUseCase @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : BaseUseCase<String, StudentAnalytics>() {

    override suspend fun invoke(params: String): Result<StudentAnalytics> {
        return analyticsRepository.getStudentAnalytics(params)
    }
}
