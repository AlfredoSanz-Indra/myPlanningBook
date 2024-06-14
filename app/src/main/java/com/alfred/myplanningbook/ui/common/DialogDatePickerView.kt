package com.alfred.myplanningbook.ui.common

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.viewmodel.DialogDatePickerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class DialogDatePickerView(onClose: () -> Unit,
                           onDateSelected: (Long) -> Unit) {

    val onClose = onClose
    val onDateSelected = onDateSelected

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun openView(initialDate: Long) {
        val viewModel: DialogDatePickerViewModel = koinViewModel()
        viewModel.onStart(initialDate)

        calendarSection()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun calendarSection() {
        val viewModel: DialogDatePickerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val internalState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.date
        )

        DatePickerDialog(
            onDismissRequest = {
                viewModel.onClosing()
                onClose()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onConfirmationClick(internalState.selectedDateMillis)
                        onDateSelected(internalState.selectedDateMillis ?: 1)
                    },
                    enabled = internalState.selectedDateMillis != null
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onClosing()
                        onClose()
                    }
                ) {
                    Text("Cancelar")
                }
            },
            content = { DatePicker(state = internalState) },
        )
    }
}