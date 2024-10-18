package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
    fun CreateView(onPlanningBooks: () -> Unit,
                   onTasks: () -> Unit,
                   onActivities: () -> Unit,
                   onLibrary: () -> Unit,
                   onLogout: () -> Unit) {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.isStateLoaded) {
            if(!uiState.isStateLoaded) {
                viewModel.loadState()
            }
            else {
                viewModel.updateState()
            }
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Scaffold(
                modifier = Modifier,
                topBar = {
                    Topbar(onLogout)
                },
                bottomBar = {
                    Bottombar()
                },
                content = { innerPadding ->
                    Content(onPlanningBooks, onTasks, onActivities, onLibrary, onLogout, innerPadding)
                }
            )
        }
    }

    @Composable
    private fun Topbar(onLogout: () -> Unit) {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .height(50.dp),
            Arrangement.Top,
            Alignment.CenterHorizontally)
        {
            if(uiState.currentPlanningBook.isEmpty()) {
                Spacer(modifier = Modifier.height(30.dp))
                ErrorGeneralField()
                if(!uiState.showBack) {
                    Loading()
                }
                else {
                    BackButton(onLogout)
                }
            }
            else {
                Spacer(modifier = Modifier.height(30.dp))
                ErrorGeneralField()
                PlanningBookNameText()
            }
        }
    }

    @Composable
    private fun Bottombar() {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .height(70.dp),
            Arrangement.Top,
            Alignment.CenterHorizontally)
        {
            LegendOnFoot()
        }
    }

    @Composable
    private fun Content(onPlanningBooks: () -> Unit,
                        onTasks: () -> Unit,
                        onActivities: () -> Unit,
                        onLibrary: () -> Unit,
                        onLogout: () -> Unit,
                        innerPadding: PaddingValues) {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.Top,
            Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            ErrorGeneralField()
            PlanningBookNameText()

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            PlanningBooksButton(onPlanningBooks)

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            TasksButton(onTasks)

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            ActivitiesButton(onActivities)

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            LibraryButton(onLibrary)

            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(innerPadding))
            LogoutButton(onLogout)
        }
    }

    @Composable
    private fun ErrorGeneralField() {
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
    private fun Loading() {
        CircularProgressIndicator()
    }

    @Composable
    private fun PlanningBookNameText() {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Text(
            uiState.currentPlanningBook,
            color = CommonViewComp.c_card_buttonOneContent,
            style = TextStyle(
                fontSize = 20.sp,
                background = CommonViewComp.c_snow
            )
        )
    }

    @Composable
    private fun PlanningBooksButton(onPlanningBooks: () -> Unit) {
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

        LaunchedEffect(uiState.isToPlanningBookManager) {
            if(uiState.isToPlanningBookManager) {
                viewModel.clearFields()
                onPlanningBooks()
            }
        }
    }

    @Composable
    private fun TasksButton(onTasks: () -> Unit) {
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

        LaunchedEffect(uiState.isToTasksManager) {
            if(uiState.isToTasksManager) {
                viewModel.clearFields()
                onTasks()
            }
        }
    }

    @Composable
    private fun ActivitiesButton(onActivities: () -> Unit) {
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

        LaunchedEffect(uiState.isToActivitiesManager) {
            if(uiState.isToActivitiesManager) {
                viewModel.clearFields()
                onActivities()
            }
        }
    }

    @Composable
    private fun LibraryButton(onLibrary: () -> Unit) {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getMenuLibraryButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "LibraryButton", "library button clicked")
                viewModel.libraryView();
            }
        ) {
            Text("Library")
        }

        LaunchedEffect(uiState.isToLibrary) {
            if(uiState.isToLibrary) {
                viewModel.clearFields()
                onLibrary()
            }
        }
    }

    @Composable
    private fun LogoutButton(onLogout: () -> Unit) {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "logoutButton", "logout clicked")
                viewModel.logoutUser()
            }
        ) {
            Text("Log Out")
        }

        LaunchedEffect(uiState.isToLogout) {
            if(uiState.isToLogout) {
                viewModel.clearFields()
                onLogout()
            }
        }
    }

    @Composable
    private fun BackButton(onLogout: () -> Unit) {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                Klog.line("BookMenuView", "logoutButton", "back clicked")
                viewModel.doBack()
            }
        ) {
            Text("Back")
        }

        LaunchedEffect(uiState.isToBack) {
            if(uiState.isToBack) {
                viewModel.clearFields()
                onLogout()
            }
        }
    }

    @Composable
    private fun LegendOnFoot() {
        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Klog.line("BookMenuView", "legendOnFoot", "uiState.loggedUser: ${uiState.loggedUser}")
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(Modifier.padding(4.dp))
            {
                Text(
                    text = uiState.loggedUser,
                    color = CommonViewComp.c_card_buttonOneContent,
                    style = TextStyle(
                        fontSize = 20.sp,
                        background = CommonViewComp.c_snow
                    )
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(Modifier.padding(4.dp)) {
                Text(
                    text = "version 1.5.7", color = CommonViewComp.c_card_buttonOneContent, style = TextStyle(
                        fontSize = 15.sp, background = CommonViewComp.c_snow
                    )
                )
            }
        }
    }
}