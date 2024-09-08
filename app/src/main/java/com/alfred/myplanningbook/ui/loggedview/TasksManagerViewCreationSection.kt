package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.domain.model.TaskBookNatureEnum
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.DialogDatePickerView
import com.alfred.myplanningbook.ui.common.DialogTimePickerView
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TasksManagerViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun TaskCreationSection() {
    TaskCreationActions()
    Spacer(modifier = Modifier.height(10.dp))
    TaskDataFieldsComponents()
}

@Composable
private fun TaskCreationActions() {
    val viewModel: TasksManagerViewModel = koinViewModel()

    Column {
        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    viewModel.createTask();
                }) {
                Text("Save")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    viewModel.showTaskCreationSection(false);
                }) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun TaskUpdateSection() {
    TaskUpdateActions()
    Spacer(modifier = Modifier.height(10.dp))
    TaskDataFieldsComponents()
}

@Composable
private fun TaskUpdateActions() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        Column(Modifier.fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally) {

            Row {
                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getActionsButtonColour(),
                    onClick = {
                        viewModel.updateTask();
                    }) {
                    Text("Save")
                }

                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        viewModel.hideTaskUpdateSection();
                    }) {
                    Text("Cancel")
                }
            }
        }
        if(uiState.taskNature != TaskBookNatureEnum.IS_ACTIVITY) {
            Column(Modifier.fillMaxWidth(),
                Arrangement.Top,
                Alignment.CenterHorizontally) {

                Row {
                    OutlinedButton(
                        modifier = Modifier.width(200.dp).height(70.dp),
                        colors = CommonViewComp.getActionsButtonColour(),
                        onClick = {
                            viewModel.cloneTask();
                        }) {
                        Text("Clone with changes")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDataFieldsComponents() {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            TaskCreationComponents_taskName()
            TaskCreationComponents_taskDesc()
            TaskCreationComponents_Datepicker()
            TaskCreationComponents_Timepicker_START()
            TaskCreationComponents_Timepicker_END()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationComponents_taskName() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = uiState.taskName,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                enabled = when(uiState.taskNature) {
                    TaskBookNatureEnum.IS_ACTIVITY -> false
                    TaskBookNatureEnum.ORIGIN_ACTIVITY -> false
                    else -> true
                },
                readOnly = when(uiState.taskNature) {
                    TaskBookNatureEnum.IS_ACTIVITY -> true
                    TaskBookNatureEnum.ORIGIN_ACTIVITY -> true
                    else -> false
                },
                onValueChange = { viewModel.updateTaskName(it) },
                placeholder = { Text("Enter Task Name (5-30)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.taskNameError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.taskNameErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationComponents_taskDesc() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = uiState.taskDesc,
                onValueChange = { viewModel.updateTaskDesc(it) },
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
                placeholder = { Text("Enter Task Description (5-100)") },
                singleLine = false,
                maxLines = 3
            )
            if(uiState.taskDescError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.taskDescErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationComponents_Datepicker() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dialogPicker = DialogDatePickerView(
        onClose = {
            viewModel.closeCalendarDi()
        },
        onDateSelected = {
            viewModel.onDateSelected(it)
        }
    )

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            Text("Task Date")

            OutlinedTextField(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                value = uiState.taskDateFormatted,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.openCalendarDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null)
                    }
                }
            )

            if(uiState.taskDateError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.taskDateErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    }

    if(uiState.openCalendarDialog) {
        dialogPicker.openView(uiState.taskDate)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationComponents_Timepicker_START() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dialogPicker = DialogTimePickerView(
        onClose = {
            viewModel.closeTimeDi()
        },
        onTimeSelected = { hour: Int, min: Int ->
            viewModel.onTimeSelected(hour, min)
        }
    )

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            Text("Task start time")

            OutlinedTextField(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                value = uiState.taskTimeFormatted,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.openTimeDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Schedule, contentDescription = null)
                    }
                }
            )

            if(uiState.taskTimeError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.taskTimeErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    }

    if(uiState.openTimeDialog) {
        dialogPicker.openView(uiState.taskHour, uiState.taskMinute)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationComponents_Timepicker_END() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dialogPicker = DialogTimePickerView(
        onClose = {
            viewModel.closeTimeEndDi()
        },
        onTimeSelected = { hour: Int, min: Int ->
            viewModel.onTimeEndSelected(hour, min)
        }
    )

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            Text("Task end time")

            OutlinedTextField(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                value = uiState.taskTimeEndFormatted,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.openTimeEndDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Schedule, contentDescription = null)
                    }
                }
            )

            if(uiState.taskTimeEndError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.taskTimeEndErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    }

    if(uiState.openTimeEndDialog) {
        dialogPicker.openView(uiState.taskEndHour, uiState.taskEndMinute)
    }
}
