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
    fun observeTasks(): Flow<List<Task>> = dao.observeAll().map { it.map(::toDomain) }
    suspend fun upsert(task: Task) = dao.upsert(toEntity(task))
    suspend fun delete(task: Task) = dao.delete(toEntity(task))
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
    fun observeSince(since: Long): Flow<List<PomodoroSession>> = dao.observeSince(since).map { it.map(::toDomain) }
    suspend fun upsert(session: PomodoroSession) = dao.upsert(toEntity(session))
    suspend fun getById(id: UUID): PomodoroSession? = dao.getById(id)?.let(::toDomain)
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