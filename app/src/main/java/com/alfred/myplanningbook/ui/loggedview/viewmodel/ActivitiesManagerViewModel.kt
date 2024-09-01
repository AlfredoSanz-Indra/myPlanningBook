package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.TimeGreaterValidator
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.usecaseapi.ActivityService
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
data class ActivitiesManagerUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var currentPlanningBook: String = "",
    var isToCreateActivity: Boolean = false,
    var isToUpdateActivity: Boolean = false,
    var activityName: String = "",
    var activityDesc: String = "",
    var activityNameError: Boolean = false,
    var activityNameErrorTxt: String = "",
    var activityDescError: Boolean = false,
    var activityDescErrorTxt: String = "",
    var activityStartHour: Int = 0,
    var activityStartMinute: Int = 0,
    var activityStartTimeFormatted: String = "",
    var activityStartTimeError: Boolean = false,
    var activityStartTimeErrorTxt: String = "",
    var openTimeStartDialog: Boolean = false,
    var openTimeEndDialog: Boolean = false,
    var activityEndHour: Int = 0,
    var activityEndMinute: Int = 0,
    var activityEndTimeFormatted: String = "",
    var activityEndTimeError: Boolean = false,
    var activityEndTimeErrorTxt: String = "",
    var activityBookList: MutableList<ActivityBook> = mutableListOf(),
    var isActivityBookListLoaded: Boolean = false,
    var activityBookSelectedId: String = "",
    var chipsSelectedList: MutableList<String> = mutableListOf(),
    var chipsSelectedMap: MutableMap<String, Boolean> = mutableMapOf(),
    var activityChipsError: Boolean = false,
    var activityChipsErrorTxt: String = ""
)

