package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class PlanningBookManagerUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var createPlanningAction: Boolean = false,
    var activitiesAction: Boolean = false,
    var currentPlanningBook: String = ""
)

class PlanningBookManagerViewModel(val planningBookService: PlanningBookService): ViewModel() {

    private val _uiState = MutableStateFlow(PlanningBookManagerUiState())
    val uiState: StateFlow<PlanningBookManagerUiState> = _uiState.asStateFlow()

}