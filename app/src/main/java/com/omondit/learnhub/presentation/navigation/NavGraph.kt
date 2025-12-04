package com.omondit.learnhub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.omondit.learnhub.presentation.screens.cklasses.ClassesScreen
import com.omondit.learnhub.presentation.screens.home.HomeScreen
import com.omondit.learnhub.presentation.screens.login.LoginScreen
import com.omondit.learnhub.presentation.screens.splash.SplashScreen
import com.omondit.learnhub.presentation.screens.subjects.SubjectsScreen
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

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToClasses = {
                    navController.navigate(Screen.Classes.route)
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

        // More screens will be added here
    }
}
