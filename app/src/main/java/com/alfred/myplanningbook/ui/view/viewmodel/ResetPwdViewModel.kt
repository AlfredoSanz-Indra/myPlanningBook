package com.alfred.myplanningbook.ui.view.viewmodel

import android.util.Patterns
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
data class ResetPwdUiState(
    val email: String = "",
    var emailError: Boolean = false,
    var emailErrorText: String = "",
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var emailSentAction: Boolean = false
)
class ResetPwdViewModel(val usersService: UsersService): ViewModel() {

    private val _uiState = MutableStateFlow(ResetPwdUiState())
    val uiState: StateFlow<ResetPwdUiState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update {
            it.copy(email = newEmail)
        }
    }

    fun sendResetEmail() {

        clearErrors()
        if(!validateFields()) {
            Klog.line("ResetPwdViewModel", "sendResetEmail", "Validation was unsuccessfull")
            return
        }
        Klog.line("ResetPwdViewModel", "sendResetEmail", "Validation has been success")

        viewModelScope.launch {
            val resp = usersService.sendResetEmail(uiState.value.email.trim())
            Klog.line("ResetPwdViewModel", "sendResetEmail", "resp: $resp")
            if(resp.result) {
                updateEmailSentAction(true)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun updateEmailSentAction(action: Boolean) {
        _uiState.update {
            it.copy(emailSentAction = action)
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


    private fun validateFields(): Boolean {

        val chainTxtValEmail = ChainTextValidator(
            TextValidatorLength(5, 50),
            TextValidatorEmail()
        )

        val valResultEmail = chainTxtValEmail.validate(uiState.value.email.trim())

        var result = true
        if(valResultEmail is ValidatorResult.Error) {
            setEmailError(valResultEmail.message)
            result = false
        }

        return result
    }

    private fun setEmailError(txt: String) {

        _uiState.update {
            it.copy(emailError = true)
        }
        _uiState.update {
            it.copy(emailErrorText = txt)
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
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }
}