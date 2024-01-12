package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
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
data class BookMenuUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var logoutAction: Boolean = false,
    var planningbookAction: Boolean = false,
    var tasksAction: Boolean = false,
    var activitiesAction: Boolean = false,
    var currentPlanningBook: String = ""
)
class BookMenuViewModel(val usersService: UsersService,
                        val planningBookService: PlanningBookService): ViewModel()  {

    private val _uiState = MutableStateFlow(BookMenuUiState())
    val uiState: StateFlow<BookMenuUiState> = _uiState.asStateFlow()

    suspend fun loadState() {
        Klog.line("BookMenuViewModel", "loadState", "loading state")
        if(AppState.useremail == null) {
            setGeneralError(" 501: login was not successfull and user info was not loaded")
            return
        }

        var respo: SimpleResponse? = null
        try {
            val defer = viewModelScope.async {
                val resp: SimpleResponse = planningBookService.loadState(AppState.useremail!!)
                Klog.line("BookMenuViewModel", "loadState", "resp: $resp")

                return@async resp
            }
            respo = defer.await()
        }
        catch (e: Exception) {
            Klog.stackTrace("BookMenuViewModel", "loadState", e.stackTrace)
            Klog.line("BookMenuViewModel", "loadState", " Exception localizedMessage: ${e.localizedMessage}")
            setGeneralError(" 500: ${e.message}, please make login again!")
        }

        if (respo!!.result) {
            Klog.line("BookMenuViewModel", "loadState", "State Successfully loaded")
            if(respo.planningBook != null) {
                updateCurrentPlanningBook(respo.planningBook!!.name)
            }
            else {
                updateCurrentPlanningBook("You don't have any Planning Book active yet")
            }

            if(respo.owner == null) {
                setGeneralError(" Owner is not accessible, please make login again!")
            }
        }
        else {
            setGeneralError(" ${respo.code}: ${respo.message}, please make login again!")
        }
    }

    fun logoutUser(): Boolean {

        var result = false
        clearErrors()

        var respo: SimpleResponse? = null
        try {
            viewModelScope.launch {
                val defer = viewModelScope.async {
                    val resp = usersService.logoutUser()
                    Klog.line("BookMenuViewModel", "logoutUser", "resp: $resp")

                    return@async resp
                }
                respo = defer.await()
            }
        }
        catch (e: Exception) {
            Klog.stackTrace("BookMenuViewModel", "logoutUser", e.stackTrace)
            Klog.line("BookMenuViewModel", "logoutUser", " Exception localizedMessage: ${e.localizedMessage}")
            setGeneralError(" 500: ${e.message}, Couldn't log out!")
        }

        if(respo != null) {
            if(respo!!.result) {
                updateLogoutAction(true)
                result = true
            }
            else {
                setGeneralError(" ${respo!!.code}: ${respo!!.message}")
            }
        }
        else {
            setGeneralError(" 500: Undefined Error, couldn't log out")
        }

        Klog.line("BookMenuViewModel", "logoutUser", "result: $result")
        return result
    }

    fun tasksView() {

    }

    fun planningbookView() {

    }

    fun activitiesView() {

    }

    private fun updateCurrentPlanningBook(pbText: String) {
        _uiState.update {
            it.copy(currentPlanningBook = pbText)
        }
    }

    private fun updateLogoutAction(action: Boolean) {
        _uiState.update {
            it.copy(logoutAction = action)
        }
    }

    private fun updateplanningbookAction(action: Boolean) {
        _uiState.update {
            it.copy(planningbookAction = action)
        }
    }

    private fun updateTasksAction(action: Boolean) {
        _uiState.update {
            it.copy(tasksAction = action)
        }
    }

    private fun updateActivitiesAction(action: Boolean) {
        _uiState.update {
            it.copy(activitiesAction = action)
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

    fun resetActions() {
        updateLogoutAction(false)
        updateplanningbookAction(false)
        updateTasksAction(false)
        updateActivitiesAction(false)
    }
}