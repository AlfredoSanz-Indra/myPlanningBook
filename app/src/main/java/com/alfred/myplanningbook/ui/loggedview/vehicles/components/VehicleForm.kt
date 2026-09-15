package com.alfred.myplanningbook.ui.loggedview.vehicles.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.TextBigFieldComponent
import com.alfred.myplanningbook.ui.common.TextFieldComponent
import com.alfred.myplanningbook.ui.common.TextWithDatePickerComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import org.koin.androidx.compose.koinViewModel

/**
 *  * @author Alfredo Sanz
 *  * @time 2026
 */
object VehicleForm {

    @Composable
    fun show() {
        val viewModel: VehiclesViewModel = koinViewModel()
        val uiState: State<VehiclesUiState> = viewModel.uiState.collectAsStateWithLifecycle()

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

        TextWithDatePickerComponent.show(
            uiState.value.vehicleTerminationDateFormatted ?: "",
            uiState.value.vehicleTerminationDate ?: 0L,
            "Termination date",
            onDateSelected = {
                Klog.line("onTerminationDateSelected: it: $it")
                viewModel.onTerminationDateSelected(it)
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