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
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerUiState
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookManagerView {


    @Composable
    fun createView(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.currentPlanningBook) {
            viewModel.loadPlanningBooks()
            Klog.line("PlanningBookManagerView", "createView", "currentPlanningBook:  ${uiState.currentPlanningBook}")
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
                    headerMessageOptional(uiState)

                    Spacer(modifier = Modifier.height(10.dp))
                    PBHeaderSection(onBack)

                    Spacer(modifier = Modifier.height(10.dp))
                    pbListSection()
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
    private fun headerMessageOptional(uiState: PlanningBookManagerUiState) {
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
    }

    @Composable
    private fun PBHeaderSection(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToCreatePB) {
            pbCreationSection()
        }
        else {
            pbCreationActions(onBack)
        }
    }

    @Composable
    private fun pbCreationActions(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()

        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    Klog.line("PlanningBookManagerView","headerActions","create Planning Book button clicked")
                    viewModel.showPBCreationSection(true);
                }
            ) {
                Text("Create Planning Book")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    Klog.line("PlanningBookManagerView", "headerActions", "back Button clicked")
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }

    @Composable
    private fun pbCreationSection() {

        pbCreationActions()
        Spacer(modifier = Modifier.height(10.dp))
        pbCreationComponents()
    }

    @Composable
    private fun pbCreationActions() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column {
            Row {
                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getActionsButtonColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","PBCreationActions", "create Planning Book button clicked")
                        viewModel.createPlanningBook();
                    }) {
                    Text("Create")
                }

                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","PBCreationActions","cancel create Planning Book button clicked")
                        viewModel.showPBCreationSection(false);
                    }) {
                    Text("Cancel")
                }
            }
        }
    }

    @Composable
    private fun pbCreationComponents() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Row {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.pbName,
                    onValueChange = { viewModel.updatePBName(it) },
                    placeholder = { Text("Enter Planning Book Name") }
                )
                if (uiState.pbNameError) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        uiState.pbNameErrorText,
                        color = Color.Red,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Red
                        )
                    )
                }
            }
        }

    }

    @Composable
    private fun pbListSection() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
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
                    .height(350.dp)
                    .padding(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(),
                    )
                    {
                        Column {
                            Row {
                                Column(
                                    Modifier
                                        .padding(4.dp)
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
                                    Modifier
                                        .padding(4.dp)
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
                                    Modifier
                                        .padding(8.dp)
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
    }





}