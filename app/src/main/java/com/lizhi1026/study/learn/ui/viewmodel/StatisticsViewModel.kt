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

    val summary: StateFlow<Summary> = repo.observeSince(sinceMillis()).map { list ->
        val completed = list.filter { it.completed }
        val pomodoros = completed.count { it.type == SessionType.Work }
        val interruptions = list.sumOf { it.interruptions }
        val activeDays = completed.mapNotNull { it.startTime }.map { it / (24 * 60 * 60 * 1000) }.distinct().size
        Summary(pomodoros, completed.size, interruptions, activeDays)
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, Summary(0,0,0,0))

    fun setRange(r: Range) { range.value = r }

    data class Summary(val pomodoros: Int, val completed: Int, val interruptions: Int, val activeDays: Int)
    enum class Range { Today, Week, Month }
}