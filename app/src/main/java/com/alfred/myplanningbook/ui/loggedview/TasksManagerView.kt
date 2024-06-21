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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.DialogDatePickerView
import com.alfred.myplanningbook.ui.common.DialogTimePickerView
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

        LaunchedEffect(uiState.isTaskBookListLoaded) {
            if(!uiState.isTaskBookListLoaded) {
                viewModel.loadTasks()
            }
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
            taskListSection()
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
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }

    @Composable
    private fun taskListSection() {
        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    //@see https://developer.android.com/codelabs/basic-android-compose-training-add-scrollable-list?hl=es-419#2
                    items( uiState.taskBookList.size, itemContent = { item ->
                        val taskBook = uiState.taskBookList[item]
                        taskListCardComponent(taskBook)
                    })
                }//lazy
            } //Box
        }
    }

    @Composable
    private fun taskListCardComponent(taskBook: TaskBook) {
        OutlinedCard(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .height(140.dp)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            colors = CommonViewComp.gePlanningBookCardColour(),
            elevation = CardDefaults.outlinedCardElevation(),
            border = CardDefaults.outlinedCardBorder(),
        )
        {
            Column(Modifier.padding(4.dp)
                .fillMaxWidth(),
                //.width(550.dp),
                horizontalAlignment = Alignment.Start)
            {
                taskListCardComponentRowName(taskBook)

                Spacer(modifier = Modifier.height(10.dp))
                taskListCardComponentRowDesc(taskBook)

                Spacer(modifier = Modifier.height(10.dp))
                taskListCardComponentRowDate(taskBook)
            }//Column
        } //card
    }

    @Composable
    private fun taskListCardComponentRowName(taskBook: TaskBook) {
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(Modifier.padding(4.dp))
            {
                Text(
                    text = taskBook.name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }

    @Composable
    private fun taskListCardComponentRowDesc(taskBook: TaskBook) {
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(Modifier.padding(4.dp))
            {
                Text(
                    text = taskBook.description ?: "-",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }

    @Composable
    private fun taskListCardComponentRowDate(taskBook: TaskBook) {
        Row (Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(Modifier.padding(4.dp))
            {
                Text(
                    text = " ${taskBook.day}/${taskBook.month}/${taskBook.year} - ${taskBook.hour}:${taskBook.minute}",
                    style = MaterialTheme.typography.titleSmall,
                )
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun taskCreationComponents() {
        Column {
            taskCreationComponents_taskName()
            taskCreationComponents_taskDesc()
            taskCreationComponents_Datepicker()
            taskCreationComponents_Timepicker()
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
                Modifier.background(color = MaterialTheme.colorScheme.surface)
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
}