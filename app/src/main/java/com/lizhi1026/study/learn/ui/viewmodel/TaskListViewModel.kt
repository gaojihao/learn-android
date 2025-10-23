/**
 * 文件：TaskListViewModel.kt
 * 简介：任务列表 ViewModel
 * 关键职责：加载、增删改查任务数据，提供当前任务
 * 相关组件：Room、Repositories、TaskListFragment
 */
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

    /**
     * 任务列表状态流（根据当前排序规则提供）
     * CreatedAt：按创建时间倒序；Priority：按优先级倒序；Completed：未完成在前
     */
    val tasks: StateFlow<List<Task>> = combine(repo.observeTasks(), sortMode) { list, sort ->
        when (sort) {
            SortMode.CreatedAt -> list.sortedByDescending { it.createdAt }
            SortMode.Priority -> list.sortedByDescending { it.priority.value }
            SortMode.Completed -> list.sortedBy { it.isCompleted }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * 设置排序模式
     * @param mode 排序方式
     */
    fun setSort(mode: SortMode) { sortMode.value = mode }

    /**
     * 新增或更新任务
     * @param id 任务ID（为null则新建）
     * @param title 标题
     * @param description 描述
     * @param priority 优先级
     * @param estimated 预估番茄钟数量
     */
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

    /** 删除任务 */
    fun delete(task: Task) { viewModelScope.launch { repo.delete(task) } }

    /** 切换完成状态（完成↔未完成） */
    fun toggleCompleted(task: Task) { viewModelScope.launch { repo.upsert(task.toggleCompleted()) } }

    /**
     * 排序枚举
     * CreatedAt：创建时间；Priority：优先级；Completed：完成状态
     */
    enum class SortMode { CreatedAt, Priority, Completed }
}