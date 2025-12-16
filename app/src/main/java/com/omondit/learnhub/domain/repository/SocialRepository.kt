package com.omondit.learnhub.domain.repository

import com.omondit.learnhub.domain.model.Achievement
import com.omondit.learnhub.domain.model.LeaderboardEntry
import com.omondit.learnhub.domain.model.LeaderboardType
import com.omondit.learnhub.domain.model.UserProfile

interface SocialRepository {
    suspend fun getLeaderboard(type: LeaderboardType, limit: Int = 10): Result<List<LeaderboardEntry>>
    suspend fun getUserProfile(userId: String): Result<UserProfile>
    suspend fun getAchievements(userId: String): Result<List<Achievement>>
    suspend fun getUserRank(userId: String, type: LeaderboardType): Result<Int>
}
