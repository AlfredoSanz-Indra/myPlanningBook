package com.alfred.myplanningbook.ui.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    var loginAction: Boolean = false
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

    suspend fun loginUser(): Boolean {

        var result: Boolean
        clearErrors()

        if(!validateFields()) {
            Klog.line("LoginViewModel", "loginUser", "Validation was unsuccessfull")
            return false
        }

        val defer = viewModelScope.async {
            val resp = usersService.logginUser(uiState.value.email.trim(), uiState.value.pwd.trim())
            Klog.line("LoginViewModel", "loginUser", "resp: $resp")
            var r = false
            if(resp.result) {
                updateLoginAction(true);
                AppState.setUserEmail(uiState.value.email.trim())
                r = true
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }

            return@async r
        }

        result = defer.await()
        return result
    }

    private fun updateLoginAction(action: Boolean) {
        _uiState.update {
            it.copy(loginAction = action)
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
}