package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.BookMenuViewModel
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookManagerView {




    @Composable
    fun createView(onCreatePlanning: () -> Unit
                   ) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.loadPlanningBooks()
            Klog.line("PlanningBookManagerView", "createView", "currentPlanningBook:  ${uiState.currentPlanningBook}")
            Klog.line("PlanningBookManagerView", "createView", "planningBookList:  ${uiState.planningBookList}")
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
                    currentPlanningBookActive()
                    createPBookButton()

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
        val viewModel: PlanningBookManagerViewModel = koinViewModel()
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
    private fun currentPlanningBookActive() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.planningBookList.isEmpty()) {
            Text(
                uiState.currentPlanningBook,
                color = Color.Blue,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )
        }
        else {
            Column(
                Modifier.padding(4.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(15.dp)
                        .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(15.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                            .padding(horizontal = 10.dp)
                    ) {
                        Card(
                            modifier = Modifier.padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(),
                        )
                        {
                            Column {
                                Row {
                                    Column(
                                        Modifier.padding(4.dp)
                                            .width(550.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = uiState.currentPlanningBook,
                                            style = MaterialTheme.typography.headlineLarge,
                                            color = MaterialTheme.colorScheme.onSecondary,
                                        )
                                    }
                                    Column(
                                        Modifier.padding(4.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = "row 1 c2",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }//Row

                                Row {
                                    Column(
                                        Modifier.padding(8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "row 2 c1",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSecondary,
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = "row 2 c1 txt2",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondary,
                                        )
                                    }
                                }//Row
                            }//Column
                        }//card
                    }//lazyColumn
                }//Box
            }
        }//isNotEmpty
    }

    @Composable
    private fun createPBookButton() {
        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToCreatePB) {
            Column {
                Row {
                    OutlinedButton(modifier = Modifier
                        .width(200.dp)
                        .height(70.dp),
                        colors = CommonViewComp.getActionsButtonColour(),
                        onClick = {
                            Klog.line("PlanningBookManagerView","createPBookButton","create Planning Book button clicked")
                            viewModel.createPlanningBook();
                        }
                    ) {
                        Text("Create")
                    }

                    OutlinedButton(modifier = Modifier
                        .width(200.dp)
                        .height(70.dp),
                        colors = CommonViewComp.getSecondaryButtonColour(),
                        onClick = {
                            Klog.line("PlanningBookManagerView","createPBookButton","cancel create Planning Book button clicked")
                            viewModel.cancelCreatePlanningBookButtonClicked();
                        }
                    ) {
                        Text("Cancel")
                    }
                }

                Row {
                    OutlinedTextField(
                        value = uiState.pbName,
                        onValueChange = { viewModel.updatePBName(it) },
                        placeholder = { Text("Enter Planning Book Name") }
                    )
                }
            }
        }
        else {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    Klog.line(
                        "PlanningBookManagerView",
                        "createPBookButton",
                        "create Planning Book button clicked"
                    )
                    viewModel.createPlanningBookButtonClicked();
                }
            ) {
                Text("Create Planning Book")
            }
        }
    }

}