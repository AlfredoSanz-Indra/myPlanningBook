package com.alfred.myplanningbook.ui.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorEmail
import com.alfred.myplanningbook.core.validators.TextValidatorEqualsFields
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.TextValidatorPassword
import com.alfred.myplanningbook.core.validators.ValidatorResult
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
data class RegisterUiState(
    val email: String = "",
    val pwd01: String = "",
    val pwd02: String = "",
    var emailError: Boolean = false,
    var emailErrorText: String = "",
    var pwdError: Boolean = false,
    var pwdErrorText: String = "",
    var pwd02Error: Boolean = false,
    var pwd02ErrorText: String = "",
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var registerAction: Boolean = false
)

class RegisterViewModel(val usersService: UsersService) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update {
            it.copy(email = newEmail)
        }
    }

    fun updatePwd01(newPwd01: String) {
        _uiState.update {
            it.copy(pwd01 = newPwd01)
        }
    }

    fun updatePwd02(newPwd02: String) {
        _uiState.update {
            it.copy(pwd02 = newPwd02)
        }
    }

    fun registerUser() {

        clearErrors()
        if(!validateFields()) {
            Klog.linedbg("RegisterViewModel", "registerUser", "Validation was unsuccessfull")
            return
        }
        Klog.linedbg("RegisterViewModel", "registerUser", "Validation has been success")

        viewModelScope.launch {
            val resp = usersService.newUserRegister(uiState.value.email.trim(), uiState.value.pwd01.trim())
            Klog.line("RegisterViewModel", "registerUser", "resp: $resp")
            if(resp.result) {
                updateRegisterAction(true);
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun updateRegisterAction(action: Boolean) {
        _uiState.update {
            it.copy(registerAction = action)
        }
    }

    private fun validateFields(): Boolean {

        val chainTxtValEmail = ChainTextValidator(
            TextValidatorLength(5, 50),
            TextValidatorEmail()
        )
        val chainTxtValPwd01 = ChainTextValidator(
            TextValidatorLength(8, 10),
            TextValidatorPassword()
        )
        val chainTxtValPwd02 = ChainTextValidator(
            TextValidatorEqualsFields(uiState.value.pwd02.trim())
        )

        val valResultEmail = chainTxtValEmail.validate(uiState.value.email.trim())
        val valResultPwd01 = chainTxtValPwd01.validate(uiState.value.pwd01.trim())
        val valResultPwd02 = chainTxtValPwd02.validate(uiState.value.pwd01.trim())

        var result = true
        if(valResultEmail is ValidatorResult.Error) {
            setEmailError(valResultEmail.message)
            result = false
        }
        if(valResultPwd01 is ValidatorResult.Error) {
            setPwdError(valResultPwd01.message)
            result = false
        }
        if(valResultPwd02 is ValidatorResult.Error) {
            setPwd02Error(valResultPwd02.message)
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

    private fun setPwd02Error(txt: String) {

        _uiState.update {
            it.copy(pwd02Error = true)
        }
        _uiState.update {
            it.copy(pwd02ErrorText = txt)
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
            it.copy(pwd02Error = false)
        }
        _uiState.update {
            it.copy(pwd02ErrorText = "")
        }
        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }
}