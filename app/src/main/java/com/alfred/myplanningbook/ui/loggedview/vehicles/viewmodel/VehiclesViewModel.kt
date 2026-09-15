package com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.util.DateTimeUtils
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle
import com.alfred.myplanningbook.domain.usecaseapi.vehicle.VehicleService
import com.alfred.myplanningbook.ui.common.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.apply

/**
 * @author Alfredo Sanz
 * @time 2025
 */
data class VehiclesUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var flagInitial: Boolean = true,
    var isVehiclesLoading: Boolean = false,
    var headerMessage: String = "",
    var vehicleList: MutableList<Vehicle> = mutableStateListOf(),
    var isToAddVehicle: Boolean = false,
    var isToUpdateVehicle: Boolean = false,
    var isToMaintainVehicle: Boolean = false,
    var vehiclesLoadingMessage: String = "",
    var vehicleName: String = "",
    var vehicleModel: String = "",
    var vehicleNotes: String = "",
    var vehicleDate: Long = 0,
    var vehicleDateFormatted: String = "",
    var vehicleTerminationDate: Long? = null,
    var vehicleTerminationDateFormatted: String? = null,
    var vehicleToUpdate: Vehicle? = null
    )
class VehiclesViewModel(private val vehicleService: VehicleService): ViewModel() {
    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()

    val VEHICLE_NAME_MAXLENGTH = 50
    val VEHICLE_LONG_MAXLENGTH = 70

    fun init() {
        Klog.line("VehiclesViewModel", "init", "initializing view model")

        clearState()
        updateFlagInitial(false)
        updateHeaderMessage("Vehicles")

        loadVehicles()
    }

    private fun loadVehicles() {
        Klog.line("VehiclesViewModel", "loadVehicles", "loadVehicles")

        viewModelScope.launch {
            updateIsVehicleLoading(true)
            fetchVehiclesFromService()
            updateIsVehicleLoading(false)
        }
    }

    private suspend fun fetchVehiclesFromService() {
        Klog.line("VehiclesViewModel", "fetchVehiclesFromService", "fetching vehicles")

        val resp = vehicleService.getVehicles(AppState.useremail!!)
        if(resp.result) {
            updateVehicleList(resp.vehicleList ?: mutableStateListOf())
            uiState.value.vehicleList.forEach { Klog.line("VehiclesViewModel", "loadVehicles", "vehicle: $it") }
            clearErrors()
        }
        else {
            Klog.line("VehiclesViewModel", "fetchVehiclesFromService", "error")
            setGeneralError("${resp.code}: ${resp.message}")
        }
    }

    fun showNewVehicle(action: Boolean) {
        Klog.line("VehiclesViewModel", "showNewVehicle", "showing new vehicle section action: $action")

        this.updateIsToAddVehicle(action)
        clearForm()
        clearErrors()

        val msg = if(action) "-> Add" else ""
        updateHeaderMessage("Vehicles $msg")

        updateVehicleDate(DateTimeUtils.currentDate())
        updateVehicleDateFormatted(DateTimeUtils.currentDateFormatted())

        if(!action) {
            return
        }
    }

    fun saveNewVehicle() {
        Klog.linedbg("VehiclesViewModel", "saveNewVehicle", "save new vehicle")
        updateVehiclesLoadingMessage("Saving Vehicle")
        updateIsVehiclesLoading(true)

        val validateResult = validateFields()
        if(!validateResult.result) {
            updateGeneralError(true, "The field ${validateResult.field} ${validateResult.message}")
            updateIsVehiclesLoading(false)
            return
        }
        Klog.linedbg("VehiclesViewModel", "saveNewVehicle", "Validation has been success")

        val veh: Vehicle = fillObj()
        Klog.linedbg("VehiclesViewModel", "saveNewVehicle", "Veh: $veh")

        viewModelScope.launch {
            val resp = vehicleService.createVehicle(veh, AppState.useremail!!)
            Klog.line("VehiclesViewModel", "saveNewVehicle", "resp: $resp")
            if(resp.result) {
                clearErrors()
                fetchVehiclesFromService()
                clearState()
            }
            else {
                updateGeneralError(true, "${resp.code}: ${resp.message}")
                Klog.linedbg("VehiclesViewModel", "saveNewVehicle", "error saving new Vehicle")
            }
            updateIsVehiclesLoading(false)
        }
    }

    fun showUpdateVehicle(action: Boolean, vehicle: Vehicle? = null) {
        Klog.linedbg("VehiclesViewModel", "showUpdateVehicle", "updating vehicle action: $action")

        this.updateIsToUpdateVehicle(action)
        clearForm()
        clearErrors()

        val msg = if(action) "-> Edit" else ""
        updateHeaderMessage("Vehicles $msg")

        if(action && vehicle != null) {
            getVehicle(vehicle)
        }
    }

