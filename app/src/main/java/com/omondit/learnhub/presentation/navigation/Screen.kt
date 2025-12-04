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
}
