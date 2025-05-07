package com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle
import com.alfred.myplanningbook.domain.usecaseapi.vehicle.VehicleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Alfredo Sanz
 * @time 2025
 */
data class VehiclesUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var flagInitial: Boolean = true,
    var isVehiclesLoading: Boolean = false,
    var vehicleList: MutableList<Vehicle> = mutableStateListOf(),

    )
class VehiclesViewModel(private val vehicleService: VehicleService): ViewModel() {
    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()

    fun init() {
        clearState()
        updateFlagInitial(false)

        loadVehicles()
    }

    private fun loadVehicles() {
        Klog.line("VehiclesViewModel", "loadVehicles", "loadVehicles")

        updateIsVehicleLoading(true)

        viewModelScope.launch {
            val resp = vehicleService.getVehicles(AppState.useremail!!)
            Klog.line("VehiclesViewModel", "loadVehicles", "resp: $resp")
            if(resp.result) {
                updateVehicleList(resp.vehicleList ?: mutableStateListOf())
                clearErrors()
                clearState()
                uiState.value.vehicleList.forEach { Klog.line("VehiclesViewModel", "loadVehicles", "vehicle: $it") }

            }
            else {
                Klog.line("VehiclesViewModel", "loadVehicles", "error")
                setGeneralError(" ${resp.code}: ${resp.message}")
                updateVehicleList(mutableStateListOf())
            }
            updateIsVehicleLoading(false)
        }

    }


    private fun updateVehicleList(list: List<Vehicle>) {
        _uiState.update {
            it.copy(vehicleList = list.toMutableStateList())
        }
    }

    private fun updateFlagInitial(flag: Boolean) {
        _uiState.update {
            it.copy(flagInitial = flag)
        }
    }

    private fun updateIsVehicleLoading(flag: Boolean) {
        _uiState.update {
            it.copy(isVehiclesLoading = flag)
        }
    }

    private fun setGeneralError(txt: String) {
        _uiState.update {
            it.copy(generalError = true)
        }
        _uiState.update {
            it.copy(generalErrorText = txt)
        }
    }

    private fun clearState() {
        updateFlagInitial(false)
    }

    private fun clearErrors() {
        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }
}