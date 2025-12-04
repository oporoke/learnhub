package com.omondit.learnhub.data.remote.mapper

import com.omondit.learnhub.data.remote.dto.QuestionDto
import com.omondit.learnhub.data.remote.dto.SubQuestionDto
import com.omondit.learnhub.domain.model.Difficulty
import com.omondit.learnhub.domain.model.Question
import com.omondit.learnhub.domain.model.QuestionType
import com.omondit.learnhub.domain.model.SubQuestion

fun QuestionDto.toDomain(): Question {
    return Question(
        id = this.id,
        contentId = this.contentId,
        teacherId = this.teacherId,
        classId = this.classId,
        questionType = when (this.questionType.uppercase()) {
            "STANDALONE" -> QuestionType.STANDALONE
            "SECTIONAL" -> QuestionType.SECTIONAL
            "MCQ" -> QuestionType.MCQ
            else -> throw IllegalArgumentException("Unknown question type: ${this.questionType}")
        },
        questionText = this.questionText,
        subQuestions = this.subQuestions?.map { it.toDomain() },
        options = this.options,
        correctAnswer = this.correctAnswer,
        difficulty = when (this.difficulty.uppercase()) {
            "EASY" -> Difficulty.EASY
            "MEDIUM" -> Difficulty.MEDIUM
            "HARD" -> Difficulty.HARD
            else -> throw IllegalArgumentException("Unknown difficulty: ${this.difficulty}")
        },
        topicId = this.topicId,
        subtopicId = this.subtopicId
    )
}

fun SubQuestionDto.toDomain(): SubQuestion {
    return SubQuestion(
        label = this.label,
        text = this.text,
        answer = this.answer
    )
}

fun Question.toDto(): QuestionDto {
    return QuestionDto(
        id = this.id,
        contentId = this.contentId,
        teacherId = this.teacherId,
        classId = this.classId,
        questionType = this.questionType.name,
        questionText = this.questionText,
        subQuestions = this.subQuestions?.map { it.toDto() },
        options = this.options,
        correctAnswer = this.correctAnswer,
        difficulty = this.difficulty.name,
        topicId = this.topicId,
        subtopicId = this.subtopicId
    )
}

fun SubQuestion.toDto(): SubQuestionDto {
    return SubQuestionDto(
        label = this.label,
        text = this.text,
        answer = this.answer
    )
}
