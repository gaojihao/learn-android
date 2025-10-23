/**
 * 文件：SettingsFragment.kt
 * 简介：设置界面 Fragment
 * 关键职责：展示与编辑用户偏好设置
 * 相关组件：SettingsViewModel、SettingsDataStore、布局 activity_settings
 */
package com.lizhi1026.study.learn.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lizhi1026.study.learn.databinding.ActivitySettingsBinding
import com.lizhi1026.study.learn.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    private val vm: SettingsViewModel by viewModels()

    /**
     * 负责创建并返回设置界面的根视图
     * 使用 ViewBinding 进行安全的视图访问
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivitySettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 注册设置项状态订阅，并处理“保存”按钮点击
     * - 通过 repeatOnLifecycle 在 STARTED 期间收集状态
     * - 点击保存时读取输入框和开关状态并写入 DataStore
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.settings.collect { s -> renderSettings(s) }
            }
        }

        binding.btnSave.setOnClickListener {
            val work = binding.etFocusMinutes.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 25
            val shortBreak = binding.etShortBreakMinutes.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 5
            val longBreak = binding.etLongBreakMinutes.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 15
            val interval = binding.etSessionsBeforeLongBreak.text.toString().filter { it.isDigit() }.toIntOrNull() ?: 4
            val enableHaptics = binding.swVibrate.isChecked
            vm.update { it.copy(
                workDurationMinutes = work.coerceIn(1, 999),
                shortBreakMinutes = shortBreak.coerceIn(1, 999),
                longBreakMinutes = longBreak.coerceIn(1, 999),
                sessionsBeforeLongBreak = interval.coerceIn(1, 20),
                enableHaptics = enableHaptics
            ) }
        }
    }

    /**
     * 将设置值渲染到输入框和开关控件
     */
    private fun renderSettings(s: com.lizhi1026.study.learn.domain.model.Settings) {
        binding.etFocusMinutes.setText(s.workDurationMinutes.toString())
        binding.etShortBreakMinutes.setText(s.shortBreakMinutes.toString())
        binding.etLongBreakMinutes.setText(s.longBreakMinutes.toString())
        binding.etSessionsBeforeLongBreak.setText(s.sessionsBeforeLongBreak.toString())
        binding.swVibrate.isChecked = s.enableHaptics
    }

    /**
     * 销毁视图绑定以避免内存泄漏
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}