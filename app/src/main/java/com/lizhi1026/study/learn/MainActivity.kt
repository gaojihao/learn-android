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
    private val notifReq = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingType?.let { vm.start(it) }
        pendingType = null
    }

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