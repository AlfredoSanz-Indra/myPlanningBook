package com.alfred.myplanningbook.ui.loggedview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookListUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var logoutAction: Boolean = false
)
class BookListViewModel(val usersService: UsersService): ViewModel()  {

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    fun logoutUser() {

        clearErrors()

        viewModelScope.launch {
            val resp = usersService.logoutUser()
            Klog.line("BookListViewModel", "loginUser", "resp: $resp")
            if(resp.result) {
                updateLogoutAction(true);
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }
    }

    private fun updateLogoutAction(action: Boolean) {
        _uiState.update {
            it.copy(logoutAction = action)
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

    private fun clearErrors() {

        _uiState.update {
            it.copy(generalError = false)
        }
        _uiState.update {
            it.copy(generalErrorText = "")
        }
    }
}