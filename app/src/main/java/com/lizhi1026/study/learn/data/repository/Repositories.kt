/**
 * 文件：Repositories.kt
 * 简介：仓库聚合入口
 * 关键职责：封装数据访问、对外提供 Repository
 * 相关组件：DAO、Room、ViewModel
 */
package com.lizhi1026.study.learn.data.repository

import com.lizhi1026.study.learn.data.local.dao.SessionDao
import com.lizhi1026.study.learn.data.local.dao.TaskDao
import com.lizhi1026.study.learn.data.local.entity.SessionEntity
import com.lizhi1026.study.learn.data.local.entity.TaskEntity
import com.lizhi1026.study.learn.domain.model.PomodoroSession
import com.lizhi1026.study.learn.domain.model.Priority
import com.lizhi1026.study.learn.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TaskRepository(private val dao: TaskDao) {
    /**
     * 观察任务列表（领域模型）
     * 将数据库实体映射为领域对象并按流提供
     */
    fun observeTasks(): Flow<List<Task>> = dao.observeAll().map { it.map(::toDomain) }
    /** 插入或更新任务（Upsert） */
    suspend fun upsert(task: Task) = dao.upsert(toEntity(task))
    suspend fun delete(task: Task) = dao.delete(toEntity(task))
    /** 按 ID 查询任务（返回 null 表示未找到） */
    suspend fun getById(id: UUID): Task? = dao.getById(id)?.let(::toDomain)

    private fun toDomain(e: TaskEntity) = Task(
        id = e.id,
        title = e.title,
        description = e.description,
        isCompleted = e.isCompleted,
        priority = when (e.priority) { 0 -> Priority.Low; 1 -> Priority.Medium; else -> Priority.High },
        createdAt = e.createdAt,
        completedCount = e.completedCount,
        estimatedPomodoros = e.estimatedPomodoros,
    )
    private fun toEntity(d: Task) = TaskEntity(
        id = d.id,
        title = d.title,
        description = d.description,
        isCompleted = d.isCompleted,
        priority = d.priority.value,
        createdAt = d.createdAt,
        completedCount = d.completedCount,
        estimatedPomodoros = d.estimatedPomodoros,
    )
}

class SessionRepository(private val dao: SessionDao) {
    /**
     * 观察指定时间以来的会话列表
     * @param since 起始时间（毫秒）
     */
    fun observeSince(since: Long): Flow<List<PomodoroSession>> = dao.observeSince(since).map { it.map(::toDomain) }
    /** 插入或更新会话（Upsert） */
    suspend fun upsert(session: PomodoroSession) = dao.upsert(toEntity(session))
    /** 按 ID 查询会话（返回 null 表示未找到） */
    suspend fun getById(id: UUID): PomodoroSession? = dao.getById(id)?.let(::toDomain)
    /**
     * 清理旧会话
     * @param threshold 删除该时间戳之前的记录
     */
    suspend fun cleanup(threshold: Long) = dao.deleteOlderThan(threshold)

    private fun toDomain(e: SessionEntity) = PomodoroSession(
        id = e.id,
        taskId = e.taskId,
        startTime = e.startTime,
        endTime = e.endTime,
        durationSeconds = e.durationSeconds,
        completed = e.completed,
        interruptions = e.interruptions,
        type = when (e.type) {
            "work" -> com.lizhi1026.study.learn.domain.model.SessionType.Work
            "shortBreak" -> com.lizhi1026.study.learn.domain.model.SessionType.ShortBreak
            else -> com.lizhi1026.study.learn.domain.model.SessionType.LongBreak
        }
    )
    private fun toEntity(d: PomodoroSession) = SessionEntity(
        id = d.id,
        taskId = d.taskId,
        startTime = d.startTime,
        endTime = d.endTime,
        durationSeconds = d.durationSeconds,
        completed = d.completed,
        interruptions = d.interruptions,
        type = when (d.type) {
            com.lizhi1026.study.learn.domain.model.SessionType.Work -> "work"
            com.lizhi1026.study.learn.domain.model.SessionType.ShortBreak -> "shortBreak"
            com.lizhi1026.study.learn.domain.model.SessionType.LongBreak -> "longBreak"
        }
    )
}