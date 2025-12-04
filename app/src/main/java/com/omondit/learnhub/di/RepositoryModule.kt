package com.omondit.learnhub.di

import com.omondit.learnhub.data.repository.AuthRepositoryImpl
import com.omondit.learnhub.data.repository.ContentRepositoryImpl
import com.omondit.learnhub.data.repository.QuestionRepositoryImpl
import com.omondit.learnhub.data.repository.TeacherRepositoryImpl
import com.omondit.learnhub.domain.repository.AuthRepository
import com.omondit.learnhub.domain.repository.ContentRepository
import com.omondit.learnhub.domain.repository.QuestionRepository
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
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        contentRepositoryImpl: ContentRepositoryImpl
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
}
