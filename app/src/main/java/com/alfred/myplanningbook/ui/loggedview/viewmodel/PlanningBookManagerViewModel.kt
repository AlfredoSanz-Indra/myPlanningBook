package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.TextValidatorOnlyNaturalChars
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
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
    var pbName: String = "",
    var pbNameError: Boolean = false,
    var pbNameErrorText: String = "",
    var isToCreatePB: Boolean = false,
    var currentPlanningBook: String = "",
    var planningBookList: MutableList<PlanningBook> = mutableListOf()
)

class PlanningBookManagerViewModel(val planningBookService: PlanningBookService): ViewModel() {

    private val _uiState = MutableStateFlow(PlanningBookManagerUiState())
    val uiState: StateFlow<PlanningBookManagerUiState> = _uiState.asStateFlow()

    fun loadPlanningBooks() {

        clearErrors();

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        updateViewPlanningBooks()
    }

    fun showPBCreationSection(action: Boolean) {

        clearErrors()
        updatePBName("")
        updateIsToCreatePB(action)
    }

    /**
     * Create a new Planning book owned by me.
     */
    fun createPlanningBook() {

        clearErrors()
        if(!validateFields()) {
            Klog.linedbg("PlanningBookManagerViewModel", "createPlanningBook", "Validation was unsuccessful")
            return
        }
        Klog.linedbg("PlanningBookManagerViewModel", "createPlanningBook", "Validation has been successful")

        viewModelScope.launch {
            try {
                val resp: SimpleResponse = planningBookService.createPlanningBook(uiState.value.pbName.trim())
                Klog.linedbg("PlanningBookManagerViewModel","createPlanningBook", "resp: $resp")
                Klog.linedbg("PlanningBookManagerViewModel","createPlanningBook", "resp.planningBoo: ${resp.planningBook}")

                if (resp.result && resp.code == 200) {
                    showPBCreationSection(false)
                    updateViewPlanningBooks()
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}")
                }
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "createPlanningBook", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel","createPlanningBook"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error creating planning books, please login again!")
            }
        }//launch
    }

    /**
     * Check if the the planning book is currently active
     */
    fun isActivePlanningBook(pb: PlanningBook): Boolean {

        var result = false
        Klog.linedbg("PlanningBookManagerViewModel","isActivePlanningBook", "pb: $pb")
        if(AppState.owner!!.activePlanningBook != null) {
            if(AppState.owner!!.activePlanningBook == pb.id) {
                result = true
            }
        }
        return result
    }

    /**
     * Check if the planning book is owned by me or if it is shared
     * with me.
     */
    fun isSharedWithMePlanningBook(pb: PlanningBook): Boolean {

        var result = true
        if(pb.idOwner == AppState.owner!!.id) {
            result = false
        }

        return result
    }

    /**
     * When the planning book is owned by other user that has
     * shared it with me and i want to remove it from my list.
     */
    fun forgetPlanningBook(pb: PlanningBook) {

        Klog.linedbg("PlanningBookManagerViewModel","forgetPlanningBook", "planningBookID: $pb")
    }

    fun setActivePlanningBook(planningBookID: String) {

        Klog.linedbg("PlanningBookManagerViewModel","setActivePlanningBook", "planningBookID: $planningBookID")
    }

    fun deletePlanningBook(planningBookID: String) {

        Klog.linedbg("PlanningBookManagerViewModel","deletePlanningBook", "planningBookID: $planningBookID")
    }

    fun sharePlanningBook(pb: PlanningBook) {

        Klog.linedbg("PlanningBookManagerViewModel","sharePlanningBook", "planningBookID: $pb")
    }

    private fun validateFields(): Boolean {

        val chainTxt = ChainTextValidator(
            TextValidatorLength(5, 20),
            TextValidatorOnlyNaturalChars()
        )
        val valResult = chainTxt.validate(uiState.value.pbName.trim())

        var result = true
        if(valResult is ValidatorResult.Error) {
            updatePBNameError(valResult.message)
            result = false
        }

        return result
    }

    private fun updateViewPlanningBooks() {

        if(AppState.activePlanningBook != null) {
            updateCurrentPlanningBook(AppState.activePlanningBook!!.name)
        }
        if(AppState.planningBooks.isNotEmpty()) {
            updatePlanningBookList(AppState.planningBooks)
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

    fun updatePBName(txt: String) {
        _uiState.update {
            it.copy(pbName = txt)
        }
    }

    private fun updatePBNameError(txt: String) {
        _uiState.update {
            it.copy(pbNameError = true)
        }

        _uiState.update {
            it.copy(pbNameErrorText = txt)
        }
    }

    private fun updateIsToCreatePB(action: Boolean) {
        _uiState.update {
            it.copy(isToCreatePB = action)
        }
    }

    private fun clearErrors() {

        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
        _uiState.update {
            it.copy(pbNameError = false)
        }

        _uiState.update {
            it.copy(pbNameErrorText = "")
        }
    }
}