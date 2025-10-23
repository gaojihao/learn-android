/**
 * 文件：StatisticsViewModel.kt
 * 简介：统计 ViewModel
 * 关键职责：聚合统计数据并提供给 UI
 * 相关组件：Room、Repositories、StatisticsFragment
 */
package com.lizhi1026.study.learn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhi1026.study.learn.data.repository.SessionRepository
import com.lizhi1026.study.learn.domain.model.SessionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repo: SessionRepository,
) : ViewModel() {
    private val range = MutableStateFlow(Range.Today)

    private fun sinceMillis(): Long {
        val cal = Calendar.getInstance()
        return when (range.value) {
            Range.Today -> cal.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
            Range.Week -> cal.apply { set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
            Range.Month -> cal.apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
        }
    }

    /**
     * 统计摘要状态流（基于当前选择的时间范围）
     * 聚合番茄钟数、完成会话、中断次数与活跃天数
     */
    val summary: StateFlow<Summary> = repo.observeSince(sinceMillis()).map { list ->
        val completed = list.filter { it.completed }
        val pomodoros = completed.count { it.type == SessionType.Work }
        val interruptions = list.sumOf { it.interruptions }
        val activeDays = completed.mapNotNull { it.startTime }.map { it / (24 * 60 * 60 * 1000) }.distinct().size
        Summary(pomodoros, completed.size, interruptions, activeDays)
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, Summary(0,0,0,0))

    /**
     * 设置统计时间范围
     * @param r 统计的时间范围（今日/本周/本月）
     */
    fun setRange(r: Range) { range.value = r }

    /**
     * 统计摘要
     * @property pomodoros 番茄钟数量（工作会话）
     * @property completed 完成会话数量
     * @property interruptions 中断总次数
     * @property activeDays 有完成记录的活跃天数
     */
    data class Summary(val pomodoros: Int, val completed: Int, val interruptions: Int, val activeDays: Int)
    /**
     * 时间范围枚举
     * Today：今天；Week：本周；Month：本月
     */
    enum class Range { Today, Week, Month }
}