package com.lizhi1026.study.learn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val priority: Int,
    val createdAt: Long,
    val completedCount: Int,
    val estimatedPomodoros: Int,
)