package com.lizhi1026.study.learn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhi1026.study.learn.data.repository.TaskRepository
import com.lizhi1026.study.learn.domain.model.Priority
import com.lizhi1026.study.learn.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repo: TaskRepository,
) : ViewModel() {
    private val sortMode = MutableStateFlow(SortMode.CreatedAt)

    val tasks: StateFlow<List<Task>> = combine(repo.observeTasks(), sortMode) { list, sort ->
        when (sort) {
            SortMode.CreatedAt -> list.sortedByDescending { it.createdAt }
            SortMode.Priority -> list.sortedByDescending { it.priority.value }
            SortMode.Completed -> list.sortedBy { it.isCompleted }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setSort(mode: SortMode) { sortMode.value = mode }

    fun addOrUpdate(
        id: UUID? = null,
        title: String,
        description: String,
        priority: Priority,
        estimated: Int,
    ) {
        val task = Task(
            id = id ?: UUID.randomUUID(),
            title = title,
            description = description,
            priority = priority,
            estimatedPomodoros = estimated
        )
        viewModelScope.launch { repo.upsert(task) }
    }

    fun delete(task: Task) { viewModelScope.launch { repo.delete(task) } }

    fun toggleCompleted(task: Task) { viewModelScope.launch { repo.upsert(task.toggleCompleted()) } }

    enum class SortMode { CreatedAt, Priority, Completed }
}