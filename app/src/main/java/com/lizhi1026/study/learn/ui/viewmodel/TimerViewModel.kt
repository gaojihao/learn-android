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

    fun setCurrentTask(taskId: UUID?) {
        viewModelScope.launch { engine.setCurrentTask(taskId) }
    }

    fun start(type: SessionType) {
        val ctx = getApplication<Application>()
        PomodoroService.startForeground(ctx, type)
    }

    fun pause() = engine.pause()
    fun resume() = engine.resume()
    fun stop(reset: Boolean = true) = engine.stop(reset)
    fun interrupt() = engine.interrupt()
}