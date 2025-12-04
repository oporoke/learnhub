package com.omondit.learnhub.data.remote.api

import com.omondit.learnhub.data.remote.ApiResponse
import com.omondit.learnhub.data.remote.dto.ClassDto
import com.omondit.learnhub.data.remote.dto.ContentDto
import com.omondit.learnhub.data.remote.dto.SubjectDto
import com.omondit.learnhub.data.remote.dto.SubtopicDto
import com.omondit.learnhub.data.remote.dto.TopicDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContentApiService {

    @GET("classes")
    suspend fun getClasses(): ApiResponse<List<ClassDto>>

    @GET("classes/{classId}/subjects")
    suspend fun getSubjects(@Path("classId") classId: String): ApiResponse<List<SubjectDto>>

    @GET("subjects/{subjectId}/topics")
    suspend fun getTopics(@Path("subjectId") subjectId: String): ApiResponse<List<TopicDto>>

    @GET("topics/{topicId}/subtopics")
    suspend fun getSubtopics(@Path("topicId") topicId: String): ApiResponse<List<SubtopicDto>>

    @GET("subtopics/{subtopicId}/content")
    suspend fun getContent(@Path("subtopicId") subtopicId: String): ApiResponse<List<ContentDto>>

    @GET("content/{contentId}")
    suspend fun getContentById(@Path("contentId") contentId: String): ApiResponse<ContentDto>

    @POST("content")
    suspend fun createContent(@Body content: ContentDto): ApiResponse<ContentDto>

    @PUT("content/{contentId}")
    suspend fun updateContent(
        @Path("contentId") contentId: String,
        @Body content: ContentDto
    ): ApiResponse<ContentDto>
}