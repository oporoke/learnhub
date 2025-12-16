package com.omondit.learnhub.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.model.UserRole
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = getCurrentUserUseCase()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToClasses: () -> Unit,
    onNavigateToTeacherDashboard: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LearnHub Kenya") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LearnHub Kenya",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            currentUser?.let { user ->
                Text(
                    text = "Welcome, ${user.name}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = when (user.role) {
                            UserRole.STUDENT -> "👨‍🎓 Student"
                            UserRole.TEACHER -> "👨‍🏫 Teacher"
                            UserRole.ADMIN -> "👨‍💼 Admin"
                            UserRole.COURSE_CREATOR -> "✍️ Course Creator"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Role-based navigation
            when (currentUser?.role) {
                UserRole.STUDENT -> {
                    StudentHomeContent(onNavigateToClasses, onNavigateToSearch, onNavigateToLeaderboard)
                }

                UserRole.TEACHER -> {
                    TeacherHomeContent(
                        onNavigateToClasses = onNavigateToClasses,
                        onNavigateToTeacherDashboard = onNavigateToTeacherDashboard
                    )
                }

                UserRole.ADMIN -> {
                    AdminHomeContent()
                }

                UserRole.COURSE_CREATOR -> {
                    CreatorHomeContent()
                }

                null -> {
                    CircularProgressIndicator()
                }
            }

            OutlinedButton(
                onClick = onNavigateToAnalytics,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "📊 My Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun StudentHomeContent(
    onNavigateToClasses: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToLeaderboard: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ready to learn something new?",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToClasses,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .semantics {
                    contentDescription = "Start Learning - Browse classes and subjects"
                }
        ) {
            Text(
                text = "Start Learning",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavigateToSearch,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "🔍 Search Content",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onNavigateToLeaderboard,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "🏆 Leaderboard",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TeacherHomeContent(
    onNavigateToClasses: () -> Unit,
    onNavigateToTeacherDashboard: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What would you like to do?",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToTeacherDashboard,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "📚 Teacher Tools",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavigateToClasses,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "📖 Browse Content",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AdminHomeContent() {
    Text(
        text = "Admin features coming soon...",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun CreatorHomeContent() {
    Text(
        text = "Course creator features coming soon...",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
