package com.lizhi1026.study.learn.timer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lizhi1026.study.learn.MainActivity
import com.lizhi1026.study.learn.NotificationHelper
import com.lizhi1026.study.learn.domain.model.SessionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.util.Log

@AndroidEntryPoint
class PomodoroService : Service() {
    @Inject lateinit var engine: PomodoroEngine

    private val scope = CoroutineScope(Dispatchers.Main)
    private var observeJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val type = when (intent.getStringExtra(EXTRA_TYPE)) {
                    "work" -> SessionType.Work
                    "shortBreak" -> SessionType.ShortBreak
                    else -> SessionType.LongBreak
                }
                engine.start(type, null)
                val hasNotifPerm = Build.VERSION.SDK_INT < 33 ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                if (hasNotifPerm) {
                    try {
                        NotificationHelper.ensureChannel(this)
                        startForeground(NOTIF_ID, buildNotification(this))
                    } catch (e: Exception) {
                        Log.e("PomodoroService", "startForeground failed, fallback", e)
                        // 如果前台失败，保持普通服务运行
                    }
                }
                startObserving()
            }
            ACTION_PAUSE -> engine.pause()
            ACTION_RESUME -> engine.resume()
            ACTION_STOP -> {
                engine.stop(reset = true)
                try { stopForeground(STOP_FOREGROUND_REMOVE) } catch (_: Exception) {}
                stopSelf()
            }
            else -> startObserving()
        }
        return START_STICKY
    }

    private fun startObserving() {
        observeJob?.cancel()
        observeJob = scope.launch {
            engine.remainingSeconds.collectLatest { seconds ->
                NotificationHelper.showOngoing(
                    this@PomodoroService,
                    title = currentSessionTitle(),
                    timeText = formatTime(seconds),
                    running = engine.isRunning.value && !engine.isPaused.value
                )
            }
        }
    }

    override fun onDestroy() {
        observeJob?.cancel()
        NotificationHelper.cancel(this)
        super.onDestroy()
    }

    private fun piFlags(): Int {
        return PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
    }

    private fun buildNotification(ctx: Context): Notification {
        val openIntent = Intent(ctx, MainActivity::class.java)
        val contentPending = PendingIntent.getActivity(
            ctx, 0, openIntent, piFlags()
        )

        val pauseIntent = Intent(ctx, PomodoroService::class.java).apply { action = ACTION_PAUSE }
        val resumeIntent = Intent(ctx, PomodoroService::class.java).apply { action = ACTION_RESUME }
        val stopIntent = Intent(ctx, PomodoroService::class.java).apply { action = ACTION_STOP }

        return NotificationCompat.Builder(ctx, "pomodoro_channel")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(currentSessionTitle())
            .setContentText(formatTime(engine.remainingSeconds.value))
            .setContentIntent(contentPending)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_media_pause, "暂停", PendingIntent.getService(ctx, 1, pauseIntent, piFlags()))
            .addAction(android.R.drawable.ic_media_play, "继续", PendingIntent.getService(ctx, 2, resumeIntent, piFlags()))
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "停止", PendingIntent.getService(ctx, 3, stopIntent, piFlags()))
            .build()
    }

    private fun currentSessionTitle(): String {
        val type = engine.currentSession.value?.type ?: SessionType.Work
        val prefix = when (type) {
            SessionType.Work -> "工作"
            SessionType.ShortBreak -> "短休息"
            SessionType.LongBreak -> "长休息"
        }
        val taskTitle = engine.currentTask.value?.title
        return if (taskTitle.isNullOrEmpty()) prefix else "$prefix · 当前任务：$taskTitle"
    }

    private fun formatTime(s: Int): String {
        val m = s / 60
        val sec = s % 60
        return "%02d:%02d".format(m, sec)
    }

    companion object {
        const val NOTIF_ID = 1001
        const val ACTION_START = "com.lizhi1026.study.learn.timer.ACTION_START"
        const val ACTION_PAUSE = "com.lizhi1026.study.learn.timer.ACTION_PAUSE"
        const val ACTION_RESUME = "com.lizhi1026.study.learn.timer.ACTION_RESUME"
        const val ACTION_STOP = "com.lizhi1026.study.learn.timer.ACTION_STOP"
        const val EXTRA_TYPE = "type"

        fun startForeground(ctx: Context, type: SessionType) {
            val i = Intent(ctx, PomodoroService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_TYPE, when (type) {
                    SessionType.Work -> "work"
                    SessionType.ShortBreak -> "shortBreak"
                    SessionType.LongBreak -> "longBreak"
                })
            }
            val isT = Build.VERSION.SDK_INT >= 33
            val hasNotifPerm = !isT ||
                    ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            try {
                if (hasNotifPerm) ctx.startForegroundService(i) else ctx.startService(i)
            } catch (e: Exception) {
                Log.e("PomodoroService", "start service failed, fallback to startService", e)
                // 回退到普通服务
                try { ctx.startService(i) } catch (_: Exception) {}
            }
        }
    }
}