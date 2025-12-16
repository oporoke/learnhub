package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.local.dao.ContentDao
import com.omondit.learnhub.data.local.dao.ProgressDao
import com.omondit.learnhub.domain.model.StudentAnalytics
import com.omondit.learnhub.domain.model.TopicAnalytics
import com.omondit.learnhub.domain.model.WeeklyProgress
import com.omondit.learnhub.domain.repository.AnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao,
    private val contentDao: ContentDao
) : AnalyticsRepository {

    override suspend fun getStudentAnalytics(userId: String): Result<StudentAnalytics> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Get all progress data
                val contentProgress = progressDao.getContentProgressByUser(userId)
                val subtopicProgress = progressDao.getSubtopicProgressByUser(userId)
                val topicProgress = progressDao.getTopicProgressByUser(userId)

                // Get all content for totals
                val allTopics = contentDao.getTopics("") // All topics
                val allSubtopics = contentDao.getSubtopics("") // All subtopics
                val allContent = contentDao.getContent("") // All content

                // Calculate stats
                val completedContent = contentProgress.count { it.completed }
                val completedSubtopics = subtopicProgress.count { it.completionPercentage >= 100f }
                val completedTopics = topicProgress.count { it.completionPercentage >= 100f }

                // Topic analytics
                val topicAnalyticsList = topicProgress.map { topicProgressEntity ->
                    val topic = allTopics.find { it.id == topicProgressEntity.topicId }
                    val subtopicsInTopic = allSubtopics.filter { it.topicId == topicProgressEntity.topicId }
                    val contentInTopic = subtopicsInTopic.flatMap { subtopic ->
                        allContent.filter { it.subtopicId == subtopic.id }
                    }
                    val completedContentInTopic = contentInTopic.count { content ->
                        contentProgress.any { it.contentId == content.id && it.completed }
                    }

                    TopicAnalytics(
                        topicId = topicProgressEntity.topicId,
                        topicName = topic?.name ?: "Unknown Topic",
                        subjectName = "Mathematics", // Simplified - would need subject lookup
                        completionPercentage = topicProgressEntity.completionPercentage,
                        contentCompleted = completedContentInTopic,
                        contentTotal = contentInTopic.size,
                        lastAccessedAt = null // Would need to track this
                    )
                }

                val analytics = StudentAnalytics(
                    userId = userId,
                    totalTopics = allTopics.size,
                    completedTopics = completedTopics,
                    totalSubtopics = allSubtopics.size,
                    completedSubtopics = completedSubtopics,
                    totalContent = allContent.size,
                    completedContent = completedContent,
                    quizzesTaken = 0, // Would need quiz tracking
                    averageQuizScore = 0f, // Would need quiz results
                    studyStreak = Random.nextInt(1, 15), // Simulated
                    totalStudyTimeMinutes = completedContent * Random.nextInt(5, 20), // Simulated
                    topicProgress = topicAnalyticsList
                )

                Result.success(analytics)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getWeeklyProgress(userId: String): Result<List<WeeklyProgress>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Simulated weekly progress
                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val weeklyProgress = daysOfWeek.mapIndexed { index, day ->
                    WeeklyProgress(
                        dayOfWeek = day,
                        contentCompleted = Random.nextInt(0, 5),
                        minutesStudied = Random.nextInt(0, 120)
                    )
                }

                Result.success(weeklyProgress)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
