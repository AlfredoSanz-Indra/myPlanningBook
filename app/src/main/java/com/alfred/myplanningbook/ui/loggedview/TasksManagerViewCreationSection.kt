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
fun taskCreationSection() {
    taskCreationActions()
    Spacer(modifier = Modifier.height(10.dp))
    taskDataFieldsComponents()
}

@Composable
private fun taskCreationActions() {
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
fun taskUpdateSection() {
    taskUpdateActions()
    Spacer(modifier = Modifier.height(10.dp))
    taskDataFieldsComponents()
}

@Composable
private fun taskUpdateActions() {
    val viewModel: TasksManagerViewModel = koinViewModel()

    Column {
        Column(Modifier.fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally) {

            Row {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun taskDataFieldsComponents() {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            taskCreationComponents_taskName()
            taskCreationComponents_taskDesc()
            taskCreationComponents_Datepicker()
            taskCreationComponents_Timepicker()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun taskCreationComponents_taskName() {
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
private fun taskCreationComponents_taskDesc() {
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
private fun taskCreationComponents_Datepicker() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogPicker = DialogDatePickerView(
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
private fun taskCreationComponents_Timepicker() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogPicker = DialogTimePickerView(
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
            Text("Task time")

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
