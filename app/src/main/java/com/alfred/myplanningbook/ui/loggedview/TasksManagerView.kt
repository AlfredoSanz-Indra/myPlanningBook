package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.CommonViewComp
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
                    TasksBodySection(onBack)
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
    private fun TasksBodySection(onBack: () -> Unit) {
        val viewModel: TasksManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToCreateTask) {
            TaskCreationSection()
        }
        else if(uiState.isToUpdateTask) {
            TaskUpdateSection()
        }
        else {
            tasksHeaderActions(onBack)
            TaskListSection()
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
}