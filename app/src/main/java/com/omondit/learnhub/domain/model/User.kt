package com.omondit.learnhub.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val classId: String? = null, // For students
    val schoolId: String? = null
)

enum class UserRole {
    ADMIN,
    TEACHER,
    STUDENT,
    COURSE_CREATOR
}
