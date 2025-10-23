/**
 * 文件：TaskEntity.kt
 * 简介：Room 实体
 * 关键职责：定义任务数据表结构与字段
 * 相关组件：Room、Converters、TaskDao
 */
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