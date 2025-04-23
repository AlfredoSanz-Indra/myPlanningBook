package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailViewModel
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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


    }

}