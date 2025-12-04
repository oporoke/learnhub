package com.omondit.learnhub.domain.model

data class Topic(
    val id: String,
    val subjectId: String,
    val name: String, // e.g., "Algebra", "Organic Chemistry"
    val description: String,
    val order: Int = 0 // For sequencing
)
