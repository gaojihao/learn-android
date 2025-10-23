package com.lizhi1026.study.learn.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lizhi1026.study.learn.databinding.ActivityStatisticsBinding
import com.lizhi1026.study.learn.ui.viewmodel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private var _binding: ActivityStatisticsBinding? = null
    private val binding get() = _binding!!

    private val vm: StatisticsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToday.setOnClickListener { vm.setRange(StatisticsViewModel.Range.Today) }
        binding.btnWeek.setOnClickListener { vm.setRange(StatisticsViewModel.Range.Week) }
        binding.btnMonth.setOnClickListener { vm.setRange(StatisticsViewModel.Range.Month) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.summary.collect { s ->
                    binding.tvPomodoros.text = "番茄钟数：${s.pomodoros}"
                    binding.tvCompleted.text = "完成会话：${s.completed}"
                    binding.tvInterruptions.text = "中断次数：${s.interruptions}"
                    binding.tvActiveDays.text = "活跃天数：${s.activeDays}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}