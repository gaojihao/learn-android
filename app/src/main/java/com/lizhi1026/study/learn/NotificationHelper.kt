/**
 * 文件：NotificationHelper.kt
 * 简介：通知工具
 * 关键职责：渠道创建、通知构建/更新
 * 相关组件：Service、系统通知
 */
package com.lizhi1026.study.learn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object NotificationHelper {
    private const val CHANNEL_ID = "pomodoro_channel"
    private const val CHANNEL_NAME = "Pomodoro Timer"
    private const val NOTIFICATION_ID = 1001

    /**
     * 确保通知渠道存在（Android O+）
     */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(channel)
        }
    }

    /**
     * 显示或更新进行中的番茄钟通知
     * @param title 通知标题（当前会话类型与任务）
     * @param timeText 剩余时间文本
     * @param running 是否常驻（ongoing）
     */
    fun showOngoing(context: Context, title: String, timeText: String, running: Boolean) {
        if (!hasNotifPerm(context)) return
        ensureChannel(context)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or flagsCompat()
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(timeText)
            .setContentIntent(pi)
            .setOngoing(running)
            .setOnlyAlertOnce(true)
        nm.notify(NOTIFICATION_ID, builder.build())
    }

    /**
     * 取消番茄钟通知（如会话结束或服务销毁）
     */
    fun cancel(context: Context) {
        if (!hasNotifPerm(context)) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_ID)
    }

    private fun flagsCompat(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
    }

    private fun hasNotifPerm(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}