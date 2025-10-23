package com.lizhi1026.study.learn.timer

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.lizhi1026.study.learn.data.repository.SessionRepository
import com.lizhi1026.study.learn.data.repository.TaskRepository
import com.lizhi1026.study.learn.data.settings.SettingsDataStore
import com.lizhi1026.study.learn.domain.model.PomodoroSession
import com.lizhi1026.study.learn.domain.model.SessionType
import com.lizhi1026.study.learn.domain.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class PomodoroEngine(
    private val settings: SettingsDataStore,
    private val taskRepo: TaskRepository,
    private val sessionRepo: SessionRepository,
    private val context: Context,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private val _currentSession = MutableStateFlow<PomodoroSession?>(null)
    val currentSession: StateFlow<PomodoroSession?> = _currentSession

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask

    private var tickJob: Job? = null
    private var workCountSinceLong = 0

    suspend fun setCurrentTask(taskId: UUID?) {
        _currentTask.value = taskId?.let { taskRepo.getById(it) }
    }

    fun start(type: SessionType, attachTaskId: UUID? = _currentTask.value?.id) {
        scope.launch {
            val s = settings.observe().first()
            val duration = when (type) {
                SessionType.Work -> s.workSeconds
                SessionType.ShortBreak -> s.shortBreakSeconds
                SessionType.LongBreak -> s.longBreakSeconds
            }
            val session = PomodoroSession(
                taskId = attachTaskId,
                startTime = System.currentTimeMillis(),
                durationSeconds = duration,
                completed = false,
                interruptions = 0,
                type = type
            )
            _currentSession.value = session
            _remainingSeconds.value = duration
            _isRunning.value = true
            _isPaused.value = false
            _progress.value = 0f
            startTicker()
        }
    }

    fun pause() {
        _isPaused.value = true
        tickJob?.cancel()
    }

    fun resume() {
        if (_isRunning.value && _isPaused.value) {
            _isPaused.value = false
            startTicker()
        }
    }

    fun stop(reset: Boolean = false) {
        tickJob?.cancel()
        _isRunning.value = false
        _isPaused.value = false
        val cur = _currentSession.value
        if (cur != null) {
            scope.launch {
                sessionRepo.upsert(cur.copy(endTime = System.currentTimeMillis(), completed = false))
            }
        }
        if (reset) {
            _currentSession.value = null
            _remainingSeconds.value = 0
            _progress.value = 0f
        }
    }

    fun interrupt() {
        val cur = _currentSession.value ?: return
        _currentSession.value = cur.copy(interruptions = cur.interruptions + 1)
    }

    private fun startTicker() {
        tickJob?.cancel()
        val total = _currentSession.value?.durationSeconds ?: return
        tickJob = scope.launch {
            while (isActive && _remainingSeconds.value > 0 && !_isPaused.value) {
                kotlinx.coroutines.delay(1000L)
                _remainingSeconds.value = (_remainingSeconds.value - 1).coerceAtLeast(0)
                _progress.value = 1f - (_remainingSeconds.value.toFloat() / total.toFloat())
            }
            if (_remainingSeconds.value <= 0 && !_isPaused.value) {
                onCompleted()
            }
        }
    }

    private fun onCompleted() {
        val cur = _currentSession.value ?: return
        _isRunning.value = false
        _isPaused.value = false
        scope.launch {
            val done = cur.copy(endTime = System.currentTimeMillis(), completed = true)
            sessionRepo.upsert(done)
            vibrate()
            playSound()
            if (done.type == SessionType.Work && done.taskId != null) {
                taskRepo.getById(done.taskId!!)?.let { t ->
                    taskRepo.upsert(t.incCompletedCount())
                }
                workCountSinceLong += 1
            }
            // Auto next session logic
            val s = settings.observe().first()
            val nextType = when (done.type) {
                SessionType.Work -> if (workCountSinceLong >= s.sessionsBeforeLongBreak) {
                    workCountSinceLong = 0
                    SessionType.LongBreak
                } else SessionType.ShortBreak
                SessionType.ShortBreak, SessionType.LongBreak -> SessionType.Work
            }
            start(nextType, cur.taskId)
        }
    }

    private suspend fun vibrate() {
        val s = settings.observe().first()
        if (!s.enableHaptics) return
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val effect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        } else null
        if (effect != null) vibrator.vibrate(effect) else vibrator.vibrate(300)
    }

    private suspend fun playSound() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, uri)
        r.play()
    }
}