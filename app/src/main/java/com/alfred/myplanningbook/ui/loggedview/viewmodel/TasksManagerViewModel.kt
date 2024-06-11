package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.StateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class TaskManagerUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var currentPlanningBook: String = "",
    var isToCreateTask: Boolean = false,
    var taskName: String = "",
    var taskDesc: String = "",
    var taskDate: Long = 0,
    var taskDateFormatted: String = "",
    var taskNameError: Boolean = false,
    var taskNameErrorTxt: String = "",
    var taskDescError: Boolean = false,
    var taskDescErrorTxt: String = "",
    var taskDateError: Boolean = false,
    var taskDateErrorTxt: String = "",
    var openCalendarDialog: Boolean = false,
)

class TasksManagerViewModel(private val planningBookService: PlanningBookService,
                            private val ownerService: OwnerService,
                            private val stateService: StateService): ViewModel() {

    private val _uiState = MutableStateFlow(TaskManagerUiState())
    val uiState: StateFlow<TaskManagerUiState> = _uiState.asStateFlow()
    val taskName_maxLength = 30
    val taskDesc_maxLength = 100

    fun loadTasks() {

        clearErrors();

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        updateViewTasks()
    }

    fun showTaskCreationSection(action: Boolean) {
        clearErrors()
        updateTaskDate(DateTimeUtils.currentDate(), DateTimeUtils.currentDateFormatted())
        updateIsToCreateTask(action)
    }

    fun openCalendarDi() {
        updateOpenCalendarDialog(true)
    }

    fun closeCalendarDi() {
        Klog.line("TasksManagerViewModel", "closeCalendarDi", "closeCalendarDi")
        updateOpenCalendarDialog(false)
    }

    fun onDateSelected(dateInMill: Long) {
        Klog.line("TasksManagerViewModel", "onDateSelected", "dateInMill: ${dateInMill}")

        val dateFormatted = DateTimeUtils.formatDate(dateInMill)
        updateTaskDate(dateInMill, dateFormatted)
        updateOpenCalendarDialog(false)
    }

    fun createTask() {

    }


    private fun updateViewTasks() {
        if(AppState.activePlanningBook != null) {
            updateCurrentPlanningBook(AppState.activePlanningBook!!.name)
        }
    }

    private fun updateIsToCreateTask(action: Boolean) {
        _uiState.update {
            it.copy(isToCreateTask = action)
        }
    }

    private fun updateCurrentPlanningBook(pbText: String) {
        _uiState.update {
            it.copy(currentPlanningBook = pbText)
        }
    }

    private fun updateOpenCalendarDialog(action: Boolean) {
        _uiState.update {
            it.copy(openCalendarDialog = action)
        }
    }

    fun updateTaskName(txt: String) {
        Klog.line("TasksManagerViewModel", "updateTaskName", "txt size: ${txt.length}")
        if(txt.length <= taskName_maxLength) {
            _uiState.update {
                it.copy(taskName = txt)
            }
        }
    }

    private fun updateTaskNameError(txt: String) {
        _uiState.update {
            it.copy(taskNameErrorTxt = txt)
        }

        _uiState.update {
            it.copy(taskNameError = false)
        }
    }

    fun updateTaskDesc(txt: String) {
        Klog.line("TasksManagerViewModel", "updateTaskDesc", "txt size: ${txt.length}")
        if(txt.length < taskDesc_maxLength) {
            _uiState.update {
                it.copy(taskDesc = txt)
            }
        }
    }

    private fun updateTaskDescError(txt: String) {
        _uiState.update {
            it.copy(taskDescErrorTxt = txt)
        }

        _uiState.update {
            it.copy(taskDescError = false)
        }
    }

    private fun updateTaskDate(dateInMill: Long, dateInStr: String) {
        _uiState.update {
            it.copy(taskDate = dateInMill)
        }

        _uiState.update {
            it.copy(taskDateFormatted = dateInStr)
        }
    }

    private fun updateTaskDateError(txt: String) {
        _uiState.update {
            it.copy(taskDateErrorTxt = txt)
        }

        _uiState.update {
            it.copy(taskDateError = false)
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
            it.copy(taskNameErrorTxt = "")
        }
        _uiState.update {
            it.copy(taskNameError = false)
        }
        _uiState.update {
            it.copy(taskDescErrorTxt = "")
        }
        _uiState.update {
            it.copy(taskDescError = false)
        }
        _uiState.update {
            it.copy(taskDateError = false)
        }
        _uiState.update {
            it.copy(taskDateErrorTxt = "")
        }
    }
}