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
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "fanqie.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTaskRepository(db: AppDatabase): TaskRepository = TaskRepository(db.taskDao())

    @Provides
    @Singleton
    fun provideSessionRepository(db: AppDatabase): SessionRepository = SessionRepository(db.sessionDao())

    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext ctx: Context): SettingsDataStore = SettingsDataStore(ctx)

    @Provides
    @Singleton
    fun provideEngine(
        settings: SettingsDataStore,
        tasks: TaskRepository,
        sessions: SessionRepository,
        @ApplicationContext ctx: Context,
    ): PomodoroEngine = PomodoroEngine(settings, tasks, sessions, ctx)
}