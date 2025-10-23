/**
 * 文件：FanQieApp.kt
 * 简介：Application 初始化入口
 * 关键职责：应用级初始化、Hilt 集成
 * 相关组件：DI、Android Context
 */
package com.lizhi1026.study.learn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FanQieApp : Application()