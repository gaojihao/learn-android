package com.lizhi1026.study.learn.data.local

import androidx.room.TypeConverter
import java.util.UUID

class UuidConverters {
    @TypeConverter
    fun fromString(id: String?): UUID? = id?.let { UUID.fromString(it) }
    @TypeConverter
    fun toString(id: UUID?): String? = id?.toString()
}