package com.omondit.learnhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.omondit.learnhub.data.preferences.ThemeMode
import com.omondit.learnhub.data.preferences.ThemePreferences
import com.omondit.learnhub.presentation.navigation.NavGraph
import com.omondit.learnhub.ui.theme.LearnhubTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnHubApp()
        }
    }
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themePreferences.setThemeMode(mode)
        }
    }
}

@Composable
fun LearnHubApp(
    viewModel: ThemeViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val systemInDarkTheme = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }

    LearnhubTheme(
        darkTheme = darkTheme,
        dynamicColor = true
    ) {
        val navController = rememberNavController()
        NavGraph(navController = navController)
    }
}
