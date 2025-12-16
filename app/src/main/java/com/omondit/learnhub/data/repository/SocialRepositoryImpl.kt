package com.omondit.learnhub.data.repository

import com.omondit.learnhub.data.local.dao.ProgressDao
import com.omondit.learnhub.domain.model.Achievement
import com.omondit.learnhub.domain.model.LeaderboardEntry
import com.omondit.learnhub.domain.model.LeaderboardType
import com.omondit.learnhub.domain.model.UserProfile
import com.omondit.learnhub.domain.model.UserRole
import com.omondit.learnhub.domain.repository.SocialRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class SocialRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : SocialRepository {

    // Mock data for leaderboard
    private val mockUsers = listOf(
        "Alice Mwangi", "Brian Odhiambo", "Catherine Wanjiru", "David Kipchoge",
        "Emma Achieng", "Frank Kamau", "Grace Njeri", "Hassan Mohamed",
        "Irene Mumbi", "James Otieno"
    )

    override suspend fun getLeaderboard(
        type: LeaderboardType,
        limit: Int
    ): Result<List<LeaderboardEntry>> {
        delay(500)

        return try {
            val entries = mockUsers.take(limit).mapIndexed { index, name ->
                LeaderboardEntry(
                    userId = "user_${index + 1}",
                    userName = name,
                    rank = index + 1,
                    score = when (type) {
                        LeaderboardType.COMPLETION -> Random.nextInt(50, 100)
                        LeaderboardType.STREAK -> Random.nextInt(5, 30)
                        LeaderboardType.WEEKLY -> Random.nextInt(10, 50)
                    },
                    streak = Random.nextInt(1, 30),
                    completedContent = Random.nextInt(10, 100)
                )
            }.sortedByDescending { it.score }

            Result.success(entries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): Result<UserProfile> {
        delay(500)

        return try {
            // Get actual progress from database
            val contentProgress = progressDao.getContentProgressByUser(userId)
            val completedContent = contentProgress.count { it.completed }

            val profile = UserProfile(
                userId = userId,
                userName = "Current User",
                email = "user@learnhub.ke",
                role = UserRole.STUDENT,
                joinedAt = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000), // 30 days ago
                streak = Random.nextInt(1, 20),
                totalContent = contentProgress.size,
                completedContent = completedContent,
                achievements = getDefaultAchievements(completedContent)
            )

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAchievements(userId: String): Result<List<Achievement>> {
        delay(500)

        return try {
            val contentProgress = progressDao.getContentProgressByUser(userId)
            val completedContent = contentProgress.count { it.completed }

            Result.success(getDefaultAchievements(completedContent))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserRank(userId: String, type: LeaderboardType): Result<Int> {
        delay(500)

        return try {
            // Simulated rank
            val rank = Random.nextInt(1, 50)
            Result.success(rank)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultAchievements(completedContent: Int): List<Achievement> {
        return listOf(
            Achievement(
                id = "first_lesson",
                title = "First Steps",
                description = "Complete your first lesson",
                icon = "🎯",
                isUnlocked = completedContent >= 1,
                earnedAt = if (completedContent >= 1) System.currentTimeMillis() else null
            ),
            Achievement(
                id = "ten_lessons",
                title = "Getting Started",
                description = "Complete 10 lessons",
                icon = "🌟",
                isUnlocked = completedContent >= 10,
                earnedAt = if (completedContent >= 10) System.currentTimeMillis() else null
            ),
            Achievement(
                id = "streak_7",
                title = "Week Warrior",
                description = "Maintain a 7-day streak",
                icon = "🔥",
                isUnlocked = false
            ),
            Achievement(
                id = "perfect_quiz",
                title = "Perfect Score",
                description = "Score 100% on a quiz",
                icon = "💯",
                isUnlocked = false
            ),
            Achievement(
                id = "early_bird",
                title = "Early Bird",
                description = "Study before 8 AM",
                icon = "🌅",
                isUnlocked = false
            )
        )
    }
}
