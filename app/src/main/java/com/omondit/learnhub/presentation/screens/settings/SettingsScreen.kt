package com.omondit.learnhub.presentation.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.data.preferences.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val reminderTime by viewModel.reminderTime.collectAsStateWithLifecycle()

    // Permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleNotifications(true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                .verticalScroll(rememberScrollState())
        ) {
            // Profile section
            currentUser?.let { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(12.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = user.email,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = user.role.name.replace("_", " ").lowercase().capitalize(),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Appearance section
            SettingsSection(title = "Appearance")

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Study Reminders",
                subtitle = if (notificationsEnabled) {
                    "Enabled at ${String.format("%02d:%02d", reminderTime.first, reminderTime.second)}"
                } else {
                    "Tap to enable daily reminders"
                },
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !viewModel.hasNotificationPermission()) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.toggleNotifications(!notificationsEnabled)
                    }
                }
            )

            // Add test notification button (temporary for testing)
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Test Notification",
                subtitle = "Send a test notification",
                onClick = { viewModel.testNotification() }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // General section
            SettingsSection(title = "General")

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification preferences",
                onClick = { /* TODO */ }
            )

            SettingsItem(
                icon = Icons.Default.Storage,
                title = "Storage",
                subtitle = "Clear cache and manage data",
                onClick = { /* TODO */ }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // About section
            SettingsSection(title = "About")

            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version",
                subtitle = "1.0.0 Beta",
                onClick = { }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Account section
            SettingsSection(title = "Account")

            SettingsItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Logout",
                subtitle = "Sign out of your account",
                onClick = { showLogoutDialog = true },
                iconTint = MaterialTheme.colorScheme.error
            )
        }
    }

    // Theme selection dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
            text = {
                Column {
                    ThemeMode.entries.forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = themeMode == mode,
                                onClick = {
                                    viewModel.setThemeMode(mode)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (mode) {
                                    ThemeMode.LIGHT -> "Light"
                                    ThemeMode.DARK -> "Dark"
                                    ThemeMode.SYSTEM -> "System Default"
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout {
                            onNavigateToLogin()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
