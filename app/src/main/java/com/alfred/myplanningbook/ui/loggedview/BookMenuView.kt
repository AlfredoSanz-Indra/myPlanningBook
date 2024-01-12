package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.BookMenuViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class BookMenuView {

    @Composable
    fun createView(onPlanningBooks: () -> Unit,
                   onTasks: () -> Unit,
                   onActivities: () -> Unit,
                   onLogout: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.loadState()
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
                    errorGeneralField()
                    loading()
                }
                else {
                    Spacer(modifier = Modifier.height(30.dp))
                    errorGeneralField()
                    planningBookNameText()

                    Spacer(modifier = Modifier.height(30.dp))
                    planningBooksButton(onPlanningBooks)

                    Spacer(modifier = Modifier.height(30.dp))
                    tasksButton(onTasks)

                    Spacer(modifier = Modifier.height(30.dp))
                    activitiesButton(onActivities)

                    Spacer(modifier = Modifier.height(30.dp))
                    logoutButton(onLogout)
                }
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: BookMenuViewModel = koinViewModel()
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
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
    private fun planningBookNameText() {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Text(
            uiState.currentPlanningBook,
            color = Color.Blue,
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Red
            )
        )
    }

    @Composable
    private fun planningBooksButton(onLogout: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "planningBooksButton", "planningBooks button clicked")
                viewModel.planningbookView();
            }
        ) {
            Text("Manage Planning Book")
        }

        if(uiState.planningbookAction) {
            onLogout()
            viewModel.resetActions()
        }
    }

    @Composable
    private fun tasksButton(onTasks: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "tasksButton", "tasks button clicked")
                viewModel.tasksView();
            }
        ) {
            Text("Tasks")
        }

        if(uiState.tasksAction) {
            onTasks()
            viewModel.resetActions()
        }
    }

    @Composable
    private fun activitiesButton(onActivities: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "activitiesButton", "activities button clicked")
                viewModel.activitiesView();
            }
        ) {
            Text("Activities")
        }

        if(uiState.activitiesAction) {
            onActivities()
            viewModel.resetActions()
        }
    }
    @Composable
    private fun logoutButton(onLogout: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "logoutButton", "logout clicked")
                val r = viewModel.logoutUser();
                if(r) {
                    onLogout()
                }
            }
        ) {
            Text("Log Out")
        }
    }
}