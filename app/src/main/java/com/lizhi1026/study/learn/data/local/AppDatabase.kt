package com.lizhi1026.study.learn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lizhi1026.study.learn.data.local.dao.SessionDao
import com.lizhi1026.study.learn.data.local.dao.TaskDao
import com.lizhi1026.study.learn.data.local.entity.SessionEntity
import com.lizhi1026.study.learn.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class, SessionEntity::class], version = 1)
@TypeConverters(UuidConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}