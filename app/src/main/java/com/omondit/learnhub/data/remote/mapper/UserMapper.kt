package com.omondit.learnhub.data.remote.mapper

import com.omondit.learnhub.data.remote.dto.UserDto
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.model.UserRole

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        role = when (this.role.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "TEACHER" -> UserRole.TEACHER
            "STUDENT" -> UserRole.STUDENT
            "COURSE_CREATOR" -> UserRole.COURSE_CREATOR
            else -> throw IllegalArgumentException("Unknown role: ${this.role}")
        },
        classId = this.classId,
        schoolId = this.schoolId
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        email = this.email,
        role = this.role.name,
        classId = this.classId,
        schoolId = this.schoolId
    )
}
