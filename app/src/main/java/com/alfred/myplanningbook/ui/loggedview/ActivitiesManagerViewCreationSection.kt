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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.DialogTimePickerView
import com.alfred.myplanningbook.ui.loggedview.viewmodel.ActivitiesManagerViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun activityFormSection() {
    activityFormActions()
    Spacer(modifier = Modifier.height(10.dp))
    activityDataFieldsComponents()
}

@Composable
private fun activityFormActions() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

    Column {
        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    viewModel.createActivity();
                }) {
                Text("Save")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    viewModel.showActivityFormSection(false);
                }) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun activityUpdateSection() {
    activityUpdateActions()
    Spacer(modifier = Modifier.height(10.dp))
    activityDataFieldsComponents()
}

@Composable
private fun activityUpdateActions() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

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
                        viewModel.updateActivity();
                    }) {
                    Text("Save")
                }

                OutlinedButton(
                    modifier = Modifier.width(200.dp).height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        viewModel.hideActivityUpdateSection();
                    }) {
                    Text("Cancel")
                }
            }
        }
        Row {
            Column(Modifier.fillMaxWidth(),
                Arrangement.Top,
                Alignment.CenterHorizontally) {

                OutlinedButton(
                    modifier = Modifier.width(200.dp).height(70.dp),
                    colors = CommonViewComp.getActionsButtonColour(),
                    onClick = {
                        viewModel.cloneActivity();
                    }) {
                    Text("Clone with changes")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun activityDataFieldsComponents() {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            activityFormComponents_activityName()
            activityFormComponents_activityDesc()
            activityFormComponents_daysWeek()
            activityFormComponents_Timepicker_START()
            activityFormComponents_Timepicker_END()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun activityFormComponents_activityName() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = uiState.activityName,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateActivityName(it) },
                placeholder = { Text("Enter Activity Name (5-30)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.activityNameError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.activityNameErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun activityFormComponents_activityDesc() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = uiState.activityDesc,
                onValueChange = { viewModel.updateActivityDesc(it) },
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
                placeholder = { Text("Enter Activity Description (5-100)") },
                singleLine = false,
                maxLines = 3
            )
            if(uiState.activityDescError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.activityDescErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun activityFormComponents_daysWeek() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            Row {
                Column(
                    Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    dayChip("Lunes", DateTimeUtils.LUNES)
                    dayChip("Jueves", DateTimeUtils.JUEVES)
                    dayChip("Domingo", DateTimeUtils.DOMINGO)
                }
                Column(
                    Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    dayChip("Martes", DateTimeUtils.MARTES)
                    dayChip("Viernes", DateTimeUtils.VIERNES)
                }
                Column(
                    Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    dayChip("Miercoles", DateTimeUtils.MIERCOLES)
                    dayChip("Sábado", DateTimeUtils.SABADO)
                }
            }//Row

            if(uiState.activityChipsError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.activityChipsErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }//Column
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
private fun dayChip(name: String, code: String) {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selected by remember { mutableStateOf(false) }
    selected = uiState.chipsSelectedMap[code]!!

    FilterChip(
        selected = uiState.chipsSelectedMap[code]!!,// selected,
        onClick = {
            selected = !uiState.chipsSelectedMap[code]!!
            viewModel.selectChip(code, selected)
        },
        label = { Text(name) },
        modifier = Modifier,
        colors = when (selected) {
            true -> FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF6666aa),
                        labelColor = Color.White,
                        selectedContainerColor = Color(0xFF6666aa),
                        selectedLabelColor = Color.White
            )
            false ->FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF7bb661),
                        labelColor = Color.White,
            )
        },
        leadingIcon = {
            when (selected) {
                true -> Icon(imageVector = Icons.Filled.Done, contentDescription = name, Modifier.size(FilterChipDefaults.IconSize))
                false ->  Icon(imageVector = Icons.Filled.Home, contentDescription = "no selected", Modifier.size(FilterChipDefaults.IconSize))
            }
        },
    )//Chip
    Spacer(Modifier.width(20.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun activityFormComponents_Timepicker_START() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogPicker = DialogTimePickerView(
        onClose = {
            viewModel.closeTimeStartDi()
        },
        onTimeSelected = { hour: Int, min: Int ->
            viewModel.onTimeStartSelected(hour, min)
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
            Text("Activity start time")

            OutlinedTextField(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                value = uiState.activityStartTimeFormatted,
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.openTimeStartDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Schedule, contentDescription = null)
                    }
                }
            )

            if(uiState.activityStartTimeError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.activityStartTimeErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    }

    if(uiState.openTimeStartDialog) {
        dialogPicker.openView(uiState.activityStartHour, uiState.activityStartMinute)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun activityFormComponents_Timepicker_END() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var dialogPicker = DialogTimePickerView(
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
            Text("Activity end time")

            OutlinedTextField(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.8f)
                    .padding(10.dp),
                value = uiState.activityEndTimeFormatted,
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

            if(uiState.activityEndTimeError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.activityEndTimeErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    }

    if(uiState.openTimeEndDialog) {
        dialogPicker.openView(uiState.activityEndHour, uiState.activityEndMinute)
    }
}

