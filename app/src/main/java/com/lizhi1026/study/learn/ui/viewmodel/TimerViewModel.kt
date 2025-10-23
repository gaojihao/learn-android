/**
 * 文件：TimerViewModel.kt
 * 简介：计时器 ViewModel
 * 关键职责：暴露状态流、启动前台服务、协调引擎
 * 相关组件：PomodoroEngine、PomodoroService、TimerFragment
 */
package com.lizhi1026.study.learn.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lizhi1026.study.learn.domain.model.SessionType
import com.lizhi1026.study.learn.timer.PomodoroEngine
import com.lizhi1026.study.learn.timer.PomodoroService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    application: Application,
    private val engine: PomodoroEngine,
) : AndroidViewModel(application) {

    val remainingSeconds: StateFlow<Int> = engine.remainingSeconds.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val isRunning: StateFlow<Boolean> = engine.isRunning.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val isPaused: StateFlow<Boolean> = engine.isPaused.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val currentSession = engine.currentSession.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val progress = engine.progress.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)
    val currentTask = engine.currentTask.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * 设置当前任务（交由引擎加载并更新状态）
     * @param taskId 任务ID（null 表示清空当前任务）
     */
    fun setCurrentTask(taskId: UUID?) {
        viewModelScope.launch { engine.setCurrentTask(taskId) }
    }

    /**
     * 开始指定类型的会话（以前台服务方式）
     * @param type 会话类型（工作/短休/长休）
     */
    fun start(type: SessionType) {
        val ctx = getApplication<Application>()
        PomodoroService.startForeground(ctx, type)
    }

    /** 暂停当前会话计时 */
    fun pause() = engine.pause()
    /** 继续计时 */
    fun resume() = engine.resume()
    /**
     * 停止当前会话
     * @param reset 是否重置（清空当前会话与进度）
     */
    fun stop(reset: Boolean = true) = engine.stop(reset)
    /** 记录一次打断事件 */
    fun interrupt() = engine.interrupt()
}