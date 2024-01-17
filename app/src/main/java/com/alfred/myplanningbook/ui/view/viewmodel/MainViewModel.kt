package com.alfred.myplanningbook.ui.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
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

data class MainUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var isToLogin: Boolean = false,
    var isToBook: Boolean = false
)

class MainViewModel(val usersService: UsersService) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun loginAction() {

        clearFields()
        clearErrors()

        viewModelScope.launch {
            var result = false

            if(FirebaseSession.isUserSigned()) {
                if(FirebaseSession.isUserSignedAndValidated()) {
                    Klog.line("MainViewModel", "loginAction", "user is signed and email validated")
                    AppState.setUserEmail(FirebaseSession.auth.currentUser?.email)
                    result = true
                }
                else {
                    Klog.line("MainViewModel", "loginAction", "user email is not validated yet")
                }
            }
            else {
                Klog.line("MainViewModel", "loginAction", "user is not signed")
            }

            if(result) {
                updateIsToBook(true)
            }
            else {
                updateIsToLogin(true)
            }
        }//launch
    }

    private fun setGeneralError(txt: String) {
        _uiState.update {
            it.copy(generalError = true)
        }
        _uiState.update {
            it.copy(generalErrorText = txt)
        }
    }

    private fun updateIsToLogin(action: Boolean) {
        _uiState.update {
            it.copy(isToLogin = action)
        }
    }

    private fun updateIsToBook(action: Boolean) {
        _uiState.update {
            it.copy(isToBook = action)
        }
    }


    private fun clearErrors() {
        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }

    fun clearFields() {

        _uiState.update {
            it.copy(isToLogin = false)
        }

        _uiState.update {
            it.copy(isToBook = false)
        }
    }
}