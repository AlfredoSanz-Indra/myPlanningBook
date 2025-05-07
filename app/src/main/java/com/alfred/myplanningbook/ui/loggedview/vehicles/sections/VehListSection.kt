package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.vehicles.components.VehicleCardComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object VehListSection {
    @Composable
    fun show() {
        val viewModel: VehiclesViewModel = koinViewModel()
        val uiState: State<VehiclesUiState> = viewModel.uiState.collectAsStateWithLifecycle()

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
                    .fillMaxHeight()
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    items(uiState.value.vehicleList.size, itemContent = { it ->
                        val vehicle = uiState.value.vehicleList[it]
                        VehicleCardComponent.show(vehicle,
                                                  onEdit = {
                                                      Klog.line("VehListSection -> onEdit")
                                                  },
                                                  onDelete = {
                                                      Klog.line("VehListSection -> onDelete")
                                                  },
                                                  onNavigate = {
                                                      Klog.line("VehListSection -> onNavigate")
                                                  })
                    })
                } //lazy
            } //Box
        }
    }

}