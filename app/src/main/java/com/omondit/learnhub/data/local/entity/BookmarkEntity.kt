package com.omondit.learnhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val id: String, // Format: userId_itemType_itemId
    val userId: String,
    val itemType: String, // "TOPIC" or "SUBTOPIC"
    val itemId: String,
    val itemTitle: String,
    val itemDescription: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)