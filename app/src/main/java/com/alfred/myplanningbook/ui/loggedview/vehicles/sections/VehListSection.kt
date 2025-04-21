package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.runtime.Composable
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object VehListSection {
    @Composable
    fun showSection() {
        val _uiState = MutableStateFlow(VehiclesUiState())
        val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()


    }

}