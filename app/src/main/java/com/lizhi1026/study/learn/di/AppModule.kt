/**
 * 文件：AppModule.kt
 * 简介：依赖注入模块
 * 关键职责：通过 Hilt 提供依赖与单例
 * 相关组件：Repositories、Room、Engine
 */
package com.lizhi1026.study.learn.di

import android.content.Context
import androidx.room.Room
import com.lizhi1026.study.learn.data.local.AppDatabase
import com.lizhi1026.study.learn.data.repository.SessionRepository
import com.lizhi1026.study.learn.data.repository.TaskRepository
import com.lizhi1026.study.learn.data.settings.SettingsDataStore
import com.lizhi1026.study.learn.timer.PomodoroEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * 提供 Room 数据库单例
     * 使用破坏性迁移回退（开发阶段便捷）
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "fanqie.db")
            .fallbackToDestructiveMigration()
            .build()

    /** 提供任务仓库（封装 TaskDao 数据访问） */
    @Provides
    @Singleton
    fun provideTaskRepository(db: AppDatabase): TaskRepository = TaskRepository(db.taskDao())

    /** 提供会话仓库（封装 SessionDao 数据访问） */
    @Provides
    @Singleton
    fun provideSessionRepository(db: AppDatabase): SessionRepository = SessionRepository(db.sessionDao())

    /** 提供设置存储（DataStore 包装） */
    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext ctx: Context): SettingsDataStore = SettingsDataStore(ctx)

    /** 提供番茄钟引擎单例 */
    @Provides
    @Singleton
    fun provideEngine(
        settings: SettingsDataStore,
        tasks: TaskRepository,
        sessions: SessionRepository,
        @ApplicationContext ctx: Context,
    ): PomodoroEngine = PomodoroEngine(settings, tasks, sessions, ctx)
}