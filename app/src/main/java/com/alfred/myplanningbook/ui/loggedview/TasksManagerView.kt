package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.DialogDatePickerView
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TaskManagerUiState
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TasksManagerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class TasksManagerView {
    @Composable
    fun createView(onBack: () -> Unit) {

        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.currentPlanningBook) {
            viewModel.loadTasks()
            Klog.line("TasksManagerView", "createView", "currentPlanningBook:  ${uiState.currentPlanningBook}")
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                if(uiState.currentPlanningBook.isEmpty()) {
                    Spacer(modifier = Modifier.height(30.dp))
                    loading()
                }
                else {
                    Spacer(modifier = Modifier.height(30.dp))
                    errorGeneralField()
                    headerTitlePB(uiState)

                    Spacer(modifier = Modifier.height(30.dp))
                    tasksHeaderSection(onBack)
                }
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.generalError) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                uiState.generalErrorText,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    private fun loading() {

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
            }
        ) {
            Text("Loading State!! Please Wait")
        }
    }

    @Composable
    private fun headerTitlePB(uiState: TaskManagerUiState) {
        if(uiState.currentPlanningBook.isNotEmpty()) {
            Text(
                uiState.currentPlanningBook,
                color = CommonViewComp.c_card_buttonOneContent,
                style = TextStyle(
                    fontSize = 20.sp,
                    background = CommonViewComp.c_snow
                )
            )
        }
    }

    @Composable
    private fun tasksHeaderSection(onBack: () -> Unit) {

        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToCreateTask) {
            taskCreationSection()
        }
        else {
            tasksHeaderActions(onBack)
        }
    }

    @Composable
    private fun tasksHeaderActions(onBack: () -> Unit) {

        val viewModel: TasksManagerViewModel = koinViewModel()

        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    Klog.line("TasksManagerView","tasksMainControlsSection","show create Task button clicked")
                    viewModel.showTaskCreationSection(true);
                }
            ) {
                Text("Create Task")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    Klog.line("TasksManagerView", "tasksMainControlsSection", "back Button clicked")
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }

    @Composable
    private fun taskCreationSection() {

        taskCreationActions()
        Spacer(modifier = Modifier.height(10.dp))
        taskCreationComponents()
    }

    @Composable
    private fun taskCreationActions() {

        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column {
            Row {
                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getActionsButtonColour(),
                    onClick = {
                        Klog.line("TasksManagerView","taskCreationActions", "create Task button clicked")
                        viewModel.createTask();
                    }) {
                    Text("Create")
                }

                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        Klog.line("TasksManagerView","taskCreationActions","cancel create Task button clicked")
                        viewModel.showTaskCreationSection(false);
                    }) {
                    Text("Cancel")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun taskCreationComponents() {
        Column {
            taskCreationComponents_taskName()
            taskCreationComponents_taskDesc()
            taskCreationComponents_Datepicker()
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
                    placeholder = { Text("Enter Task Description (0-100)") },
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

        var ddpv = DialogDatePickerView(
            onClose = {
                viewModel.closeCalendarDi()
            },
            onDateSelected = {
                viewModel.onDateSelected(it)
            }
        )

        Row {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.surface)
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
            ddpv.openView(uiState.taskDate)
        }
    }
}