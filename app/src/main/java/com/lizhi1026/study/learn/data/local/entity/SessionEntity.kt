/**
 * 文件：SessionEntity.kt
 * 简介：Room 实体
 * 关键职责：定义会话数据表结构与字段
 * 相关组件：Room、Converters、SessionDao
 */
package com.lizhi1026.study.learn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: UUID,
    val taskId: UUID?,
    val startTime: Long?,
    val endTime: Long?,
    val durationSeconds: Int,
    val completed: Boolean,
    val interruptions: Int,
    val type: String,
)