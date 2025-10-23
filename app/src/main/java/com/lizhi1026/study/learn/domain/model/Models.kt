package com.lizhi1026.study.learn.domain.model

import java.util.UUID

enum class Priority(val value: Int) { Low(0), Medium(1), High(2) }

data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.Medium,
    val createdAt: Long = System.currentTimeMillis(),
    val completedCount: Int = 0,
    val estimatedPomodoros: Int = 1,
) {
    fun toggleCompleted() = copy(isCompleted = !isCompleted)
    fun incCompletedCount() = copy(completedCount = completedCount + 1)
}

enum class SessionType { Work, ShortBreak, LongBreak }

data class PomodoroSession(
    val id: UUID = UUID.randomUUID(),
    val taskId: UUID? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val durationSeconds: Int = 0,
    val completed: Boolean = false,
    val interruptions: Int = 0,
    val type: SessionType = SessionType.Work,
)

data class Settings(
    val workDurationMinutes: Int = 25,
    val shortBreakMinutes: Int = 5,
    val longBreakMinutes: Int = 15,
    val sessionsBeforeLongBreak: Int = 4,
    val notificationSound: String = "default",
    val enableHaptics: Boolean = true,
    val theme: String = "auto"
) {
    val workSeconds get() = workDurationMinutes * 60
    val shortBreakSeconds get() = shortBreakMinutes * 60
    val longBreakSeconds get() = longBreakMinutes * 60
}