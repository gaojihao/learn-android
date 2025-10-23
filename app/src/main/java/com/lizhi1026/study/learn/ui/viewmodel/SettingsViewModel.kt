/**
 * 文件：SettingsViewModel.kt
 * 简介：设置 ViewModel
 * 关键职责：管理偏好设置状态与更新
 * 相关组件：SettingsDataStore、SettingsFragment
 */
package com.lizhi1026.study.learn.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhi1026.study.learn.data.settings.SettingsDataStore
import com.lizhi1026.study.learn.domain.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val store: SettingsDataStore,
) : ViewModel() {
    /** 公开的设置状态流（始终提供最新的偏好设置） */
    val settings: StateFlow<Settings> = store.observe().stateIn(viewModelScope, SharingStarted.Eagerly, Settings())

    /**
     * 更新设置
     * @param block 基于当前设置返回新值的转换函数
     */
    fun update(block: (Settings) -> Settings) { viewModelScope.launch { store.update(block) } }

    /** 恢复默认设置（重置为初始配置） */
    fun restoreDefaults() {
        viewModelScope.launch {
            store.update { Settings() }
        }
    }
}