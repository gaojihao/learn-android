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
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val vm: TimerViewModel by activityViewModels()

    private var pendingType: SessionType? = null
    private val notifReq = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pendingType?.let { vm.start(it) }
        pendingType = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                launch {
                    vm.remainingSeconds.collect { s -> binding.tvTime.text = "%02d:%02d".format(s / 60, s % 60) }
                }
                launch {
                    vm.progress.collect { p ->
                        binding.progress.isIndeterminate = false
                        binding.progress.setProgress((p * 100).toInt())
                    }
                }
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
                // Update start/pause button text whenever either running or paused state changes
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

        binding.btnStartPause.setOnClickListener {
            val running = vm.isRunning.value
            val paused = vm.isPaused.value
            when {
                !running -> startWithPermission(SessionType.Work)
                paused -> vm.resume()
                else -> vm.pause()
            }
        }
        binding.btnReset.setOnClickListener { vm.stop(reset = true) }
        binding.btnSkip.setOnClickListener { vm.interrupt() }
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}