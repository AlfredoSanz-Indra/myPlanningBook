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
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.ErrorGeneralField
import com.alfred.myplanningbook.ui.common.ExecutingRowComponent
import com.alfred.myplanningbook.ui.common.TitleViewComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.sections.VehDetailFormSection
import com.alfred.myplanningbook.ui.loggedview.vehicles.sections.VehDetailHeaderSection
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
class VehiclesDetailView {
    @Composable
    fun createView(onBack: () -> Unit) {
        val viewModel: VehiclesDetailViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val isInitialized = remember { mutableStateOf(false) }
        if(uiState.flagInitial != isInitialized.value) {
            viewModel.init()
        }

        if(uiState.flagFinal) {
            onBack()
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TitleViewComponent.show("New vehicle")
                Spacer(modifier = Modifier.height(10.dp))

                VehDetailHeaderSection.show(onSave = {
                                                viewModel.save()
                                            },
                                            onCancel = {
                                                onBack()
                                            })

                if(uiState.isVehiclesLoading) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ExecutingRowComponent.show("${uiState.vehiclesLoadingMessage} ...")
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if(uiState.generalError) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ErrorGeneralField.showRow(uiState.generalErrorText)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                VehDetailFormSection.show()
            }
        }
    }
}