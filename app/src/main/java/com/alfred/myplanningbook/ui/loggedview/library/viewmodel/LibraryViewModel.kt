package com.alfred.myplanningbook.ui.loggedview.library.viewmodel

import androidx.lifecycle.ViewModel
import com.alfred.myplanningbook.core.log.Klog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class LibraryUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var isDesiredBookListLoaded: Boolean = false,
    var isDesiredBookListLoading: Boolean = false,
    var isToAddBook: Boolean = false,
    var isToUpdateBook: Boolean = false,
)

class LibraryViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()


    suspend fun loadDesiredBookList() {
        Klog.line("LibraryViewModel", "loadDesiredBookList", "loading desired books")

        updateIsDesiredBookListLoaded(false)
        updateIsDesiredBookListLoading(true)

        Klog.line("LibraryViewModel", "loadDesiredBookList", "${_uiState.value.isDesiredBookListLoaded }")
        //TODO quitar
        delay(2000) //
        runBlocking {
            delay(1.toDuration(DurationUnit.SECONDS))
        }
        updateIsDesiredBookListLoaded(true)
        updateIsDesiredBookListLoading(false)

        Klog.line("LibraryViewModel", "loadDesiredBookList", "${_uiState.value.isDesiredBookListLoaded }")
    }

    fun showAddBook(action: Boolean) {

    }

    fun openFilterBooksDi() {

    }


    private fun updateIsDesiredBookListLoaded(action: Boolean) {
        _uiState.update {
            it.copy(isDesiredBookListLoaded = action)
        }
    }

    private fun updateIsDesiredBookListLoading(action: Boolean) {
        _uiState.update {
            it.copy(isDesiredBookListLoading = action)
        }
    }

    private fun updateIsToAddBook(action: Boolean) {
        _uiState.update {
            it.copy(isToAddBook = action)
        }
    }

    private fun updateIsToUpdateBook(action: Boolean) {
        _uiState.update {
            it.copy(isToUpdateBook = action)
        }
    }

    private fun clearState() {

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