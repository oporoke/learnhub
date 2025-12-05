package com.omondit.learnhub.presentation.navigation


sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Classes : Screen("classes")
    data object Subjects : Screen("subjects/{classId}") {
        fun createRoute(classId: String) = "subjects/$classId"
    }
    data object Topics : Screen("topics/{subjectId}") {
        fun createRoute(subjectId: String) = "topics/$subjectId"
    }
    data object Subtopics : Screen("subtopics/{topicId}") {
        fun createRoute(topicId: String) = "subtopics/$topicId"
    }
    data object Content : Screen("content/{subtopicId}") {
        fun createRoute(subtopicId: String) = "content/$subtopicId"
    }
    data object Quiz : Screen("quiz/{subtopicId}") {
        fun createRoute(subtopicId: String) = "quiz/$subtopicId"
    }
    data object QuizResult : Screen("quiz_result/{subtopicId}/{score}/{totalQuestions}/{passed}") {
        fun createRoute(subtopicId: String, score: Int, totalQuestions: Int, passed: Boolean) =
            "quiz_result/$subtopicId/$score/$totalQuestions/$passed"
    }

    // Teacher routes
    data object TeacherDashboard : Screen("teacher_dashboard")
    data object LessonPlanner : Screen("lesson_planner")
    data object LessonPlanForm : Screen("lesson_plan_form/{planId}") {
        fun createRoute(planId: String = "new") = "lesson_plan_form/$planId"
    }
    data object QuestionBank : Screen("question_bank")
    data object QuestionForm : Screen("question_form/{questionId}") {
        fun createRoute(questionId: String = "new") = "question_form/$questionId"
    }
    data object ExamBuilder : Screen("exam_builder")
    data object ExamPreview : Screen("exam_preview/{examId}") {
        fun createRoute(examId: String) = "exam_preview/$examId"
    }
}
