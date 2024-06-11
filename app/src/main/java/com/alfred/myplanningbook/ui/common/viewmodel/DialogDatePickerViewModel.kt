package com.alfred.myplanningbook.ui.common.viewmodel;

import androidx.lifecycle.ViewModel
import com.alfred.myplanningbook.core.log.Klog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class DialogDatePickerUiState(
    val date: Long? = 0,
    val isPickerVisible: Boolean = false,
)

class DialogDatePickerViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(DialogDatePickerUiState())
    val uiState: StateFlow<DialogDatePickerUiState> = _uiState.asStateFlow()

    fun onStart(initialDate: Long) {
        updateDate(initialDate)
        updateIsPickerVisible(true)
    }

    fun onClosing() {
        Klog.line("DialogDatePickerViewModel", "onClosing", "onClosing")
        updateIsPickerVisible(false)
    }

    fun onConfirmationClick(date: Long?) {
        Klog.line("DialogDatePickerViewModel", "onConfirmationClick", "date")
        updateDate(date)
        updateIsPickerVisible(false)
    }

    private fun updateIsPickerVisible(makeVisible: Boolean) {
        _uiState.update {
            it.copy(isPickerVisible = makeVisible)
        }
    }

    private fun updateDate(theDate: Long?) {
        _uiState.update {
            it.copy(date = theDate)
        }
    }
}
