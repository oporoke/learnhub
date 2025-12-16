package com.omondit.learnhub.di

import com.omondit.learnhub.data.repository.AnalyticsRepositoryImpl
import com.omondit.learnhub.data.repository.AuthRepositoryImpl
import com.omondit.learnhub.data.repository.BookmarkRepositoryImpl
import com.omondit.learnhub.data.repository.CachedContentRepositoryImpl
import com.omondit.learnhub.data.repository.ContentRepositoryImpl
import com.omondit.learnhub.data.repository.MockAuthRepositoryImpl
import com.omondit.learnhub.data.repository.MockContentRepositoryImpl
import com.omondit.learnhub.data.repository.ProgressRepositoryImpl
import com.omondit.learnhub.data.repository.QuestionRepositoryImpl
import com.omondit.learnhub.data.repository.SearchRepositoryImpl
import com.omondit.learnhub.data.repository.SocialRepositoryImpl
import com.omondit.learnhub.data.repository.TeacherRepositoryImpl
import com.omondit.learnhub.domain.repository.AnalyticsRepository
import com.omondit.learnhub.domain.repository.AuthRepository
import com.omondit.learnhub.domain.repository.BookmarkRepository
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.repository.ProgressRepository
import com.omondit.learnhub.domain.repository.QuestionRepository
import com.omondit.learnhub.domain.repository.SearchRepository
import com.omondit.learnhub.domain.repository.SocialRepository
import com.omondit.learnhub.domain.repository.TeacherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        mockAuthRepositoryImpl: MockAuthRepositoryImpl // Changed to Mock
    ): AuthRepository

//    @Binds
//    @Singleton
//    abstract fun bindContentRepository(
//        mockContentRepositoryImpl: MockContentRepositoryImpl // Changed to Mock
//    ): ContentRepository

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        cachedContentRepositoryImpl: CachedContentRepositoryImpl // Changed from Mock
    ): ContentRepository


    @Binds
    @Singleton
    abstract fun bindQuestionRepository(
        questionRepositoryImpl: QuestionRepositoryImpl
    ): QuestionRepository



    @Binds
    @Singleton
    abstract fun bindTeacherRepository(
        teacherRepositoryImpl: TeacherRepositoryImpl
    ): TeacherRepository

    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(
        analyticsRepositoryImpl: AnalyticsRepositoryImpl
    ): AnalyticsRepository

    @Binds
    @Singleton
    abstract fun bindSocialRepository(
        socialRepositoryImpl: SocialRepositoryImpl
    ): SocialRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        progressRepositoryImpl: ProgressRepositoryImpl
    ): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}
