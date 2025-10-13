package com.lizhi1026.study.learn

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val  TAG = "MainActivity"
    /**
     * Activity创建时的回调方法，用于初始化Activity的界面和相关设置
     *
     * @param savedInstanceState 保存的实例状态Bundle对象，用于恢复之前保存的Activity状态，
     *                           如果是首次创建则为null
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用边缘到边缘显示模式，让内容可以延伸到系统UI区域
        enableEdgeToEdge()
        // 设置Activity的布局文件
        setContentView(R.layout.activity_main)
        // 为根视图设置窗口边距监听器，处理系统UI元素（如状态栏、导航栏）的边距适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(TAG, "onCreate: Activity创建，初始化完成");
    }

    /**
     * 当Activity停止时调用此方法
     *
     * 此方法是Activity生命周期的一部分，当Activity不再可见时被调用。
     * 它会先调用父类的onStop方法来确保标准的停止流程得到执行。
     */
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop");
    }

    /**
     * 当Activity被销毁时调用此方法
     *
     * 此方法是Activity生命周期的一部分，在Activity被系统销毁之前执行清理工作
     * 需要先调用父类的onDestroy方法以确保正常的生命周期处理
     */
    override fun onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy()
    }

    /**
     * 当Activity启动时调用此方法
     *
     * 此方法是Activity生命周期的一部分，当Activity变为可见但还未获得焦点时被调用。
     * 通常在此方法中执行一些初始化操作或者启动一些后台任务。
     *
     * 注意：必须调用super.onStart()以确保父类的生命周期逻辑正常执行
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart");
    }

    /**
     * 当Activity恢复到前台时调用此方法
     *
     * 此方法在Activity生命周期中被调用，当Activity从暂停状态恢复到运行状态时执行
     * 通常用于重新获取焦点、恢复动画、注册广播接收器等操作
     *
     * 注意：此方法会先调用父类的onResume方法，确保父类的恢复逻辑先执行
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume");
    }

    /**
     * 当Activity失去焦点并进入暂停状态时调用此方法。
     * 在这里可以执行一些暂停相关的操作，如停止动画、释放资源等。
     * 注意：此方法在Activity被部分遮挡或完全不可见时都会被调用。
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause");
    }

    /**
     * 当Activity从停止状态重新启动时调用此方法。
     * 此方法在Activity生命周期中位于onStop()之后，onStart()之前。
     * 通常用于在Activity重新变为可见时执行必要的初始化操作。
     */
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart");
    }

    /**
     * 当Activity从被系统销毁的状态恢复时调用此方法，用于恢复之前保存的实例状态
     *
     * @param savedInstanceState 包含之前保存的实例状态数据的Bundle对象，如果之前没有保存状态则为null
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState");
    }

    /**
     * 保存Activity的状态信息到Bundle中
     *
     * @param outState 用于保存状态信息的Bundle对象，系统会在Activity被销毁时保存这些数据，
     *                在Activity重新创建时通过onCreate(Bundle?)和onRestoreInstanceState(Bundle)方法恢复
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState");
    }

    /**
     * 当用户按下Home键或者有电话接入时触发的回调方法
     *
     * 此方法在Activity生命周期中被调用，表示用户即将离开当前应用界面。
     * 通常用于处理用户离开时的逻辑，比如暂停视频播放、保存数据等操作。
     *
     * 注意：此方法不会在所有情况下都被调用，例如当Activity被系统销毁时可能不会触发
     */
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d(TAG, "onUserLeaveHint");
    }

    /**
     * 当系统内存不足时调用此方法
     *
     * 此方法是Android生命周期回调方法，当系统内存不足时会通知应用程序
     * 应用程序可以在此方法中释放不必要的资源以帮助系统回收内存
     */
    override fun onLowMemory() {
        super.onLowMemory()
    }

    /**
     * 当系统内存不足时，系统会回调此方法通知应用进行内存清理
     * @param level 内存紧张级别，数值越大表示内存越紧张
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    /**
     * 当视图附加到窗口时调用此方法
     *
     * 此方法在视图被添加到窗口时触发，通常用于执行一些初始化操作
     * 或者注册监听器等需要在视图显示时进行的操作
     *
     * 注意：此方法会调用父类的onAttachedToWindow()方法以确保
     * 父类的逻辑能够正常执行
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow");
    }

    /**
     * 当视图从窗口中分离时调用此方法。 View级别的生命周期方法 适合放置一些清理View的逻辑
     * 这是View生命周期中的一个重要回调方法，通常用于清理资源或取消注册监听器。
     * 注意：此方法会调用父类的onDetachedFromWindow()方法，确保继承链上的所有清理逻辑都能正常执行。
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow");
    }

    /**
     * 当窗口焦点状态发生变化时调用此方法
     * @param hasFocus 表示当前窗口是否获得焦点的布尔值
     *                 true表示窗口获得焦点，false表示窗口失去焦点
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.d(TAG, "onWindowFocusChanged");
    }

    /**
     * 当设备配置发生改变时调用此方法
     * @param newConfig 新的配置信息，包含屏幕方向、字体大小等配置变更详情
     */
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged");
    }

    /**
     * 当用户与应用程序交互时调用此方法。
     * 此方法是Activity生命周期的一部分，当用户执行任何交互操作时都会触发，
     * 例如触摸屏幕、按键、滚动等。默认实现会调用父类的相应方法。
    */
    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d(TAG, "onUserInteraction");
    }



}