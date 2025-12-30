package com.example.swipeclean.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeclean.data.repository.ThemeRepository
import com.example.swipeclean.utils.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val repository: ThemeRepository) : ViewModel() {

    val themeMode = repository.themeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.LIGHT)

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            repository.saveTheme(mode)
        }
    }
}
