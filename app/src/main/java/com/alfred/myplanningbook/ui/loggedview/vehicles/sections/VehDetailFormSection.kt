package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.TextBigFieldComponent
import com.alfred.myplanningbook.ui.common.TextFieldComponent
import com.alfred.myplanningbook.ui.common.TextWithDatePickerComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailViewModel
import org.koin.androidx.compose.koinViewModel

object VehDetailFormSection {
    @Composable
    fun show() {
        val viewModel: VehiclesDetailViewModel = koinViewModel()
        val uiState: State<VehiclesDetailUiState> = viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier.padding(15.dp)
                               .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                               .fillMaxWidth()
                               .fillMaxHeight()
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 10.dp)
            ) {
                TextFieldComponent.show(uiState.value.vehicleName,
                                        "Vehicle",
                                        onValueChange = {
                                            viewModel.updateVehicleName(it)
                                        })

                TextBigFieldComponent.show(uiState.value.vehicleModel,
                                           3,
                                           "Model",
                                           onValueChange = {
                                               viewModel.updateVehicleModel(it)
                                           })

                TextWithDatePickerComponent.show(uiState.value.vehicleDateFormatted,
                                                 uiState.value.vehicleDate,
                                                 "Acquisition date",
                                                 onDateSelected = {
                                                     Klog.line("onDateSelected: it: $it")
                                                     viewModel.onDateSelected(it)
                                                 })

                TextBigFieldComponent.show(uiState.value.vehicleNotes,
                                           5,
                                           "Notes",
                                           onValueChange = {
                                               viewModel.updateVehicleNotes(it)
                                           })

            }
        }
    }
}