package com.alfred.myplanningbook.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class DialogTimePickerUiState(
    val hour: Int = 0,
    val minute: Int = 0,
    val is24Hour: Boolean = true
)

class DialogTimePickerViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(DialogTimePickerUiState())
    val uiState: StateFlow<DialogTimePickerUiState> = _uiState.asStateFlow()

    fun onStart(initialH: Int, initialM: Int) {
        updateHour(initialH)
        updateMinute(initialM)
    }

    fun onConfirm(hour: Int, minute: Int) {
        updateHour(hour)
        updateMinute(minute)
    }

    private fun updateHour(theHour: Int) {
        _uiState.update {
            it.copy(hour = theHour)
        }
    }

    private fun updateMinute(theMinute: Int) {
        _uiState.update {
            it.copy(minute = theMinute)
        }
    }
}