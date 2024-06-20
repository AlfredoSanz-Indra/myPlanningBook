package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.usecaseapi.StateService
import com.alfred.myplanningbook.domain.usecaseapi.TaskService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    var taskHour: Int = 0,
    var taskMinute: Int = 0,
    var taskTimeFormatted: String = "",
    var taskTimeError: Boolean = false,
    var taskTimeErrorTxt: String = "",
    var openTimeDialog: Boolean = false,
    var tasksBook: MutableList<TaskBook> = mutableListOf(),
    var isTaskBookListLoaded: Boolean = false
)

class TasksManagerViewModel(private val taskService: TaskService,
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

        Klog.line("TasksManagerViewModel", "loadTasks", "${AppState.activePlanningBook!!.id}")
        updateCurrentPlanningBook(AppState.activePlanningBook!!.id)

        viewModelScope.launch {
            val resp = taskService.getTaskList(AppState.activePlanningBook!!.id,)
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToCreateTask(false)
                updateIsTaskBookListLodaded(true)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }

        updateViewTasks()
    }

    fun showTaskCreationSection(action: Boolean) {
        clearErrors()
        clearState()
        updateTaskDate(DateTimeUtils.currentDate(), DateTimeUtils.currentDateFormatted())
        updateTaskTime(DateTimeUtils.currentHour(), 0, DateTimeUtils.currentTimeFormatted())
        updateIsToCreateTask(action)
    }

    fun openCalendarDi() {
        updateOpenCalendarDialog(true)
    }

    fun closeCalendarDi() {
        updateOpenCalendarDialog(false)
    }

    fun onDateSelected(dateInMill: Long) {
        val dateFormatted = DateTimeUtils.formatDate(dateInMill)
        updateTaskDate(dateInMill, dateFormatted)
        updateOpenCalendarDialog(false)
    }

    fun openTimeDi() {
        updateOpenTimeDialog(true)
    }

    fun closeTimeDi() {
        updateOpenTimeDialog(false)
    }

    fun onTimeSelected(hour: Int, min: Int) {
        val timeFormatted = DateTimeUtils.formatTime(hour, min)
        updateTaskTime(hour, min, timeFormatted)
        updateOpenTimeDialog(false)
    }

    fun createTask() {
        Klog.line("TasksManagerViewModel", "createTask", "-")

        if(!validateFields()) {
            Klog.linedbg("TasksManagerViewModel", "createTask", "Validation was unsuccessfull")
            return
        }
        Klog.linedbg("TasksManagerViewModel", "createTask", "Validation has been success")

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        val taskbook = TaskBook(null,
                                AppState.activePlanningBook!!.id,
                                uiState.value.taskName,
                                uiState.value.taskDesc,
                                uiState.value.taskDate,
                                DateTimeUtils.dateToYear(uiState.value.taskDate),
                                DateTimeUtils.dateToMonth(uiState.value.taskDate),
                                DateTimeUtils.dateToDay(uiState.value.taskDate),
                                uiState.value.taskHour,
                                uiState.value.taskMinute)

        viewModelScope.launch {
            val resp = taskService.createTask(taskbook)
            Klog.line("TasksManagerViewModel", "createTask", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToCreateTask(false)
                updateIsTaskBookListLodaded(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }

        Klog.linedbg("TasksManagerViewModel", "createTask", "Validation OOKK")
    }

    private fun validateFields(): Boolean {

        val chainTxtValName = ChainTextValidator(
            TextValidatorLength(5, 30)
        )
        val chainTxtValDesc = ChainTextValidator(
            TextValidatorLength(5, 100)
        )

        val valResultName = chainTxtValName.validate(uiState.value.taskName.trim())
        val valResultDesc = chainTxtValDesc.validate(uiState.value.taskDesc.trim())

        var result = true
        if(valResultName is ValidatorResult.Error) {
            updateTaskNameError(valResultName.message)
            result = false
        }
        if(valResultDesc is ValidatorResult.Error) {
            updateTaskDescError(valResultDesc.message)
            result = false
        }

        return result
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
    private fun updateOpenTimeDialog(action: Boolean) {
        _uiState.update {
            it.copy(openTimeDialog = action)
        }
    }


    fun updateTaskName(txt: String) {
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
            it.copy(taskDateError = true)
        }
    }

    private fun updateTaskTime(hour: Int, min: Int, timeFormatted: String) {
        _uiState.update {
            it.copy(taskHour = hour)
        }

        _uiState.update {
            it.copy(taskMinute = min)
        }

        _uiState.update {
            it.copy(taskTimeFormatted = timeFormatted)
        }
    }

    private fun updateTaskHour(hour: Int) {
        _uiState.update {
            it.copy(taskHour = hour)
        }
    }

    private fun updateTaskMinute(minute: Int) {
        _uiState.update {
            it.copy(taskMinute = minute)
        }
    }

    private fun updateTaskTimeFormatted(timeFormatted: String) {
        _uiState.update {
            it.copy(taskTimeFormatted = timeFormatted)
        }
    }

    private fun updateTaskTimeError(txt: String) {
        _uiState.update {
            it.copy(taskTimeErrorTxt = txt)
        }

        _uiState.update {
            it.copy(taskTimeError = true)
        }
    }

    private fun updateIsTaskBookListLodaded(action: Boolean) {
        _uiState.update {
            it.copy(isTaskBookListLoaded = action)
        }
    }

    private fun clearState() {
        updateTaskName("")
        updateTaskDesc("")
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
        _uiState.update {
            it.copy(taskTimeError = false)
        }
        _uiState.update {
            it.copy(taskTimeErrorTxt = "")
        }
    }
}