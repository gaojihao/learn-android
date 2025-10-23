/**
 * 文件：Models.kt
 * 简介：领域模型
 * 关键职责：定义核心数据结构（任务、会话、类型等）
 * 相关组件：Engine、ViewModel、Room
 */
package com.lizhi1026.study.learn.domain.model

import java.util.UUID

/**
 * 任务优先级
 * Low(0)：低；Medium(1)：中；High(2)：高
 */
enum class Priority(val value: Int) { Low(0), Medium(1), High(2) }

/**
 * 任务模型
 * @property id 任务唯一 ID
 * @property title 标题
 * @property description 描述
 * @property isCompleted 是否完成
 * @property priority 优先级
 * @property createdAt 创建时间戳（毫秒）
 * @property completedCount 完成的番茄钟数量
 * @property estimatedPomodoros 预估番茄钟数量
 */
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
    /** 切换完成状态（完成↔未完成） */
    fun toggleCompleted() = copy(isCompleted = !isCompleted)
    /** 完成计数 +1（会话完成后调用） */
    fun incCompletedCount() = copy(completedCount = completedCount + 1)
}

/**
 * 会话类型
 * Work：专注；ShortBreak：短休息；LongBreak：长休息
 */
enum class SessionType { Work, ShortBreak, LongBreak }

/**
 * 番茄钟会话模型
 * @property id 会话 ID
 * @property taskId 关联任务 ID（可为空）
 * @property startTime 开始时间（毫秒）
 * @property endTime 结束时间（毫秒）
 * @property durationSeconds 会话时长（秒）
 * @property completed 是否已完成
 * @property interruptions 中断次数
 * @property type 会话类型（工作/短休/长休）
 */
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

/**
 * 设置模型（用户偏好）
 * 包含时长、间隔、通知与主题相关配置
 */
data class Settings(
    val workDurationMinutes: Int = 25,
    val shortBreakMinutes: Int = 5,
    val longBreakMinutes: Int = 15,
    val sessionsBeforeLongBreak: Int = 4,
    val notificationSound: String = "default",
    val enableHaptics: Boolean = true,
    val theme: String = "auto"
) {
    /** 工作时长（秒） */
    val workSeconds get() = workDurationMinutes * 60
    /** 短休息时长（秒） */
    val shortBreakSeconds get() = shortBreakMinutes * 60
    /** 长休息时长（秒） */
    val longBreakSeconds get() = longBreakMinutes * 60
}