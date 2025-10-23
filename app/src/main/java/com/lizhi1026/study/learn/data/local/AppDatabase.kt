/**
 * 文件：AppDatabase.kt
 * 简介：Room 数据库
 * 关键职责：数据库配置、版本与实体注册
 * 相关组件：DAO、Entities、Converters
 */
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
    /** 提供任务 DAO（访问任务表的接口） */
    abstract fun taskDao(): TaskDao
    /** 提供会话 DAO（访问会话表的接口） */
    abstract fun sessionDao(): SessionDao
}