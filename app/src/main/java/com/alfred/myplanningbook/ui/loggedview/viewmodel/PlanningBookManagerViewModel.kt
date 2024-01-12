package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class PlanningBookManagerUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var currentPlanningBook: String = "",
    var planningBookList: MutableList<PlanningBook> = mutableListOf()
)

class PlanningBookManagerViewModel(val planningBookService: PlanningBookService): ViewModel() {

    private val _uiState = MutableStateFlow(PlanningBookManagerUiState())
    val uiState: StateFlow<PlanningBookManagerUiState> = _uiState.asStateFlow()

    fun loadPlanningBooks() {

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        val currentPBs: MutableList<PlanningBook> = mutableListOf()
        currentPBs.add(AppState.activePlanningBook!!)

        viewModelScope.launch {
            try {
                val defer = viewModelScope.async {
                    var r = mutableListOf<PlanningBook>()
                    if (!AppState.owner!!.planningBooks.isNullOrEmpty()) {
                        val resp: SimpleResponse = planningBookService.loadPlanningBooks(AppState.owner!!.planningBooks!!)
                        Klog.linedbg("PlanningBookManagerViewModel","loadPlanningBooks","resp: $resp")

                        if(resp.result && resp.code == 200) {
                            r = resp.planningBookList!!
                        }
                    }

                    return@async r
                }

                val pbList: MutableList<PlanningBook>? = defer.await()
                if(pbList.isNullOrEmpty()) {
                    currentPBs.addAll(pbList!!)
                }

                AppState.planningBooks = currentPBs
                updatePlanningBookList(currentPBs)
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "loadState", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel", "loadPlanningBooks", " Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error loading planning books, please login again!")
            }
        }
    }

    private fun updateCurrentPlanningBook(pbText: String) {
        _uiState.update {
            it.copy(currentPlanningBook = pbText)
        }
    }

    private fun updatePlanningBookList(pbList: MutableList<PlanningBook>) {
        _uiState.update {
            it.copy(planningBookList = pbList)
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

    private fun clearErrors() {

        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }
}