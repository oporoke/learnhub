package com.omondit.learnhub.domain.model

data class Subtopic(
    val id: String,
    val topicId: String,
    val name: String, // e.g., "Linear Equations"
    val description: String,
    val order: Int = 0
)
