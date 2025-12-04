package com.omondit.learnhub.domain.model

data class Subject(
    val id: String,
    val classId: String,
    val name: String, // e.g., "Mathematics", "Chemistry"
    val iconUrl: String? = null
)
