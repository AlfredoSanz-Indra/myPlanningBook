package com.alfred.myplanningbook.ui.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Alfredo Sanz
 * @time 2023
 */

data class LoginUiState(
    val email: String = "",
    val pwd: String = "",
    var emailError: Boolean = false,
    var emailErrorText: String = "",
    var pwdError: Boolean = false,
    var pwdErrorText: String = "",
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var isToLogin: Boolean = false
)


class LoginViewModel(val usersService: UsersService): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update {
            it.copy(email = newEmail)
        }
    }

    fun updatePwd(newPwd01: String) {
        _uiState.update {
            it.copy(pwd = newPwd01)
        }
    }

    fun loginUser() {

        clearFields()
        clearErrors()

        if(!validateFields()) {
            Klog.line("LoginViewModel", "loginUser", "Validation was unsuccessfull")
            return
        }

        viewModelScope.launch {
            try {
                val resp = usersService.logginUser(uiState.value.email.trim(), uiState.value.pwd.trim())
                Klog.linedbg("LoginViewModel", "loginUser", "resp: $resp")
                if(resp.result) {
                    AppState.setUserEmail(uiState.value.email.trim())
                    updateIsToLogin(true);
                }
                else {
                    setGeneralError(" ${resp.code}: ${resp.message}")
                }
            }
            catch (e: Exception) {
                Klog.stackTrace("LoginViewModel", "loginUser", e.stackTrace)
                Klog.line("LoginViewModel","loginUser"," Exception localizedMessage: ${e.localizedMessage}")
                setGeneralError(" 500: ${e.message}, please make login again!")
            }
        }
    }

    private fun updateIsToLogin(action: Boolean) {
        _uiState.update {
            it.copy(isToLogin = action)
        }
    }

    private fun validateFields(): Boolean {

        val chainTxtValEmail = ChainTextValidator(
            TextValidatorLength(5, 50)
        )
        val chainTxtValPwd01 = ChainTextValidator(
            TextValidatorLength(8, 10)
        )
        val valResultEmail = chainTxtValEmail.validate(uiState.value.email.trim())
        val valResultPwd01 = chainTxtValPwd01.validate(uiState.value.pwd.trim())

        var result = true

        if(valResultEmail is ValidatorResult.Error) {
            setEmailError(valResultEmail.message)
            result = false
        }
        if(valResultPwd01 is ValidatorResult.Error) {
            setPwdError(valResultPwd01.message)
            result = false
        }

        return result
    }

    private fun setGeneralError(txt: String) {

        _uiState.update {
            it.copy(generalError = true)
        }
        _uiState.update {
            it.copy(generalErrorText = txt)
        }
    }

    private fun setEmailError(txt: String) {

        _uiState.update {
            it.copy(emailError = true)
        }
        _uiState.update {
            it.copy(emailErrorText = txt)
        }
    }

    private fun setPwdError(txt: String) {

        _uiState.update {
            it.copy(pwdError = true)
        }
        _uiState.update {
            it.copy(pwdErrorText = txt)
        }
    }

    private fun clearErrors() {

        _uiState.update {
            it.copy(emailError = false)
        }
        _uiState.update {
            it.copy(emailErrorText = "")
        }
        _uiState.update {
            it.copy(pwdError = false)
        }
        _uiState.update {
            it.copy(pwdErrorText = "")
        }
        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }

    private fun clearFields() {

        _uiState.update {
            it.copy(isToLogin = false)
        }
    }
}