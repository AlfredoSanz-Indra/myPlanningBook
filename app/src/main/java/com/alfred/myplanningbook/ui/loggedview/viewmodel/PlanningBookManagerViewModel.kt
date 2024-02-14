package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorEmail
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.TextValidatorOnlyNaturalChars
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.StateService
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
    var planningBookList: MutableList<PlanningBook> = mutableListOf(),
    var isSharing: Boolean = false,
    var isSharingPB: String = "",
    var shareToEmail: String = "",
    var shareToEmailError: Boolean = false,
    var shareToEmailErrorText: String = "",
    var isToDeletePB: Boolean = false,
    var pbIDtoDelete: String = ""
)

class PlanningBookManagerViewModel(private val planningBookService: PlanningBookService,
                                   private val ownerService: OwnerService,
                                   private val stateService: StateService): ViewModel() {

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
     * When i want to set Active a planning book of my list.
     */
    fun setActivePlanningBook(planningBookID: String) {

        Klog.linedbg("PlanningBookManagerViewModel","setActivePlanningBook", "planningBookID: $planningBookID")
        clearErrors()
        if(planningBookID.isNullOrBlank()) {
            Klog.linedbg("PlanningBookManagerViewModel", "setActivePlanningBook", "Validation was unsuccessful")
            return
        }
        Klog.linedbg("PlanningBookManagerViewModel", "setActivePlanningBook", "Validation has been successful")

        viewModelScope.launch {
            try {
                val resp = stateService.updateState_activePlanningBook(planningBookID, AppState.owner!!.id)
                Klog.linedbg("PlanningBookManagerViewModel", "setActivePlanningBook", " owner has been updated, resp: $resp")
                if (resp.result && resp.code == 200) {
                    updateViewPlanningBooks()
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}")
                }
                Klog.linedbg("PlanningBookManagerViewModel", "setActivePlanningBook", "view data has been updated")
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "setActivePlanningBook", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel","setActivePlanningBook"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error creating planning books, please login again!")
            }
        }//launch
    }

    fun isToDeletePlanningBook(action: Boolean, pbIdToDelete: String) {

        Klog.linedbg("PlanningBookManagerViewModel","isToDeletePlanningBook", "click, action -> $action, pbIdToDelete -> $pbIdToDelete")
        clearErrors()
        val id = if(action)
            pbIdToDelete
        else
            ""

        updatePbIDtoDelete(id)
        updateIsToDeletePB(action)
    }

    fun deletePlanningBook() {

        Klog.linedbg("PlanningBookManagerViewModel","deletePlanningBook", "planningBookID: ${_uiState.value.pbIDtoDelete}")

        if(_uiState.value.pbIDtoDelete.isNullOrEmpty()) {
            Klog.linedbg("PlanningBookManagerViewModel","deletePlanningBook", "Planning Book id to delete is missing")
            return
        }

        viewModelScope.launch {
            try {
                val resp = stateService.removePlanningBook(_uiState.value.pbIDtoDelete)
                Klog.linedbg("PlanningBookManagerViewModel", "deletePlanningBook", " PlanningBook forgotten, resp: $resp")
                if (resp.result && resp.code == 200) {
                    updateViewPlanningBooks()
                    updatePbIDtoDelete("")
                    updateIsToDeletePB(false)
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}")
                }
                Klog.linedbg("PlanningBookManagerViewModel", "deletePlanningBook", "PlanningBook has been deleted or not")
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "deletePlanningBook", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel","deletePlanningBook"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error deleting planning book!")
            }
        }//launch
    }

    fun sharePlanningBook_ON(pbID: String) {

        Klog.linedbg("PlanningBookManagerViewModel","sharePlanningBook_ON", "click")
        clearErrors()
        updateIsSharing(true)
        updateIsSharingPB(pbID)
        updateShareToEmail("")
    }

    fun sharePlanningBook_OFF() {

        Klog.linedbg("PlanningBookManagerViewModel","sharePlanningBook_OFF", "click")
        clearErrors()
        updateIsSharing(false)
        updateIsSharingPB("")
        updateShareToEmail("")
    }

    /**
     * When i want to share a planning book of my own with another user.
     */
    fun sharePlanningBook(planningBook: PlanningBook) {

        Klog.linedbg("PlanningBookManagerViewModel","sharePlanningBook", "planningBook: $planningBook")
        clearErrors()

        if(!validateSharePBFields()){
            Klog.linedbg("PlanningBookManagerViewModel", "sharePlanningBook", "Validation was unsuccessful")
            return
        }
        Klog.linedbg("PlanningBookManagerViewModel", "sharePlanningBook", "Validation has been successful")

        viewModelScope.launch {
            try {
                val resp = ownerService.sharePlanningBookToOtherOwner(uiState.value.shareToEmail.trim(), planningBook.id)
                Klog.linedbg("PlanningBookManagerViewModel", "sharePlanningBook", " PlanningBook shared, resp: $resp")
                if (resp.result && resp.code == 200) {
                    updateIsSharing(false)
                    updateIsSharingPB("")
                    updateShareToEmail("")
                }
                else {
                    updateShareToEmailError(" ${resp.code}: ${resp.message}")
                }
                Klog.linedbg("PlanningBookManagerViewModel", "sharePlanningBook", "PlanningBook has been shared or not")
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "sharePlanningBook", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel","sharePlanningBook"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error sharing planning book!")
            }
        }//launch
    }

    /**
     * When the planning book is owned by other user that has
     * shared it with me and i want to remove it from my list.
     */
    fun forgetPlanningBook(planningBook: PlanningBook) {

        Klog.linedbg("PlanningBookManagerViewModel","forgetPlanningBook", "planningBook: $planningBook")

        viewModelScope.launch {
            try {
                val resp = stateService.forgetSharedPlanningBook(planningBook.id)
                Klog.linedbg("PlanningBookManagerViewModel", "forgetPlanningBook", " PlanningBook forgotten, resp: $resp")
                if (resp.result && resp.code == 200) {
                    updateViewPlanningBooks()
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}")
                }
                Klog.linedbg("PlanningBookManagerViewModel", "forgetPlanningBook", "PlanningBook has been forgotten or not")
            }
            catch (e: Exception) {
                Klog.stackTrace("PlanningBookManagerViewModel", "forgetPlanningBook", e.stackTrace)
                Klog.line("PlanningBookManagerViewModel","forgetPlanningBook"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Error forgetting planning book!")
            }
        }//launch
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

    private fun validateSharePBFields(): Boolean {

        val chainTxtValEmail = ChainTextValidator(
            TextValidatorLength(5, 50),
            TextValidatorEmail()
        )
        val valResult = chainTxtValEmail.validate(uiState.value.shareToEmail.trim())

        var result = true
        if(valResult is ValidatorResult.Error) {
            updateShareToEmailError(valResult.message)
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

    private fun updateIsToDeletePB(action: Boolean) {

        _uiState.update {
            it.copy(isToDeletePB = action)
        }
    }

    private fun updatePbIDtoDelete(id: String) {

        _uiState.update {
            it.copy(pbIDtoDelete = id)
        }
    }

    private fun updateIsSharing(action: Boolean) {

        _uiState.update {
            it.copy(isSharing = action)
        }
    }

    private fun updateIsSharingPB(pbID: String) {

        _uiState.update {
            it.copy(isSharingPB = pbID)
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

    fun updateShareToEmail(txt: String) {

        _uiState.update {
            it.copy(shareToEmail = txt)
        }
    }

    fun updateShareToEmailError(txt: String) {

        _uiState.update {
            it.copy(shareToEmailError = true)
        }

        _uiState.update {
            it.copy(shareToEmailErrorText = txt)
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
        _uiState.update {
            it.copy(shareToEmailError = false)
        }
        _uiState.update {
            it.copy(shareToEmailErrorText = "")
        }
    }
}