    private fun getVehicle(vehicle: Vehicle) {
        Klog.linedbg("VehiclesViewModel", "getVehicle", "getting vehicle: $vehicle")

        val vehicleId = vehicle.id
        if (vehicleId.isNullOrBlank()) {
            Klog.line("VehiclesViewModel", "getVehicle", "Error: vehicle.id is null or blank")
            updateGeneralError(true, "Error: The vehicle ID is missing.")
            return
        }

        updateIsVehiclesLoading(true)

        viewModelScope.launch {
            val resp = vehicleService.getVehicle(AppState.useremail!!, vehicle.id!!)
            Klog.line("VehiclesViewModel", "getVehicle", "resp: $resp")
            if(resp.result) {
                clearErrors()
                resp.vehicle?.let { v ->
                    updateVehicleName(v.name)
                    updateVehicleModel(v.model ?: "")
                    updateVehicleNotes(v.notes ?: "")
                    updateVehicleDate(v.dateInMillis)
                    updateVehicleDateFormatted(DateTimeUtils.dateToDateString(v.dateInMillis))
                    v.terminationDateInMillis?.let { tDate ->
                        updateVehicleTerminationDate(tDate)
                        updateVehicleTerminationDateFormatted(DateTimeUtils.dateToDateString(tDate))
                    }
                }
                updateVehicleToUpdate(resp.vehicle)
            }
            else {
                updateGeneralError(true, "${resp.code}: ${resp.message}")
                Klog.linedbg("VehiclesViewModel", "getVehicle", "error getting vehicle")
            }
            updateIsVehiclesLoading(false)
        }
    }

    fun updateVehicle() {
        Klog.linedbg("VehiclesViewModel", "updateVehicle", "updating vehicle")
        updateVehiclesLoadingMessage("Updating Vehicle")
        updateIsVehiclesLoading(true)

        val validateResult = validateFields()
        if(!validateResult.result) {
            updateGeneralError(true, "The field ${validateResult.field} ${validateResult.message}")
            updateIsVehiclesLoading(false)
            return
        }
        Klog.linedbg("VehiclesViewModel", "updateVehicle", "Validation has been success")

        val veh: Vehicle = fillVehicleObject()
        Klog.linedbg("VehiclesViewModel", "updateVehicle", "Veh: $veh")

        viewModelScope.launch {
            val resp = vehicleService.updateVehicle(veh, AppState.useremail!!)
            Klog.line("VehiclesViewModel", "updateVehicle", "resp: $resp")
            if(resp.result) {
                clearErrors()
                fetchVehiclesFromService()
                clearState()
            }
            else {
                updateGeneralError(true, "${resp.code}: ${resp.message}")
                Klog.linedbg("VehiclesViewModel", "updateVehicle", "error updating Vehicle")
            }
            updateIsVehiclesLoading(false)
        }
    }

    private fun fillVehicleObject(): Vehicle {
        val result = Vehicle(
            id = uiState.value.vehicleToUpdate?.id,
            name = uiState.value.vehicleName,
            model = uiState.value.vehicleModel.takeIf { it.isNotBlank() }?.trim(),
            notes = uiState.value.vehicleNotes.takeIf { it.isNotBlank() }?.trim(),
            dateInMillis = uiState.value.vehicleDate,
            year = DateTimeUtils.dateToYear(uiState.value.vehicleDate),
            month = DateTimeUtils.dateToMonth(uiState.value.vehicleDate),
            day = DateTimeUtils.dateToDay(uiState.value.vehicleDate)
        ).apply {
            uiState.value.vehicleTerminationDate?.takeIf { it > 0L }?.let { date ->
                terminationDateInMillis = date
                terminationYear = DateTimeUtils.dateToYear(date)
                terminationMonth = DateTimeUtils.dateToMonth(date)
                terminationDay = DateTimeUtils.dateToDay(date)
            }
        }
        return result
    }

    private fun validateFields(): ValidationResult {
        var result = ValidationResult(true, "", "")
        clearErrors()

        //----VALIDATORS----
        val chainTxtShort = ChainTextValidator(
            TextValidatorLength(2, VEHICLE_NAME_MAXLENGTH)
        )
        val chainTxtLong = ChainTextValidator(
            TextValidatorLength(2, VEHICLE_LONG_MAXLENGTH)
        )

        //----VALIDATIONS----
        val valResultName = chainTxtShort.validate(uiState.value.vehicleName.trim())

        var valResultModel: ValidatorResult = ValidatorResult.Success
        if(uiState.value.vehicleModel.isNotBlank()) {
            valResultModel = chainTxtLong.validate(uiState.value.vehicleModel.trim())
        }

        var valResultNotes: ValidatorResult = ValidatorResult.Success
        if(uiState.value.vehicleNotes.isNotBlank()) {
            valResultNotes = chainTxtLong.validate(uiState.value.vehicleNotes.trim())
        }

        var valResultDate: ValidatorResult = ValidatorResult.Success
        if(uiState.value.vehicleDate == 0L) {
            valResultDate = ValidatorResult.Error("Date is mandatory")
        }

        //----CHECK VALIDATION RESULTS----
        if(valResultName is ValidatorResult.Error) {
            result = ValidationResult(false, "Vehicle", valResultName.message)
        }
        if(valResultModel is ValidatorResult.Error) {
            result = ValidationResult(false, "Model", valResultModel.message)
        }
        if(valResultNotes is ValidatorResult.Error) {
            result = ValidationResult(false, "Notes", valResultNotes.message)
        }
        if(valResultDate is ValidatorResult.Error) {
            result = ValidationResult(false, "Date", valResultDate.message)
        }

        return result
    }

