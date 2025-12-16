package com.omondit.learnhub.domain.model

data class LeaderboardEntry(
    val userId: String,
    val userName: String,
    val rank: Int,
    val score: Int,
    val streak: Int,
    val completedContent: Int,
    val avatarUrl: String? = null
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val earnedAt: Long? = null,
    val isUnlocked: Boolean = false
)

data class UserProfile(
    val userId: String,
    val userName: String,
    val email: String,
    val role: UserRole,
    val joinedAt: Long,
    val streak: Int,
    val totalContent: Int,
    val completedContent: Int,
    val achievements: List<Achievement>
)

enum class LeaderboardType {
    COMPLETION,
    STREAK,
    WEEKLY
}
