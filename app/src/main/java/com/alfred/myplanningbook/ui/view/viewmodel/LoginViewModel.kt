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

    fun loginUser() {

        clearErrors()

        if(!validateLengths() ||
            !validateEmail()) {

            return
        }

        viewModelScope.launch {
            val resp = usersService.logginUser(uiState.value.email.trim(), uiState.value.pwd.trim())
            Klog.line("LoginViewModel", "loginUser", "resp: $resp")
            if(resp.result) {
                updateLoginAction(true);
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun updateLoginAction(action: Boolean) {
        _uiState.update {
            it.copy(loginAction = action)
        }
    }

    private fun validateLengths(): Boolean {

        var result = true

        if(uiState.value.email.trim().isEmpty()) {
            setEmailError("Enter a email")
            result = false
        }

        if(uiState.value.pwd.trim().length < 8) {

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