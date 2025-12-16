package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.StudentAnalytics
import com.omondit.learnhub.domain.model.WeeklyProgress

interface AnalyticsRepository {
    suspend fun getStudentAnalytics(userId: String): Result<StudentAnalytics>
    suspend fun getWeeklyProgress(userId: String): Result<List<WeeklyProgress>>
}
