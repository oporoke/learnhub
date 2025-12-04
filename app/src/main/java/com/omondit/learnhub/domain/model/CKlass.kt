package com.omondit.learnhub.domain.model

data class CKlass(
    val id: String,
    val name: String, // e.g., "Form 3", "Grade 10"
    val curriculum: Curriculum
)

enum class Curriculum {
    CURRICULUM_844,
    CBC
}
