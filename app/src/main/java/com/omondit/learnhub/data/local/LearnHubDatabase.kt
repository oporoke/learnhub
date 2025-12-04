package com.omondit.learnhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omondit.learnhub.data.local.dao.ProgressDao
import com.omondit.learnhub.data.local.entity.ContentProgressEntity
import com.omondit.learnhub.data.local.entity.SubtopicProgressEntity
import com.omondit.learnhub.data.local.entity.TopicProgressEntity

@Database(
    entities = [
        ContentProgressEntity::class,
        SubtopicProgressEntity::class,
        TopicProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LearnHubDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}