    private fun fillObj(): Vehicle {
        val result = Vehicle(
            id =null,
            name = uiState.value.vehicleName!!,
            model = uiState.value.vehicleModel.takeIf { it.isNotBlank() }?.trim(),
            notes = uiState.value.vehicleNotes.takeIf { it.isNotBlank() }?.trim(),
            dateInMillis = uiState.value.vehicleDate,
            year = DateTimeUtils.dateToYear(uiState.value.vehicleDate),
            month = DateTimeUtils.dateToMonth(uiState.value.vehicleDate),
            day = DateTimeUtils.dateToDay(uiState.value.vehicleDate)
        ).apply {
            uiState.value.vehicleTerminationDate?.let { date ->
                terminationDateInMillis = date
                terminationYear = DateTimeUtils.dateToYear(date)
                terminationMonth = DateTimeUtils.dateToMonth(date)
                terminationDay = DateTimeUtils.dateToDay(date)
            }
        }

        return result
    }

    fun onDateSelected(dateInMill: Long) {
        val dateFormatted = DateTimeUtils.dateToDateString(dateInMill)
        updateVehicleDate(dateInMill)
        updateVehicleDateFormatted(dateFormatted)
    }

    fun onTerminationDateSelected(dateInMill: Long) {
        val dateFormatted = DateTimeUtils.dateToDateString(dateInMill)
        updateVehicleTerminationDate(dateInMill)
        updateVehicleTerminationDateFormatted(dateFormatted)
    }

    fun clearForm() {
        updateVehicleName("")
        updateVehicleModel("")
        updateVehicleNotes("")
        updateVehicleDate(0L)
        updateVehicleDateFormatted("")
        updateVehicleTerminationDate(0L)
        updateVehicleTerminationDateFormatted("")
    }

    private fun updateVehicleList(list: List<Vehicle>) {
        _uiState.update {
            it.copy(vehicleList = list.toMutableStateList())
        }
    }

    private fun updateFlagInitial(flag: Boolean) {
        _uiState.update {
            it.copy(flagInitial = flag)
        }
    }

    private fun updateIsVehicleLoading(flag: Boolean) {
        _uiState.update {
            it.copy(isVehiclesLoading = flag)
        }
    }

    fun updateVehicleDate(date: Long) {
        _uiState.update {
            it.copy(vehicleDate = date)
        }
    }

    fun updateVehicleDateFormatted(date: String) {
        _uiState.update {
            it.copy(vehicleDateFormatted = date)
        }
    }

    fun updateVehicleTerminationDate(date: Long) {
        _uiState.update {
            it.copy(vehicleTerminationDate = date)
        }
    }

    fun updateVehicleTerminationDateFormatted(date: String) {
        _uiState.update {
            it.copy(vehicleTerminationDateFormatted = date)
        }
    }

    fun updateVehicleName(text: String) {
        _uiState.update {
            it.copy(vehicleName = text)
        }
    }

    fun updateVehicleModel(text: String) {
        _uiState.update {
            it.copy(vehicleModel = text)
        }
    }

    fun updateVehicleNotes(text: String) {
        _uiState.update {
            it.copy(vehicleNotes = text)
        }
    }

    private fun updateIsVehiclesLoading(flag: Boolean) {
        _uiState.update {
            it.copy(isVehiclesLoading = flag)
        }
    }

    private fun updateVehiclesLoadingMessage(text: String) {
        _uiState.update {
            it.copy(vehiclesLoadingMessage = text)
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

    private fun updateGeneralError(state: Boolean, text: String) {
        _uiState.update {
            it.copy(generalError = state)
        }
        _uiState.update {
            it.copy(generalErrorText = text)
        }
    }

    private fun updateIsToAddVehicle(action: Boolean) {
        _uiState.update {
            it.copy(isToAddVehicle = action)
        }
    }

    private fun updateIsToUpdateVehicle(action: Boolean) {
        _uiState.update {
            it.copy(isToUpdateVehicle = action)
        }
    }

    private fun updateVehicleToUpdate(vehicle: Vehicle?) {
        _uiState.update {
            it.copy(vehicleToUpdate = vehicle)
        }
    }

    private fun updateIsToMaintainVehicle(action: Boolean) {
        _uiState.update {
            it.copy(isToMaintainVehicle = action)
        }
    }

    private fun updateHeaderMessage(msg: String) {
        _uiState.update {
            it.copy(headerMessage = msg)
        }
    }

    private fun clearState() {
        updateHeaderMessage("Vehicles")
        updateIsVehiclesLoading(false)
        updateVehiclesLoadingMessage("")
        updateVehicleModel("")
        updateVehicleNotes("")
        updateVehicleName("")
        updateVehicleDate(0L)
        updateHeaderMessage("")
        updateIsToMaintainVehicle(false)
        updateIsToAddVehicle(false)
        updateIsToUpdateVehicle(action = false)
        updateVehicleToUpdate(null)
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