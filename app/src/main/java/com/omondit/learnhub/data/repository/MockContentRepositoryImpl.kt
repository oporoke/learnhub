package com.omondit.learnhub.data.repository

import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.model.Content
import com.omondit.learnhub.domain.model.ContentBody
import com.omondit.learnhub.domain.model.ContentStatus
import com.omondit.learnhub.domain.model.ContentType
import com.omondit.learnhub.domain.model.Curriculum
import com.omondit.learnhub.domain.model.Subject
import com.omondit.learnhub.domain.model.Subtopic
import com.omondit.learnhub.domain.model.Topic
import com.omondit.learnhub.domain.repository.ContentRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockContentRepositoryImpl @Inject constructor() : ContentRepository {

    // Mock Classes
    private val mockClasses = listOf(
        CKlass(id = "form3", name = "Form 3", curriculum = Curriculum.CURRICULUM_844),
        CKlass(id = "form4", name = "Form 4", curriculum = Curriculum.CURRICULUM_844)
    )

    // Mock Subjects
    private val mockSubjects = listOf(
        Subject(id = "math", classId = "form3", name = "Mathematics"),
        Subject(id = "chem", classId = "form3", name = "Chemistry"),
        Subject(id = "bio", classId = "form3", name = "Biology"),
        Subject(id = "physics", classId = "form3", name = "Physics"),
        Subject(id = "math_f4", classId = "form4", name = "Mathematics"),
        Subject(id = "chem_f4", classId = "form4", name = "Chemistry"),
        Subject(id = "bio_f4", classId = "form4", name = "Biology"),
        Subject(id = "physics_f4", classId = "form4", name = "Physics")
    )

    // Mock Topics
    private val mockTopics = listOf(
        Topic(id = "algebra", subjectId = "math", name = "Algebra", description = "Study of mathematical symbols and rules", order = 1),
        Topic(id = "geometry", subjectId = "math", name = "Geometry", description = "Study of shapes and spaces", order = 2),
        Topic(id = "organic", subjectId = "chem", name = "Organic Chemistry", description = "Chemistry of carbon compounds", order = 1),
        Topic(
            id = "photosynthesis",
            subjectId = "bio",
            name = "Photosynthesis",
            description = "How plants make food",
            order = 1
        )
    )

    // Mock Subtopics
    private val mockSubtopics = listOf(
        Subtopic(
            id = "linear_eq",
            topicId = "algebra",
            name = "Linear Equations",
            description = "Equations of first degree",
            order = 1
        ),
        Subtopic(id = "quadratic", topicId = "algebra", name = "Quadratic Equations", description = "Equations of second degree", order = 2)
    )

    // Mock Content
    // Mock Content
    private val mockContent = listOf(
        Content(
            id = "content1",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT,
            body = ContentBody(
                text = "A linear equation is an equation where the highest power of the variable is 1.\n\nExample: 2x + 3 = 7\n\nLinear equations form straight lines when graphed on a coordinate plane.",
                imageUrl = null,
                videoUrl = null
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        ),
        Content(
            id = "content2",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT_IMAGE,
            body = ContentBody(
                text = "To solve a linear equation, isolate the variable on one side.\n\nSteps:\n1. Simplify both sides\n2. Move variables to one side\n3. Move constants to other side\n4. Divide to isolate variable",
                imageUrl = "https://example.com/linear-equation-graph.png",
                videoUrl = null
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        ),
        Content(
            id = "content3",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT,
            body = ContentBody(
                text = "Practice Example:\n\nSolve: 3x + 5 = 14\n\nStep 1: Subtract 5 from both sides\n3x = 9\n\nStep 2: Divide both sides by 3\nx = 3\n\nTherefore, x = 3 is the solution!",
                imageUrl = null,
                videoUrl = null
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        ),
        Content(
            id = "content4",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT_IMAGE,
            body = ContentBody(
                text = "To solve a linear equation, isolate the variable on one side.\n\nSteps:\n1. Simplify both sides\n2. Move variables to one side\n3. Move constants to other side\n4. Divide to isolate variable",
                imageUrl = "https://picsum.photos/400/300", // Random placeholder image
                videoUrl = null
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        ),
        Content(
            id = "content5",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT_VIDEO,
            body = ContentBody(
                text = "Watch this video explanation of solving linear equations step by step.",
                imageUrl = null,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4" // Sample video
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        ),
        Content(
            id = "content_pdf",
            subtopicId = "linear_eq",
            creatorId = "creator1",
            contentType = ContentType.TEXT_PDF,
            body = ContentBody(
                text = "📄 Linear Equations Study Guide\n\nThis comprehensive PDF covers all aspects of linear equations including:\n• Basic concepts\n• Solving methods\n• Word problems\n• Practice exercises",
                imageUrl = null,
                videoUrl = null,
                pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf" // Sample PDF
            ),
            status = ContentStatus.PUBLISHED,
            createdAt = System.currentTimeMillis()
        )
    )

    override suspend fun getClasses(): Result<List<CKlass>> {
        delay(500)
        return Result.success(mockClasses)
    }

    override suspend fun getSubjects(classId: String): Result<List<Subject>> {
        delay(500)
        val subjects = mockSubjects.filter { it.classId == classId }
        return Result.success(subjects)
    }

    override suspend fun getTopics(subjectId: String): Result<List<Topic>> {
        delay(500)
        val topics = mockTopics.filter { it.subjectId == subjectId }
        return Result.success(topics)
    }

    override suspend fun getSubtopics(topicId: String): Result<List<Subtopic>> {
        delay(500)
        val subtopics = mockSubtopics.filter { it.topicId == topicId }
        return Result.success(subtopics)
    }

    override suspend fun getContent(subtopicId: String): Result<List<Content>> {
        delay(500)
        val content = mockContent.filter { it.subtopicId == subtopicId }
        return Result.success(content)
    }

    override suspend fun getContentById(contentId: String): Result<Content> {
        delay(500)
        val content = mockContent.find { it.id == contentId }
        return if (content != null) {
            Result.success(content)
        } else {
            Result.failure(Exception("Content not found"))
        }
    }

    override suspend fun createContent(content: Content): Result<Content> {
        delay(500)
        return Result.success(content)
    }

    override suspend fun updateContent(content: Content): Result<Content> {
        delay(500)
        return Result.success(content)
    }

    override suspend fun submitForApproval(contentId: String): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}
