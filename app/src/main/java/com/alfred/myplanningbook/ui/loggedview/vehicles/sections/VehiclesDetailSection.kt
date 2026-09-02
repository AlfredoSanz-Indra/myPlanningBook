package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.ActionButtonComponent
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.TextBigFieldComponent
import com.alfred.myplanningbook.ui.common.TextFieldComponent
import com.alfred.myplanningbook.ui.common.TextWithDatePickerComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
@Composable
fun VehicleAddSection() {
    VehicleAddActions()
    Spacer(modifier = Modifier.height(10.dp))
    VehicleAddForm()
}

@Composable
private fun VehicleAddActions() {
    val viewModel: VehiclesViewModel = koinViewModel()

    Row(
        Modifier.background(color = Color(0xFFf7f6ff))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(20.dp))
        ActionButtonComponent.show(
            "New",
            CommonViewComp.getActionsButtonColour(),
            onClick = {
                viewModel.saveNewVehicle()
            })
        Spacer(Modifier.width(5.dp))
        ActionButtonComponent.show(
            "Back",
            CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                viewModel.showNewVehicle(false)
            })
    }//Row
}

@Composable
private fun VehicleAddForm() {
    val viewModel: VehiclesViewModel = koinViewModel()
    val uiState: State<VehiclesUiState> = viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.padding(15.dp)
            .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 10.dp)
        ) {
            TextFieldComponent.show(
                uiState.value.vehicleName,
                "Vehicle",
                onValueChange = {
                    viewModel.updateVehicleName(it)
                })

            TextBigFieldComponent.show(
                uiState.value.vehicleModel,
                3,
                "Model",
                onValueChange = {
                    viewModel.updateVehicleModel(it)
                })

            TextWithDatePickerComponent.show(
                uiState.value.vehicleDateFormatted,
                uiState.value.vehicleDate,
                "Date of acquisition",
                onDateSelected = {
                    Klog.line("onDateSelected: it: $it")
                    viewModel.onDateSelected(it)
                })

            TextBigFieldComponent.show(
                uiState.value.vehicleNotes,
                5,
                "Notes",
                onValueChange = {
                    viewModel.updateVehicleNotes(it)
                })

        }
    }
}