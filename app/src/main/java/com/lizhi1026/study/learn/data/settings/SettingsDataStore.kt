package com.lizhi1026.study.learn.data.settings

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.lizhi1026.study.learn.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {
    private val store = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("settings") }
    )

    private object Keys {
        val workDurationMinutes = intPreferencesKey("workDurationMinutes")
        val shortBreakMinutes = intPreferencesKey("shortBreakMinutes")
        val longBreakMinutes = intPreferencesKey("longBreakMinutes")
        val sessionsBeforeLongBreak = intPreferencesKey("sessionsBeforeLongBreak")
        val notificationSound = stringPreferencesKey("notificationSound")
        val enableHaptics = booleanPreferencesKey("enableHaptics")
        val theme = stringPreferencesKey("theme")
    }

    fun observe(): Flow<Settings> = store.data.map { p ->
        Settings(
            workDurationMinutes = p[Keys.workDurationMinutes] ?: 25,
            shortBreakMinutes = p[Keys.shortBreakMinutes] ?: 5,
            longBreakMinutes = p[Keys.longBreakMinutes] ?: 15,
            sessionsBeforeLongBreak = p[Keys.sessionsBeforeLongBreak] ?: 4,
            notificationSound = p[Keys.notificationSound] ?: "default",
            enableHaptics = p[Keys.enableHaptics] ?: true,
            theme = p[Keys.theme] ?: "auto",
        )
    }

    suspend fun update(block: (Settings) -> Settings) {
        store.edit { p ->
            val cur = observeOnce()
            val next = block(cur)
            p[Keys.workDurationMinutes] = next.workDurationMinutes
            p[Keys.shortBreakMinutes] = next.shortBreakMinutes
            p[Keys.longBreakMinutes] = next.longBreakMinutes
            p[Keys.sessionsBeforeLongBreak] = next.sessionsBeforeLongBreak
            p[Keys.notificationSound] = next.notificationSound
            p[Keys.enableHaptics] = next.enableHaptics
            p[Keys.theme] = next.theme
        }
    }

    private suspend fun observeOnce(): Settings = observe().first()
}