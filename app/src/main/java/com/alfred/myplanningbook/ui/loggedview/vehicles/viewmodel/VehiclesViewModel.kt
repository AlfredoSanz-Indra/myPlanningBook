package com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Alfredo Sanz
 * @time 2025
 */
data class VehiclesUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var flagInitial: Boolean = true,
    var isVehiclesLoading: Boolean = false,

    )
class VehiclesViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()

    fun init() {
        clearState()
        updateFlagInitial(false)


    }



    private fun updateFlagInitial(flag: Boolean) {
        _uiState.update {
            it.copy(flagInitial = flag)
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