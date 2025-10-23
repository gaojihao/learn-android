/**
 * 文件：SettingsDataStore.kt
 * 简介：设置存储
 * 关键职责：偏好项的读取与更新（DataStore）
 * 相关组件：ViewModel、UI、DataStore
 */
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

    /**
     * 观察设置流
     * 将 DataStore 中的偏好键映射为领域模型 Settings
     */
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

    /**
     * 更新设置
     * @param block 接收当前设置并返回新的设置值
     */
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

    /**
     * 仅观察一次当前设置（用于写入时读取最新值）
     */
    private suspend fun observeOnce(): Settings = observe().first()
}