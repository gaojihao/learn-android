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
    val settings: StateFlow<Settings> = store.observe().stateIn(viewModelScope, SharingStarted.Eagerly, Settings())

    fun update(block: (Settings) -> Settings) { viewModelScope.launch { store.update(block) } }

    fun restoreDefaults() {
        viewModelScope.launch {
            store.update { Settings() }
        }
    }
}