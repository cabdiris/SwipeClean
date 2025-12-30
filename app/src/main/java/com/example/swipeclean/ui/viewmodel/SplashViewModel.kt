package com.example.swipeclean.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeclean.data.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: OnboardingRepository) : ViewModel() {

    private val _onBoardingCompleted = MutableStateFlow(false)
    val onBoardingCompleted: StateFlow<Boolean> = _onBoardingCompleted

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                _onBoardingCompleted.value = completed
            }
        }
    }

    fun completeOnBoarding() {
        viewModelScope.launch {
            repository.saveOnBoardingState(true)
        }
    }
}