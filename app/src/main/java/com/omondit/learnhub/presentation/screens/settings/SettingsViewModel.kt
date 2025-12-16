package com.omondit.learnhub.presentation.screens.settings

import com.omondit.learnhub.data.preferences.ThemeMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.data.notification.NotificationHelper
import com.omondit.learnhub.data.preferences.NotificationPreferences
import com.omondit.learnhub.data.preferences.ThemePreferences
import com.omondit.learnhub.domain.model.User
import com.omondit.learnhub.domain.usecase.auth.GetCurrentUserUseCase
import com.omondit.learnhub.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferences: ThemePreferences,
    private val notificationPreferences: NotificationPreferences,
    private val notificationHelper: NotificationHelper,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val notificationsEnabled: StateFlow<Boolean> = notificationPreferences.remindersEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val reminderTime: StateFlow<Pair<Int, Int>> = notificationPreferences.reminderTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Pair(18, 0)
        )

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            notificationPreferences.setRemindersEnabled(enabled)

            if (enabled) {
                val (hour, minute) = reminderTime.value
                notificationHelper.scheduleStudyReminder(hour, minute)
            }
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            notificationPreferences.setReminderTime(hour, minute)

            if (notificationsEnabled.value) {
                notificationHelper.scheduleStudyReminder(hour, minute)
            }
        }
    }

    fun testNotification() {
        notificationHelper.sendStudyReminder(
            title = "📚 Test Notification",
            message = "Notifications are working! You'll receive study reminders."
        )
    }

    fun hasNotificationPermission(): Boolean {
        return notificationHelper.hasNotificationPermission()
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = getCurrentUserUseCase()
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themePreferences.setThemeMode(mode)
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onLogoutComplete()
        }
    }
}
