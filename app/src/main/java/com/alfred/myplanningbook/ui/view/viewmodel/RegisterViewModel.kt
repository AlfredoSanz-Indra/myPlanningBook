package com.alfred.myplanningbook.ui.view.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
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

        if(!validateLengths() ||
           !validatePasswords() ||
           !validateEmail()) {

            return
        }

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

    private fun validateLengths(): Boolean {

        var result = true

        if(uiState.value.email.trim().isEmpty()) {
            setEmailError("Enter a email")
            result = false
        }

        if(uiState.value.pwd01.trim().length < 8 ||
           uiState.value.pwd02.trim().length < 8) {

            setPwdError("Password 8 characters min")
            result = false
        }

        return result
    }

    private fun validateEmail(): Boolean {

        var result = true

        if(!Patterns.EMAIL_ADDRESS.matcher(uiState.value.email.trim()).matches()) {
            setEmailError("Enter a valid email")
            result = false
        }

        return result
    }

    private fun validatePasswords(): Boolean {

        var result = true

        if(uiState.value.pwd02.trim().length != uiState.value.pwd01.trim().length) {
            setPwdError("Passwords must be equal")
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