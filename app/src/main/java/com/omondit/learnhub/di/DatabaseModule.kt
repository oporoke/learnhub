package com.omondit.learnhub.di

import android.content.Context
import androidx.room.Room
import com.omondit.learnhub.data.local.LearnHubDatabase
import com.omondit.learnhub.data.local.dao.BookmarkDao
import com.omondit.learnhub.data.local.dao.ContentDao
import com.omondit.learnhub.data.local.dao.ProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideContentDao(database: LearnHubDatabase): ContentDao {
        return database.contentDao()
    }

    @Provides
    @Singleton
    fun provideLearnHubDatabase(
        @ApplicationContext context: Context
    ): LearnHubDatabase {
        return Room.databaseBuilder(
            context,
            LearnHubDatabase::class.java,
            "learnhub_database"
        )
            .fallbackToDestructiveMigration() // For development only
            .build()
    }


    @Provides
    @Singleton
    fun provideProgressDao(database: LearnHubDatabase): ProgressDao {
        return database.progressDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: LearnHubDatabase): BookmarkDao {
        return database.bookmarkDao()
    }
}
