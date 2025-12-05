package com.omondit.learnhub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.omondit.learnhub.presentation.screens.cklasses.ClassesScreen
import com.omondit.learnhub.presentation.screens.content.ContentScreen
import com.omondit.learnhub.presentation.screens.home.HomeScreen
import com.omondit.learnhub.presentation.screens.login.LoginScreen
import com.omondit.learnhub.presentation.screens.quiz.QuizResultScreen
import com.omondit.learnhub.presentation.screens.quiz.QuizScreen
import com.omondit.learnhub.presentation.screens.splash.SplashScreen
import com.omondit.learnhub.presentation.screens.subjects.SubjectsScreen
import com.omondit.learnhub.presentation.screens.subtopics.SubtopicsScreen
import com.omondit.learnhub.presentation.screens.teacher.TeacherDashboardScreen
import com.omondit.learnhub.presentation.screens.teacher.exambuilder.ExamBuilderScreen
import com.omondit.learnhub.presentation.screens.teacher.exambuilder.ExamPreviewScreen
import com.omondit.learnhub.presentation.screens.teacher.lessonplanner.LessonPlanFormScreen
import com.omondit.learnhub.presentation.screens.teacher.lessonplanner.LessonPlannerScreen
import com.omondit.learnhub.presentation.screens.teacher.questionbank.QuestionBankScreen
import com.omondit.learnhub.presentation.screens.teacher.questionbank.QuestionFormScreen
import com.omondit.learnhub.presentation.screens.topics.TopicsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.LessonPlanForm.route) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: return@composable

            LessonPlanFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSaveSuccess = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.QuestionBank.route) {
            QuestionBankScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToForm = { questionId ->
                    navController.navigate(Screen.QuestionForm.createRoute(questionId))
                }
            )
        }
        composable(Screen.LessonPlanner.route) {
            LessonPlannerScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToForm = { planId ->
                    navController.navigate(Screen.LessonPlanForm.createRoute(planId))
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToClasses = {
                    navController.navigate(Screen.Classes.route)
                },
                onNavigateToTeacherDashboard = {
                    navController.navigate(Screen.TeacherDashboard.route)
                }
            )
        }

        composable(Screen.Classes.route) {
            ClassesScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onClassClick = { classId ->
                    navController.navigate(Screen.Subjects.createRoute(classId))
                }
            )
        }
        composable(Screen.ExamBuilder.route) {
            ExamBuilderScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToPreview = { examId ->
                    navController.navigate(Screen.ExamPreview.createRoute(examId))
                }
            )
        }

        composable(Screen.ExamPreview.route) { backStackEntry ->
            val examId = backStackEntry.arguments?.getString("examId") ?: return@composable

            ExamPreviewScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.QuestionForm.route) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getString("questionId") ?: return@composable

            QuestionFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSaveSuccess = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.Subjects.route) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: return@composable

            SubjectsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.Topics.createRoute(subjectId))
                }
            )
        }

        composable(Screen.Topics.route) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: return@composable

            TopicsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onTopicClick = { topicId ->
                    navController.navigate(Screen.Subtopics.createRoute(topicId))
                }
            )
        }

        composable(Screen.Subtopics.route) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: return@composable

            SubtopicsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSubtopicClick = { subtopicId ->
                    navController.navigate(Screen.Content.createRoute(subtopicId))
                }
            )
        }
        // More screens will be added here
        composable(Screen.Content.route) { backStackEntry ->
            val subtopicId = backStackEntry.arguments?.getString("subtopicId") ?: return@composable

            ContentScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToQuiz = { subtopicId ->
                    navController.navigate(Screen.Quiz.createRoute(subtopicId))
                }
            )
        }
        composable(Screen.Quiz.route) { backStackEntry ->
            val subtopicId = backStackEntry.arguments?.getString("subtopicId") ?: return@composable

            QuizScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToResults = { subtopicId, score, totalQuestions, passed ->
                    navController.navigate(
                        Screen.QuizResult.createRoute(subtopicId, score, totalQuestions, passed)
                    ) {
                        popUpTo(Screen.Quiz.createRoute(subtopicId)) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.QuizResult.route) { backStackEntry ->
            val subtopicId = backStackEntry.arguments?.getString("subtopicId") ?: return@composable

            QuizResultScreen(
                onNavigateBack = {
                    // Go back to subtopics list
                    navController.popBackStack(
                        route = Screen.Subtopics.route.replace("{topicId}", ""),
                        inclusive = false
                    )
                },
                onRetakeQuiz = {
                    navController.navigate(Screen.Quiz.createRoute(subtopicId)) {
                        popUpTo(Screen.QuizResult.route) { inclusive = true }
                    }
                },
                onContinueLearning = {
                    navController.navigate(Screen.Content.createRoute(subtopicId)) {
                        popUpTo(Screen.QuizResult.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToClasses = {
                    navController.navigate(Screen.Classes.route)
                },
                onNavigateToTeacherDashboard = {
                    navController.navigate(Screen.TeacherDashboard.route)
                }
            )
        }

        composable(Screen.TeacherDashboard.route) {
            TeacherDashboardScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToLessonPlanner = {
                    navController.navigate(Screen.LessonPlanner.route)
                },
                onNavigateToQuestionBank = {
                    navController.navigate(Screen.QuestionBank.route)
                },
                onNavigateToExamBuilder = {
                    navController.navigate(Screen.ExamBuilder.route)
                }
            )
        }

    }
}
