/**
 * 文件：MainActivity.kt
 * 简介：宿主 Activity
 * 关键职责：承载导航与 Fragment 容器，权限委托
 * 相关组件：NavHost、Fragments、ViewModel
 */
package com.lizhi1026.study.learn

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lizhi1026.study.learn.databinding.ActivityMainBinding
import com.lizhi1026.study.learn.domain.model.SessionType
import com.lizhi1026.study.learn.ui.viewmodel.TimerViewModel
import com.lizhi1026.study.learn.ui.tabs.SettingsFragment
import com.lizhi1026.study.learn.ui.tabs.StatisticsFragment
import com.lizhi1026.study.learn.ui.tabs.TaskListFragment
import com.lizhi1026.study.learn.ui.tabs.TimerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: TimerViewModel by viewModels()

    private var pendingType: SessionType? = null
    /**
     * Android 13+ 通知权限申请结果回调
     * 权限授予后启动待启动的会话类型；否则清空等待状态
     */
    private val notifReq = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingType?.let { vm.start(it) }
        pendingType = null
    }

    /**
     * Activity 创建：启用沉浸式边缘、初始化 ViewBinding 与底部导航
     * 首次进入时加载默认的 TimerFragment 到容器中
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(binding.navHostContainer.id, TimerFragment())
            }
        }
    
        val bottomNav: BottomNavigationView = binding.bottomNav
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_timer -> {
                    supportFragmentManager.commit { replace(binding.navHostContainer.id, TimerFragment()) }
                    true
                }
                R.id.nav_tasks -> {
                    supportFragmentManager.commit { replace(binding.navHostContainer.id, TaskListFragment()) }
                    true
                }
                R.id.nav_stats -> {
                    supportFragmentManager.commit { replace(binding.navHostContainer.id, StatisticsFragment()) }
                    true
                }
                R.id.nav_settings -> {
                    supportFragmentManager.commit { replace(binding.navHostContainer.id, SettingsFragment()) }
                    true
                }
                else -> false
            }
        }
    }
}