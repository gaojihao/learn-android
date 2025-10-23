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