package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class BookMenuUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var currentPlanningBook: String = "",
    var isStateLoaded: Boolean = false,
    var isToLogout: Boolean = false,
    var isToPlanningBookManager: Boolean = false
)
class BookMenuViewModel(private val usersService: UsersService,
                        private val planningBookService: PlanningBookService): ViewModel()  {

    private val _uiState = MutableStateFlow(BookMenuUiState())
    val uiState: StateFlow<BookMenuUiState> = _uiState.asStateFlow()

    fun loadState() {

        Klog.line("BookMenuViewModel", "loadState", "loading state")
        if(AppState.useremail == null) {
            setGeneralError(" 501: login was not successfull and user info was not loaded")
            return
        }

        viewModelScope.launch {
            try {
                val resp: SimpleResponse = planningBookService.loadState(AppState.useremail!!)
                Klog.line("BookMenuViewModel", "loadState", "resp: $resp")

                if (resp!!.result) {
                    updateIsStateLoaded(true)

                    Klog.line("BookMenuViewModel", "loadState", "State Successfully loaded")
                    if (resp.planningBook != null) {
                        updateCurrentPlanningBook(resp.planningBook!!.name)
                    }
                    else {
                        updateCurrentPlanningBook("You don't have any Planning Book active yet")
                    }

                    if (resp.owner == null) {
                        setGeneralError(" Owner is not accessible, please log in again!")
                        updateIsStateLoaded(false)
                    }
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}, please log in again!")
                }
            }
            catch (e: Exception) {
                Klog.stackTrace("BookMenuViewModel", "loadState", e.stackTrace)
                Klog.line("BookMenuViewModel","loadState"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, please log in again!")
            }
        }
    }

    fun logoutUser() {

        clearErrors()
        clearFields()

        viewModelScope.launch {
            try {
                val resp = usersService.logoutUser()
                Klog.linedbg("BookMenuViewModel", "logoutUser", "resp: $resp")

                if (resp != null) {
                    if (resp!!.result) {
                        updateIsToLogout(true)
                    }
                    else {
                        setGeneralError(" ${resp!!.code}: ${resp!!.message}")
                    }
                }
                else {
                    setGeneralError(" 500: Undefined Error, couldn't log out")
                }
            }
            catch (e: Exception) {
                Klog.stackTrace("BookMenuViewModel", "logoutUser", e.stackTrace)
                Klog.line("BookMenuViewModel","logoutUser"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, Couldn't log out!")
            }
        }
    }

    fun tasksView() {

    }

    fun planningbookView() {

        updateIsToPlanningBookManager(true)
    }

    fun activitiesView() {

    }

    private fun updateCurrentPlanningBook(pbText: String) {
        _uiState.update {
            it.copy(currentPlanningBook = pbText)
        }
    }

    private fun updateIsStateLoaded(action: Boolean) {
        _uiState.update {
            it.copy(isStateLoaded = action)
        }
    }

    private fun updateIsToLogout(action: Boolean) {
        _uiState.update {
            it.copy(isToLogout = action)
        }
    }

    private fun updateIsToPlanningBookManager(action: Boolean) {
        _uiState.update {
            it.copy(isToPlanningBookManager = action)
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

    fun clearFields() {

        _uiState.update {
            it.copy(isToLogout = false)
        }

        _uiState.update {
            it.copy(isToPlanningBookManager = false)
        }
    }
}