/**
 * 文件：PomodoroEngine.kt
 * 简介：番茄钟核心引擎
 * 关键职责：计时、会话切换、暂停/继续
 * 相关组件：Service、ViewModel
 */
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

    /**
     * 设置当前任务
     * @param taskId 当前任务的 UUID（null 表示未选择）
     * 从仓库加载任务并更新状态流
     */
    suspend fun setCurrentTask(taskId: UUID?) {
        _currentTask.value = taskId?.let { taskRepo.getById(it) }
    }

    /**
     * 开始一个番茄钟会话
     * @param type 会话类型（工作/短休/长休）
     * @param attachTaskId 关联的任务ID（可选，默认取当前任务）
     * 初始化会话、重置状态并启动计时器
     */
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

    /**
     * 暂停当前会话计时
     * 取消定时 Job，但保留剩余秒数与当前会话
     */
    fun pause() {
        _isPaused.value = true
        tickJob?.cancel()
    }

    /**
     * 继续计时
     * 在运行且处于暂停状态时，重启定时器
     */
    fun resume() {
        if (_isRunning.value && _isPaused.value) {
            _isPaused.value = false
            startTicker()
        }
    }

    /**
     * 停止当前会话
     * @param reset 是否重置会话（true时清空当前会话与进度）
     * 取消定时器、标记未完成并持久化
     */
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

    /**
     * 记录一次打断事件
     * 增加当前会话的 interruptions 计数
     */
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

    /**
     * 会话完成处理与自动切换逻辑
     * - 标记并持久化完成的会话，触发震动与提示音
     * - 工作会话完成时为关联任务计数 +1，并累积 `workCountSinceLong`
     * - 根据设置 `sessionsBeforeLongBreak` 决定下一个会话类型：
     *   - `Work` → `ShortBreak`；当累计达到阈值时 → `LongBreak` 并重置累计
     *   - `ShortBreak`/`LongBreak` → `Work`
     * - 自动调用 `start(nextType, cur.taskId)` 开启下一段
     */
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