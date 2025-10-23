/**
 * 文件：TaskListFragment.kt
 * 简介：任务列表界面 Fragment
 * 关键职责：展示任务列表、选择当前任务
 * 相关组件：TaskListViewModel、Room、布局 activity_task_list
 */
package com.lizhi1026.study.learn.ui.tabs

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lizhi1026.study.learn.databinding.ActivityTaskListBinding
import com.lizhi1026.study.learn.databinding.ItemTaskBinding
import com.lizhi1026.study.learn.domain.model.Priority
import com.lizhi1026.study.learn.domain.model.Task
import com.lizhi1026.study.learn.domain.model.SessionType
import com.lizhi1026.study.learn.ui.viewmodel.TaskListViewModel
import com.lizhi1026.study.learn.ui.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskListFragment : Fragment() {
    private var _binding: ActivityTaskListBinding? = null
    private val binding get() = _binding!!

    private val vm: TaskListViewModel by viewModels()
    private val timerVm: TimerViewModel by activityViewModels()

    private var pendingStartTaskId: java.util.UUID? = null
    private val notifReq = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        val id = pendingStartTaskId
        pendingStartTaskId = null
        if (granted && id != null) {
            timerVm.setCurrentTask(id)
            timerVm.start(SessionType.Work)
        }
    }

    /**
     * 创建并返回任务列表的根视图
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 初始化 RecyclerView 与适配器，订阅任务列表并渲染
     * 同时处理新增任务的浮动按钮点击事件
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(
            onToggle = { vm.toggleCompleted(it) },
            onStart = { task -> startWithPermission(task) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.tasks.collect { list -> adapter.submitList(list) }
            }
        }


        binding.fabAdd.setOnClickListener {
            val titles = listOf("阅读文档", "写周报", "算法练习", "产品复盘")
            val idx = (System.currentTimeMillis() % titles.size).toInt()
            val priorities = listOf(Priority.Low, Priority.Medium, Priority.High)
            val pIdx = (System.currentTimeMillis() % priorities.size).toInt()
            vm.addOrUpdate(
                title = titles[idx],
                description = "",
                priority = priorities[pIdx],
                estimated = 1
            )
        }
    }

    /**
     * 销毁视图绑定以避免泄漏
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 启动指定任务并检查 Android 13+ 通知权限
     * 无权限时先申请，成功后继续启动计时会话
     */
    private fun startWithPermission(task: Task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                pendingStartTaskId = task.id
                notifReq.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        timerVm.setCurrentTask(task.id)
        timerVm.start(SessionType.Work)
    }
}

private class TaskAdapter(
    val onToggle: (Task) -> Unit,
    val onStart: (Task) -> Unit,
) : ListAdapter<Task, TaskViewHolder>(DIFF) {
    /**
     * 创建任务项的 ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }
    /**
     * 绑定列表项数据与点击事件
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position), onToggle, onStart)
    }

    companion object {
        /**
         * 定义任务列表的 Diff 规则
         */
        val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }
}

private class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 渲染任务项的标题、描述、优先级与统计，并绑定按钮点击
     */
    fun bind(t: Task, onToggle: (Task) -> Unit, onStart: (Task) -> Unit) {
        binding.tvTitle.text = t.title
        if (t.description.isNotEmpty()) {
            binding.tvDesc.text = t.description
            binding.tvDesc.visibility = View.VISIBLE
        } else {
            binding.tvDesc.visibility = View.GONE
        }
        binding.tvPriority.text = when (t.priority) { Priority.Low -> "低"; Priority.Medium -> "中"; Priority.High -> "高" }
        binding.tvStats.text = "今日完成：${t.completedCount} 个番茄钟"
        binding.btnToggle.text = if (t.isCompleted) "撤销完成" else "标记完成"
        binding.btnToggle.setOnClickListener { onToggle(t) }
        binding.btnStart.setOnClickListener { onStart(t) }
    }
}