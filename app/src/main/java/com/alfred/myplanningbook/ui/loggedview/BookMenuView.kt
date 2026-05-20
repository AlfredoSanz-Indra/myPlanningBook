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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.alfred.myplanningbook.ui.common.MenuButtonComponent
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
                   onVehicles: () -> Unit,
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
                    Content(onPlanningBooks, onTasks, onActivities, onLibrary, onVehicles, onLogout, innerPadding)
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
                    MenuButtonComponent.show("Back",
                                             CommonViewComp.getSecondaryButtonColour(),
                                             onClick = {
                                                 onLogout()
                                             })
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
                        onVehicles: () -> Unit,
                        onLogout: () -> Unit,
                        innerPadding: PaddingValues) {
        Column(
            Modifier.background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
            Arrangement.Top,
            Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            ErrorGeneralField()
            PlanningBookNameText()

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Manage Planning Book",
                CommonViewComp.getActionsButtonColour(),
                onClick = {
                    onPlanningBooks()
                })

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Tasks",
                                     CommonViewComp.getActionsButtonColour(),
                                     onClick = {
                                         onTasks()
                                     })

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Activities",
                                     CommonViewComp.getActionsButtonColour(),
                                     onClick = {
                                         onActivities()
                                     })

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Library",
                                     CommonViewComp.getMenuLibraryButtonColour(),
                                     onClick = {
                                         onLibrary()
                                     })

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Vehicles",
                CommonViewComp.getMenuLibraryButtonColour(),
                onClick = {
                    onVehicles()
                })

            Spacer(modifier = Modifier.height(30.dp).padding(innerPadding))
            MenuButtonComponent.show("Log Out",
                CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    onLogout()
                })
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
                    text = "version 1.6.7", color = CommonViewComp.c_card_buttonOneContent, style = TextStyle(
                        fontSize = 15.sp, background = CommonViewComp.c_snow
                    )
                )
            }
        }
    }
}