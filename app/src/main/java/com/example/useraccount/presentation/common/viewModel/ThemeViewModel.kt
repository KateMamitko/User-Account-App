package com.example.useraccount.presentation.common.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {

    private val _isDark = MutableStateFlow(false)
    val isDark = _isDark.asStateFlow()

    fun initTheme(systemTheme: Boolean) {
        _isDark.value = systemTheme
    }

    fun toggleTheme(newValue: Boolean) {
        _isDark.value = newValue
    }
}