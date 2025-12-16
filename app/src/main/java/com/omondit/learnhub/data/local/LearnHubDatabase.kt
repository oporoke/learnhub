package com.omondit.learnhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omondit.learnhub.data.local.dao.BookmarkDao
import com.omondit.learnhub.data.local.dao.ContentDao
import com.omondit.learnhub.data.local.dao.ProgressDao
import com.omondit.learnhub.data.local.entity.BookmarkEntity
import com.omondit.learnhub.data.local.entity.ClassEntity
import com.omondit.learnhub.data.local.entity.ContentEntity
import com.omondit.learnhub.data.local.entity.ContentProgressEntity
import com.omondit.learnhub.data.local.entity.SubjectEntity
import com.omondit.learnhub.data.local.entity.SubtopicEntity
import com.omondit.learnhub.data.local.entity.SubtopicProgressEntity
import com.omondit.learnhub.data.local.entity.TopicEntity
import com.omondit.learnhub.data.local.entity.TopicProgressEntity

@Database(
    entities = [
        // Progress tracking
        ContentProgressEntity::class,
        SubtopicProgressEntity::class,
        TopicProgressEntity::class,
        // Content caching
        ClassEntity::class,
        SubjectEntity::class,
        TopicEntity::class,
        SubtopicEntity::class,
        ContentEntity::class,
        // Bookmarks
        BookmarkEntity::class
    ],
    version = 2, // Incremented version
    exportSchema = false
)
abstract class LearnHubDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
    abstract fun contentDao(): ContentDao
    abstract fun bookmarkDao(): BookmarkDao
}
