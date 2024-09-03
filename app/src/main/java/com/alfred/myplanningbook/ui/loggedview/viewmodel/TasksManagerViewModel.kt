package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.model.TaskBookNatureEnum
import com.alfred.myplanningbook.domain.usecaseapi.ActivityService
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
    var isToUpdateTask: Boolean = false,
    var taskNature: TaskBookNatureEnum? = null,
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
    var taskBookList: MutableList<TaskBook> = mutableListOf(),
    var isTaskBookListLoaded: Boolean = false,
    var taskBookSelectedId: String = ""
)

class TasksManagerViewModel(private val taskService: TaskService,
                            private val activityService: ActivityService,
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

        Klog.line("TasksManagerViewModel", "loadTasks", AppState.activePlanningBook!!.id)
        updateCurrentPlanningBook(AppState.activePlanningBook!!.name)

        val todayDate: Long = DateTimeUtils.currentDate()

        viewModelScope.launch {
            val resp = taskService.getTaskList(AppState.activePlanningBook!!.id, todayDate)
            if(resp.result) {
                val taskListReal = resp.taskBookList ?: mutableListOf()

                val resp2 = activityService.getActivityList(AppState.activePlanningBook!!.id, 1)
                if(resp2.result) {
                    var taskFromActivities: MutableList<TaskBook> = mutableListOf()
                    if(!resp2.activityBookList.isNullOrEmpty()) {
                        taskFromActivities = generateTasksFromActivities(resp2.activityBookList!!)
                    }
                    taskListReal.addAll(taskFromActivities)
                    taskListReal.sortWith(compareBy({it.year}, {it.month}, {it.day}, {it.hour}, {it.minute}))
                    setDayOfWeekToTasks(taskListReal)
                    excludeActivitiesTaskWhenTaskSimilar(taskListReal)

                    updateTaskBookList(taskListReal)
                    clearErrors()
                    clearState()
                    updateIsToCreateTask(false)
                    updateIsTaskBookListLodaded(true)
                }
                else {
                    setGeneralError(" ${resp2.code}: ${resp2.message}")
                }
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun generateTasksFromActivities(activitiesList: MutableList<ActivityBook>): MutableList<TaskBook> {
        val result: MutableList<TaskBook> = mutableListOf()

        activitiesList.forEach { it ->
            for(cday in 0..14) {
                val cDate = DateTimeUtils.currentDatePlusDays(cday.toLong())
                val cDayOfWeek = DateTimeUtils.currentDayOfWeekPlusDays(cday.toLong())
                for(itDayOfWeek in it.weekDaysList) {
                    var apply = false
                    val itDayOfWeekInt = DateTimeUtils.castDayOfWeekToInt(itDayOfWeek)
                    if(itDayOfWeekInt == cDayOfWeek) {
                        apply = true
                    }
                    if(apply) {
                        val taskB = TaskBook(
                            null,
                            it.idPlanningbook,
                            it.name,
                            it.description,
                            cDate,
                            DateTimeUtils.dateToYear(cDate),
                            DateTimeUtils.dateToMonth(cDate),
                            DateTimeUtils.dateToDay(cDate),
                            it.startHour,
                            it.startMinute,
                            "",
                            TaskBookNatureEnum.IS_ACTIVITY
                        )
                        result.add(taskB)
                        continue
                    }
                }
            }
        }
        return result
    }

    private fun setDayOfWeekToTasks(taskList: MutableList<TaskBook>) {
        taskList.map { it.dayOfWeekStr = DateTimeUtils.translateDaysToSpanish(DateTimeUtils.dateToDayString(it.dateInMillis))}
    }

    private fun excludeActivitiesTaskWhenTaskSimilar(taskBookList: MutableList<TaskBook>) {
        val iterator = taskBookList.listIterator()
        while (iterator.hasNext()) {
            val curTask = iterator.next()
            if(curTask.nature != TaskBookNatureEnum.IS_ACTIVITY) {
                continue
            }

            val similarTasks = taskBookList.filter { it.name == curTask.name &&
                    it.year == curTask.year &&
                    it.month == curTask.month &&
                    it.day == curTask.day &&
                    it.nature != TaskBookNatureEnum.IS_ACTIVITY
                }

            if (similarTasks.isNotEmpty()) {
                iterator.remove()
                continue
            }
        }
    }

    fun showTaskCreationSection(action: Boolean) {
        clearErrors()
        clearState()

        updateTaskNature(TaskBookNatureEnum.ORIGIN_TASK)
        updateTaskDate(DateTimeUtils.currentDate(), DateTimeUtils.currentDateFormatted())
        updateTaskTime(DateTimeUtils.currentHour(), 0, DateTimeUtils.currentTimeFormatted())
        updateIsToCreateTask(action)
    }

    fun showTaskUpdateSection(taskBook: TaskBook) {
        clearErrors()
        clearState()

        Klog.line("TasksManagerViewModel", "showTaskUpdateSection", "taskBook.nature: ${taskBook.nature}")
        when(taskBook.nature) {
            TaskBookNatureEnum.ORIGIN_TASK -> updateTaskBookSelectedId(taskBook.id!!)
            TaskBookNatureEnum.ORIGIN_ACTIVITY -> updateTaskBookSelectedId(taskBook.id!!)
            else -> updateTaskBookSelectedId("0")
        }
        updateTaskNature(taskBook.nature)
        //updateTaskBookSelectedId(taskBook.id!!)
        updateTaskName(taskBook.name)
        updateTaskDesc(taskBook.description ?: "")
        updateTaskDate(taskBook.dateInMillis, DateTimeUtils.formatDate(taskBook.dateInMillis))
        updateTaskTime(taskBook.hour, taskBook.minute, DateTimeUtils.formatTime(taskBook.hour, taskBook.minute))

        updateIsToUpdateTask(true)
    }

    fun hideTaskUpdateSection() {
        clearErrors()
        clearState()

        updateIsToUpdateTask(false)
    }

    fun formatTaskDateTime(taskBook: TaskBook): String {

        var result: String = DateTimeUtils.formatDate(taskBook.year, taskBook.month, taskBook.day)
        result += " - ${DateTimeUtils.formatTime(taskBook.hour, taskBook.minute)}"

        return result
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

        val taskBook = fillTaskBookObj(TaskBookNatureEnum.ORIGIN_TASK)

        viewModelScope.launch {
            val resp = taskService.createTask(taskBook)
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

        Klog.linedbg("TasksManagerViewModel", "createTask", "is created")
    }

    fun updateTask() {
        Klog.line("TasksManagerViewModel", "updateTask", "-")

        if(!validateFields()) {
            Klog.linedbg("TasksManagerViewModel", "updateTask", "Validation was unsuccessfull")
            return
        }
        Klog.linedbg("TasksManagerViewModel", "updateTask", "Validation has been success")

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        when(uiState.value.taskNature) {
            TaskBookNatureEnum.IS_ACTIVITY -> updateTaskFromActivity()
            else -> updateTaskExisting()
        }
        Klog.linedbg("TasksManagerViewModel", "updateTask", "is updated")
    }

    private fun updateTaskFromActivity() {
        val taskBook = fillTaskBookObj(TaskBookNatureEnum.ORIGIN_ACTIVITY)
        Klog.linedbg("TasksManagerViewModel", "updateTaskFromActivity", "taskBook: $taskBook")

        viewModelScope.launch {
            val resp = taskService.createTask(taskBook)
            Klog.line("TasksManagerViewModel", "updateTaskFromActivity", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToUpdateTask(false)
                updateIsTaskBookListLodaded(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun updateTaskExisting() {
        val taskBook = fillTaskBookObj(uiState.value.taskNature!!)
        taskBook.id = uiState.value.taskBookSelectedId
        Klog.linedbg("TasksManagerViewModel", "updateTaskExisting", "taskBook: $taskBook")

        viewModelScope.launch {
            val resp = taskService.updateTask(taskBook)
            Klog.line("TasksManagerViewModel", "updateTaskExisting", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToUpdateTask(false)
                updateIsTaskBookListLodaded(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    fun cloneTask() {
        Klog.line("TasksManagerViewModel", "cloneTask", "-")
        createTask()
        updateIsToUpdateTask(false)
        Klog.linedbg("TasksManagerViewModel", "cloneTask", "is cloned")
    }

    private fun fillTaskBookObj(nature: TaskBookNatureEnum): TaskBook {
        val result = TaskBook(null,
            AppState.activePlanningBook!!.id,
            uiState.value.taskName,
            uiState.value.taskDesc,
            uiState.value.taskDate,
            DateTimeUtils.dateToYear(uiState.value.taskDate),
            DateTimeUtils.dateToMonth(uiState.value.taskDate),
            DateTimeUtils.dateToDay(uiState.value.taskDate),
            uiState.value.taskHour,
            uiState.value.taskMinute,
            "",
            nature)

        return result
    }

    private fun validateFields(): Boolean {
        clearErrors()

        val chainTxtValName = ChainTextValidator(
            TextValidatorLength(5, taskName_maxLength)
        )
        val chainTxtValDesc = ChainTextValidator(
            TextValidatorLength(5, taskDesc_maxLength)
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

    private fun updateIsToCreateTask(action: Boolean) {
        _uiState.update {
            it.copy(isToCreateTask = action)
        }
    }

    private fun updateIsToUpdateTask(action: Boolean) {
        _uiState.update {
            it.copy(isToUpdateTask = action)
        }
    }

    private fun updateTaskBookSelectedId(id: String) {
        _uiState.update {
            it.copy(taskBookSelectedId = id)
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

    private fun updateTaskNature(nature: TaskBookNatureEnum?) {
        _uiState.update {
            it.copy(taskNature = nature)
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
            it.copy(taskNameError = true)
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
            it.copy(taskDescError = true)
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

    private fun updateIsTaskBookListLodaded(action: Boolean) {
        _uiState.update {
            it.copy(isTaskBookListLoaded = action)
        }
    }

    private fun updateTaskBookList(_taskBookList: MutableList<TaskBook>) {
        _uiState.update {
            it.copy(taskBookList = _taskBookList)
        }
    }

    private fun clearState() {
        updateTaskNature(null)
        updateTaskName("")
        updateTaskDesc("")
        updateTaskDate(0, "")
        updateTaskTime(0, 0, "")
        updateTaskBookSelectedId("")
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