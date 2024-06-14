package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.viewmodel.DialogTimePickerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class DialogTimePickerView(
    onClose: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {

    val onClose = onClose
    val onTimeSelected = onTimeSelected

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun openView(initialHour: Int, initialMin: Int) {
        val viewModel: DialogTimePickerViewModel = koinViewModel()
        viewModel.onStart(initialHour, initialMin)

        timepickerSection()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun timepickerSection() {
        val viewModel: DialogTimePickerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val internalState = rememberTimePickerState(
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            is24Hour = uiState.is24Hour
        )
        var mode: DisplayMode by remember { mutableStateOf(DisplayMode.Picker) }

        PickerDialog(
            onDismissRequest = onClose,
            title = { Text("Select Time") },
            buttons = {
                DisplayModeToggleButton(
                    displayMode = mode,
                    onDisplayModeChange = { mode = it },
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onClose) {
                    Text("Cancel")
                }
                TextButton(
                    onClick = {
                        viewModel.onConfirm(internalState.hour ?: 0, internalState.minute ?: 0)
                        onTimeSelected(internalState.hour ?: 0, internalState.minute ?: 0)
                    }
                ) {
                    Text("Accept")
                }
            },
        ) {
            val contentModifier = Modifier.padding(horizontal = 24.dp)
            when (mode) {
                DisplayMode.Picker -> TimePicker(modifier = contentModifier, state = internalState)
                DisplayMode.Input -> TimeInput(modifier = contentModifier, state = internalState)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DisplayModeToggleButton(displayMode: DisplayMode,
                                        onDisplayModeChange: (DisplayMode) -> Unit) {

        when (displayMode) {
            DisplayMode.Picker -> IconButton(
                onClick = {
                    onDisplayModeChange(DisplayMode.Input)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Keyboard,
                    contentDescription = "Keyboard",
                )
            }
            DisplayMode.Input -> IconButton(
                onClick = {
                    onDisplayModeChange(DisplayMode.Picker)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = "Clock",
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PickerDialog(
        onDismissRequest: () -> Unit,
        title: @Composable () -> Unit,
        buttons: @Composable RowScope.() -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        BasicAlertDialog(
            modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min),
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { // Title
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                            Box(
                                modifier = Modifier.align(Alignment.Start).padding(horizontal = 24.dp).padding(top = 16.dp, bottom = 20.dp),
                            ) {
                                title()
                            }
                        }
                    } // Content
                    CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                        content()
                    } // Buttons
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                        ProvideTextStyle(MaterialTheme.typography.labelLarge) { // TODO This should wrap on small screens, but we can't use AlertDialogFlowRow as it is no public
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, end = 6.dp, start = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                            ) {
                                buttons()
                            }
                        }
                    }
                }
            }
        }
    }
}