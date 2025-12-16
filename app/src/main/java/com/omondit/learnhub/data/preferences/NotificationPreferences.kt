package com.omondit.learnhub.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.notificationDataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_preferences")

@Singleton
class NotificationPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
    private val REMINDER_HOUR = intPreferencesKey("reminder_hour")
    private val REMINDER_MINUTE = intPreferencesKey("reminder_minute")

    val remindersEnabled: Flow<Boolean> = context.notificationDataStore.data
        .map { preferences -> preferences[REMINDERS_ENABLED] ?: false }

    val reminderTime: Flow<Pair<Int, Int>> = context.notificationDataStore.data
        .map { preferences ->
            val hour = preferences[REMINDER_HOUR] ?: 18 // Default 6 PM
            val minute = preferences[REMINDER_MINUTE] ?: 0
            Pair(hour, minute)
        }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.notificationDataStore.edit { preferences ->
            preferences[REMINDERS_ENABLED] = enabled
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.notificationDataStore.edit { preferences ->
            preferences[REMINDER_HOUR] = hour
            preferences[REMINDER_MINUTE] = minute
        }
    }
}