class ActivitiesManagerViewModel(private val activityService: ActivityService,
                                 private val stateService: StateService): ViewModel() {

    private val _uiState = MutableStateFlow(ActivitiesManagerUiState())
    val uiState: StateFlow<ActivitiesManagerUiState> = _uiState.asStateFlow()
    val activityName_maxLength = 30
    val activityDesc_maxLength = 100

    fun showActivityFormSection(action: Boolean) {
        updateIsToCreateActivity(false)
    }

    fun showActivityCreationSection(action: Boolean) {
        clearErrors()
        clearState()

        updateActivityStartTime(DateTimeUtils.currentHour(), 0, DateTimeUtils.currentTimeFormatted())
        updateActivityEndTime(DateTimeUtils.currentHour() + 1, 0, DateTimeUtils.currentTimeFormatted())
        updateIsToCreateActivity(action)
    }

    fun showActivityUpdateSection(activityBook: ActivityBook) {
        clearErrors()
        clearState()

        initChipsForUpdate(activityBook.weekDaysList)
        updateActivityBookSelectedId(activityBook.id!!)
        updateActivityName(activityBook.name)
        updateActivityDesc(activityBook.description ?: "")
        updateActivityStartTime(activityBook.startHour, activityBook.startMinute, DateTimeUtils.formatTime(activityBook.startHour, activityBook.startMinute))
        updateActivityEndTime(activityBook.endHour, activityBook.endMinute, DateTimeUtils.formatTime(activityBook.endHour, activityBook.endMinute))

        updateIsToUpdateActivity(true)
    }

    fun hideActivityUpdateSection() {
        clearErrors()
        clearState()

        updateIsToUpdateActivity(false)
    }

    fun loadActivities() {
        clearErrors()
        clearState()

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        Klog.line("ActivitiesManagerViewModel", "loadActivities", "${AppState.activePlanningBook!!.id}")
        updateCurrentPlanningBook(AppState.activePlanningBook!!.name)

        viewModelScope.launch {
            val resp = activityService.getActivityList(AppState.activePlanningBook!!.id, 1)
            Klog.line("ActivitiesManagerViewModel", "loadActivities", "resp=${resp.result}")
            if(resp.result) {
                updateActivityBookList(resp.activityBookList ?: mutableListOf())
                clearErrors()
                clearState()
                updateIsToCreateActivity(false)
                updateIsActivityBookListLoaded(true)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    fun selectChip(code: String, action: Boolean) {
        val exist = uiState.value.chipsSelectedList.contains(code)
        if(exist && !action) {
            uiState.value.chipsSelectedList.remove(code)
        }
        if(!exist && action) {
            uiState.value.chipsSelectedList.add(code)
        }
        uiState.value.chipsSelectedMap[code] = action
    }

    fun openTimeStartDi() {
        updateOpenTimeStartDialog(true)
    }

    fun closeTimeStartDi() {
        updateOpenTimeStartDialog(false)
    }

    fun openTimeEndDi() {
        updateOpenTimeEndDialog(true)
    }

    fun closeTimeEndDi() {
        updateOpenTimeEndDialog(false)
    }

    fun onTimeStartSelected(hour: Int, min: Int) {
        val timeFormatted = DateTimeUtils.formatTime(hour, min)
        updateActivityStartTime(hour, min, timeFormatted)
        updateOpenTimeStartDialog(false)
    }

    fun onTimeEndSelected(hour: Int, min: Int) {
        val timeFormatted = DateTimeUtils.formatTime(hour, min)
        updateActivityEndTime(hour, min, timeFormatted)
        updateOpenTimeEndDialog(false)
    }

    fun createActivity() {
        Klog.line("ActivitiesManagerViewModel", "createActivity", "-")

        if(!validateFields()) {
            Klog.linedbg("ActivitiesManagerViewModel", "createActivity", "Validation was unsuccessfull")
            return
        }
        Klog.linedbg("ActivitiesManagerViewModel", "createActivity", "Validation has been success")

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        val activityBook = fillActivityBookObj()

        viewModelScope.launch {
            val resp = activityService.createActivity(activityBook)
            Klog.line("ActivitiesManagerViewModel", "createActivity", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToCreateActivity(false)
                updateIsActivityBookListLoaded(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }

        Klog.linedbg("ActivitiesManagerViewModel", "createActivity", "is created")
    }

    fun updateActivity() {
        Klog.line("ActivitiesManagerViewModel", "updateActivity", "-")

        if(!validateFields()) {
            Klog.linedbg("ActivitiesManagerViewModel", "updateActivity", "Validation was unsuccessfull")
            return
        }
        Klog.linedbg("ActivitiesManagerViewModel", "updateActivity", "Validation has been success")

        if(AppState.activePlanningBook == null) {
            updateCurrentPlanningBook("You don't have any planning book yet")
            return
        }

        val activityBook = fillActivityBookObj()
        activityBook.id = uiState.value.activityBookSelectedId

        viewModelScope.launch {
            val resp = activityService.updateActivity(activityBook)
            Klog.line("ActivitiesManagerViewModel", "updateActivity", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToUpdateActivity(false)
                updateIsActivityBookListLoaded(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }

        Klog.linedbg("ActivitiesManagerViewModel", "updateActivity", "is created")
    }

    fun cloneActivity() {

    }

    private fun validateFields(): Boolean {
        clearErrors()

        val chainTxtValName = ChainTextValidator(
            TextValidatorLength(5, activityName_maxLength)
        )
        val chainTxtValDesc = ChainTextValidator(
            TextValidatorLength(5, activityDesc_maxLength)
        )
        val timeValidator = TimeGreaterValidator()

        val valResultName = chainTxtValName.validate(uiState.value.activityName.trim())
        val valResultDesc = chainTxtValDesc.validate(uiState.value.activityDesc.trim())
        val valResultTime = timeValidator.validate(uiState.value.activityStartHour,
                                                   uiState.value.activityEndHour,
                                                   uiState.value.activityStartMinute,
                                                   uiState.value.activityEndMinute)

        var result = true
        if(valResultName is ValidatorResult.Error) {
            updateActivityNameError(valResultName.message)
            result = false
        }
        if(valResultDesc is ValidatorResult.Error) {
            updateActivityDescError(valResultDesc.message)
            result = false
        }
        if(valResultTime is ValidatorResult.Error) {
            updateActivityEndTimeError(valResultTime.message)
            result = false
        }
        if(uiState.value.chipsSelectedList.size == 0) {
            updateActivityChipsError("Seleccione al menos un día")
            result = false
        }
        return result
    }

    private fun fillActivityBookObj(): ActivityBook {
        val result = ActivityBook(null,
            AppState.activePlanningBook!!.id,
            uiState.value.activityName,
            uiState.value.activityDesc,
            uiState.value.activityStartHour,
            uiState.value.activityStartMinute,
            uiState.value.activityEndHour,
            uiState.value.activityEndMinute,
            DateTimeUtils.sortWeekDaysList(uiState.value.chipsSelectedList),
            null)

        return result
    }

    private fun initChipsMap() {
        uiState.value.chipsSelectedMap[DateTimeUtils.LUNES] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.MARTES] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.MIERCOLES] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.JUEVES] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.VIERNES] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.SABADO] = false;
        uiState.value.chipsSelectedMap[DateTimeUtils.DOMINGO] = false;
    }

    private fun initChipsForUpdate(daysOfWeek: MutableList<String>) {
        Klog.linedbg("ActivitiesManagerViewModel", "initChipsMapForUpdate", "daysOfWeek: $daysOfWeek")
        Klog.linedbg("ActivitiesManagerViewModel", "initChipsMapForUpdate", "uiState.value.chipsSelectedList: ${uiState.value.chipsSelectedList}")
        Klog.linedbg("ActivitiesManagerViewModel", "initChipsMapForUpdate", "uiState.value.chipsSelectedMap: ${uiState.value.chipsSelectedMap}")

        daysOfWeek.forEach {it ->
            uiState.value.chipsSelectedMap[it] = true
            uiState.value.chipsSelectedList.add(it)
        }

        Klog.linedbg("ActivitiesManagerViewModel", "initChipsMapForUpdate", "uiState.value.chipsSelectedList *: ${uiState.value.chipsSelectedList}")
        Klog.linedbg("ActivitiesManagerViewModel", "initChipsMapForUpdate", "uiState.value.chipsSelectedMap *: ${uiState.value.chipsSelectedMap}")
    }

    private fun updateIsToCreateActivity(action: Boolean) {
        _uiState.update {
            it.copy(isToCreateActivity = action)
        }
    }

    private fun updateIsToUpdateActivity(action: Boolean) {
        _uiState.update {
            it.copy(isToUpdateActivity = action)
        }
    }


    private fun updateActivityBookSelectedId(id: String) {
        _uiState.update {
            it.copy(activityBookSelectedId = id)
        }
    }

    private fun updateCurrentPlanningBook(pbText: String) {
        _uiState.update {
            it.copy(currentPlanningBook = pbText)
        }
    }

    private fun updateIsActivityBookListLoaded(action: Boolean) {
        _uiState.update {
            it.copy(isActivityBookListLoaded = action)
        }
    }

    private fun updateActivityBookList(_activityBookList: MutableList<ActivityBook>) {
        _uiState.update {
            it.copy(activityBookList = _activityBookList)
        }
    }

    private fun updateOpenTimeStartDialog(action: Boolean) {
        _uiState.update {
            it.copy(openTimeStartDialog = action)
        }
    }

    private fun updateOpenTimeEndDialog(action: Boolean) {
        _uiState.update {
            it.copy(openTimeEndDialog = action)
        }
    }

    fun updateActivityName(txt: String) {
        if(txt.length <= activityName_maxLength) {
            _uiState.update {
                it.copy(activityName = txt)
            }
        }
    }

    private fun updateActivityNameError(txt: String) {
        _uiState.update {
            it.copy(activityNameErrorTxt = txt)
        }

        _uiState.update {
            it.copy(activityNameError = true)
        }
    }

    fun updateActivityDesc(txt: String) {
        if(txt.length < activityDesc_maxLength) {
            _uiState.update {
                it.copy(activityDesc = txt)
            }
        }
    }

    private fun updateActivityDescError(txt: String) {
        _uiState.update {
            it.copy(activityDescErrorTxt = txt)
        }

        _uiState.update {
            it.copy(activityDescError = true)
        }
    }

    private fun updateActivityChipsError(txt: String) {
        _uiState.update {
            it.copy(activityChipsErrorTxt = txt)
        }

        _uiState.update {
            it.copy(activityChipsError = true)
        }
    }

    private fun updateActivityStartTime(hour: Int, min: Int, timeFormatted: String) {
        _uiState.update {
            it.copy(activityStartHour = hour)
        }

        _uiState.update {
            it.copy(activityStartMinute = min)
        }

        _uiState.update {
            it.copy(activityStartTimeFormatted = timeFormatted)
        }
    }

    private fun updateActivityEndTime(hour: Int, min: Int, timeFormatted: String) {
        _uiState.update {
            it.copy(activityEndHour = hour)
        }

        _uiState.update {
            it.copy(activityEndMinute = min)
        }

        _uiState.update {
            it.copy(activityEndTimeFormatted = timeFormatted)
        }
    }

    private fun updateActivityStartTimeError(txt: String) {
        _uiState.update {
            it.copy(activityStartTimeErrorTxt = txt)
        }

        _uiState.update {
            it.copy(activityStartTimeError = true)
        }
    }

    private fun updateActivityEndTimeError(txt: String) {
        _uiState.update {
            it.copy(activityEndTimeErrorTxt = txt)
        }

        _uiState.update {
            it.copy(activityEndTimeError = true)
        }
    }

    private fun clearState() {
        updateActivityName("")
        updateActivityDesc("")
        updateActivityStartTime(0, 0, "")
        updateActivityEndTime(0, 0, "")
        updateActivityBookSelectedId("")
        uiState.value.chipsSelectedList = mutableListOf()
        initChipsMap()
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
            it.copy(activityNameErrorTxt = "")
        }
        _uiState.update {
            it.copy(activityNameError = false)
        }
        _uiState.update {
            it.copy(activityDescErrorTxt = "")
        }
        _uiState.update {
            it.copy(activityDescError = false)
        }
        _uiState.update {
            it.copy(activityChipsError =  false)
        }
        _uiState.update {
            it.copy(activityChipsErrorTxt =  "")
        }
        _uiState.update {
            it.copy(activityStartTimeError = false)
        }
        _uiState.update {
            it.copy(activityStartTimeErrorTxt = "")
        }
        _uiState.update {
            it.copy(activityEndTimeError = false)
        }
        _uiState.update {
            it.copy(activityEndTimeErrorTxt = "")
        }
    }

}