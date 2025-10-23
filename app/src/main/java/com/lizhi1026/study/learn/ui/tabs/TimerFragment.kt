/**
 * 文件：TimerFragment.kt
 * 简介：计时器界面 Fragment
 * 关键职责：订阅 ViewModel 状态、更新 UI、处理交互
 * 相关组件：TimerViewModel、PomodoroService、布局 fragment_timer
 */
package com.lizhi1026.study.learn.ui.tabs

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lizhi1026.study.learn.databinding.FragmentTimerBinding
import com.lizhi1026.study.learn.domain.model.PomodoroSession
import com.lizhi1026.study.learn.domain.model.SessionType
import com.lizhi1026.study.learn.ui.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class TimerFragment : Fragment() {
    // ViewBinding，用于安全、便捷地访问布局视图
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    // 共享的计时 ViewModel：在 Activity 范围内共享，用于跨标签页同步计时状态
    private val vm: TimerViewModel by activityViewModels()

    // Android 13+ 通知权限申请流程：记录待启动的会话类型，权限授予后再启动
    private var pendingType: SessionType? = null
    private val notifReq = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingType?.let { vm.start(it) }
        pendingType = null
    }

    /**
     * 创建并返回计时器界面的根视图
     * 使用 ViewBinding 安全绑定布局
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // 创建并返回根视图（绑定布局）
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 视图创建完成：订阅 ViewModel 状态并更新 UI
     * 在 STARTED 生命周期内收集多条状态流（剩余时间/进度/会话类型/按钮文字）
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        // 在 STARTED 生命周期内订阅状态流并更新 UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                // 订阅剩余时间（秒）并格式化为 mm:ss 文本显示
                launch {
                    vm.remainingSeconds.collect { s -> binding.tvTime.text = "%02d:%02d".format(s / 60, s % 60) }
                }
                // 订阅进度（0..1）并更新圆形进度指示器
                launch {
                    vm.progress.collect { p ->
                        binding.progress.isIndeterminate = false
                        binding.progress.setProgress((p * 100).toInt())
                    }
                }
                // 订阅当前会话类型并显示阶段标签（专注/短休息/长休息）
                launch {
                    vm.currentSession.collect { session: PomodoroSession? ->
                        val label = when (session?.type ?: SessionType.Work) {
                            SessionType.Work -> "专注"
                            SessionType.ShortBreak -> "短休息"
                            SessionType.LongBreak -> "长休息"
                        }
                        binding.tvPhase.text = label
                    }
                }
                // 同时监听“是否运行”和“是否暂停”两个状态，任一变化都更新按钮文字
                launch {
                    combine(vm.isRunning, vm.isPaused) { running, paused -> running to paused }
                        .collect { (running, paused) ->
                            binding.btnStartPause.text = when {
                                !running -> "开始"
                                paused -> "继续"
                                else -> "暂停"
                            }
                        }
                }
            }
        }
    
        // 开始/暂停/继续按钮：根据当前状态选择操作
        binding.btnStartPause.setOnClickListener {
            val running = vm.isRunning.value
            val paused = vm.isPaused.value
            when {
                // 未运行：请求权限后开始新的“专注”会话
                !running -> startWithPermission(SessionType.Work)
                // 已暂停：恢复计时
                paused -> vm.resume()
                // 正在运行：暂停计时
                else -> vm.pause()
            }
        }
        // 重置按钮：停止并重置当前会话
        binding.btnReset.setOnClickListener { vm.stop(reset = true) }
        // 跳过按钮：中断当前会话并切换到下一个会话（如短休息/下次专注）
        binding.btnSkip.setOnClickListener { vm.interrupt() }
    }

    // Android 13+ 需要通知权限以显示前台通知：
    // 如果缺少权限则发起申请；权限已授予或低版本则直接启动前台服务
    /**
     * 启动会话前检查 Android 13+ 通知权限
     * 无权限则申请，权限通过或低版本直接启动前台服务
     */
    private fun startWithPermission(type: SessionType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!granted) {
                pendingType = type
                notifReq.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        vm.start(type)
    }

    /**
     * 视图销毁：释放 ViewBinding 引用以防止泄漏
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // 解除绑定以避免内存泄漏（Fragment 视图销毁后不再持有）
        _binding = null
    }
}