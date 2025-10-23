/**
 * 文件：UuidConverters.kt
 * 简介：Room 类型转换器
 * 关键职责：UUID 与数据库存储类型的互转
 * 相关组件：Room、Entities
 */
package com.lizhi1026.study.learn.data.local

import androidx.room.TypeConverter
import java.util.UUID

class UuidConverters {
    @TypeConverter
    fun fromString(id: String?): UUID? = id?.let { UUID.fromString(it) }
    @TypeConverter
    fun toString(id: UUID?): String? = id?.toString()
}