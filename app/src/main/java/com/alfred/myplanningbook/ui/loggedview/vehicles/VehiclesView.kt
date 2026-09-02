package com.alfred.myplanningbook.ui.loggedview.vehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.ErrorGeneralField
import com.alfred.myplanningbook.ui.common.ExecutingRowComponent
import com.alfred.myplanningbook.ui.common.TitleViewComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.sections.VehHeaderSection
import com.alfred.myplanningbook.ui.loggedview.vehicles.sections.VehListSection
import com.alfred.myplanningbook.ui.loggedview.vehicles.sections.VehicleAddSection
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
class VehiclesView {
    @Composable
    fun createView(onBack: () -> Unit) {
        val viewModel: VehiclesViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val isInitialized = remember { mutableStateOf(false) }
        if(uiState.flagInitial != isInitialized.value) {
            viewModel.init()
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                if(uiState.isVehiclesLoading) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ExecutingRowComponent.show("Loading ...")
                    Spacer(modifier = Modifier.height(10.dp))
                }
                else {
                    Spacer(modifier = Modifier.height(20.dp))
                    if (uiState.generalError) {
                        Spacer(modifier = Modifier.height(10.dp))
                        ErrorGeneralField.showRow(uiState.generalErrorText)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    TitleViewComponent.show(uiState.headerMessage)
                    Spacer(modifier = Modifier.height(10.dp))

                    VehicleBodySection( onNew = {
                        viewModel.showNewVehicle(true)
                    }, onBack)
                }
            }
        }
    }

    @Composable
    private fun VehicleBodySection(onNew: () -> Unit, onBack: () -> Unit) {
        val viewModel: VehiclesViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToAddVehicle) {
            VehicleAddSection()
        }
        else if(uiState.isToUpdateVehicle) {
           // LibraryUpdateSection()
        }
        else if(uiState.isToMaintainVehicle) {
            //LibraryFilterSection()
        }
        else {
            VehHeaderSection.show(onNew = { onNew() }, onBack = { onBack() })
            VehListSection.show()
        }
    }
}