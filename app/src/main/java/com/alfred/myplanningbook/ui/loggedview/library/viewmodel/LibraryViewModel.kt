package com.alfred.myplanningbook.ui.loggedview.library.viewmodel

import androidx.lifecycle.ViewModel
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.resources.TheResources
import com.alfred.myplanningbook.domain.LibraryState
import com.alfred.myplanningbook.domain.model.library.LMaster
import com.alfred.myplanningbook.domain.model.library.LibraryMasters
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class LibraryUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var bookFieldErrorTxt: String = "",
    var headerMessage: String = "",
    var isDesiredBookListLoaded: Boolean = false,
    var isDesiredBookListLoading: Boolean = false,
    var isToAddBook: Boolean = false,
    var isToUpdateBook: Boolean = false,
    var bookTitleError: Boolean = false,
    var bookSubtitleError: Boolean = false,
    var bookNotesError: Boolean = false,
    var bookAuthorError: Boolean = false,
    var bookSagaError: Boolean = false,
    var bookPublisherError: Boolean = false,
    var bookCategoryError: Boolean = false,
    var bookLanguageError: Boolean = false,
    var bookFormatError: Boolean = false,
    var bookReadYearError: Boolean = false,
    var bookTitle: String = "",
    var bookSubtitle: String = "",
    var bookNotes: String = "",
    var bookAuthor: String = "",
    var bookSaga: String = "",
    var bookPublisher: String = "",
    var bookCategory: String = "",
    var bookLanguage: String = "",
    var bookFormat: String = "",
    var bookReadYear: String = "0",
    var formatsMaster: List<LMaster>? = listOf(),
    var languagesMaster: List<LMaster>? = listOf(),

)

class LibraryViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    val BOOK_TITLE_MAXLENGTH = 50
    val BOOK_TITLE_NOTES_MAXLENGTH = 150

    suspend fun loadDesiredBookList() {
        Klog.line("LibraryViewModel", "loadDesiredBookList", "loading desired books")

        updateHeaderMessage("Library")

        updateIsDesiredBookListLoaded(false)
        updateIsDesiredBookListLoading(true)

        Klog.line("LibraryViewModel", "loadDesiredBookList", "${_uiState.value.isDesiredBookListLoaded }")
        //TODO quitar
        delay(2000) //

        val libraryMaster_format = LibraryState.libraryMasters?.masters?.filter { it -> it.name == "format" }
        val libraryMaster_languages = LibraryState.libraryMasters?.masters?.filter { it -> it.name == "language" }

        _uiState.value.formatsMaster = libraryMaster_format?.get(0)?.values
        _uiState.value.languagesMaster = libraryMaster_languages?.get(0)?.values

        updateIsDesiredBookListLoaded(true)
        updateIsDesiredBookListLoading(false)

        Klog.line("LibraryViewModel", "loadDesiredBookList", "${_uiState.value.isDesiredBookListLoaded }")
    }

    fun showAddBook(action: Boolean) {
        updateIsToAddBook(action)
        val msg = if(action) "-> Adding Book" else ""
        updateHeaderMessage("Library $msg")
    }

    fun showUpdateBook(action: Boolean) {
        updateIsToUpdateBook(action)
        val msg = if(action) "-> Editing Book" else ""
        updateHeaderMessage("Library $msg")
    }

    fun openFilterBooksDi() {

    }

    fun createBook() {

    }

    fun updateBook() {

    }

    fun cloneBook() {

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

    private fun updateHeaderMessage(msg: String) {
        _uiState.update {
            it.copy(headerMessage = msg)
        }
    }

    private fun updateBookFieldErrorTxt(txt: String) {
        _uiState.update {
            it.copy(bookFieldErrorTxt = txt)
        }
    }

    private fun updateBookTitleError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookTitleError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookSubtitleError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookSubtitleError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookNotesError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookNotesError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookAuthorError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookAuthorError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookSagaError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookSagaError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookPublisherError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookPublisherError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookCategoryError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookCategoryError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookLanguageError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookLanguageError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    fun updateBookFormatError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookFormatError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    private fun updateBookReadYearError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookReadYearError = err)
        }

        updateBookFieldErrorTxt(txt)
    }

    fun updateBookTitle(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookTitle = txt)
            }
        }
    }

    fun updateBookSubtitle(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookSubtitle = txt)
            }
        }
    }

    fun updateBookNotes(txt: String) {
        if(txt.length <= BOOK_TITLE_NOTES_MAXLENGTH) {
            _uiState.update {
                it.copy(bookNotes = txt)
            }
        }
    }

    fun updateBookAuthor(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookAuthor = txt)
            }
        }
    }

    fun updateBookSaga(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookSaga = txt)
            }
        }
    }

    fun updateBookPublisher(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookPublisher = txt)
            }
        }
    }

    fun updateBookCategory(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookCategory = txt)
            }
        }
    }

    fun updateBookLanguage(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookLanguage = txt)
            }
        }
    }

    fun updateBookFormat(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookFormat = txt)
            }
        }
    }

    fun updateBookReadYear(txt: String) {
        if(txt.length <= 4) {
            _uiState.update {
                it.copy(bookReadYear = txt)
            }
        }
    }

    private fun clearState() {
        updateHeaderMessage("Library")
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
        _uiState.update {
            it.copy(bookFieldErrorTxt = "")
        }
        updateBookTitleError(false, "")
        updateBookSubtitleError(false, "")
        updateBookNotesError(false, "")
        updateBookAuthorError(false, "")
        updateBookSagaError(false, "")
        updateBookPublisherError(false, "")
        updateBookCategoryError(false, "")
        updateBookLanguageError(false, "")
        updateBookFormatError(false, "")
        updateBookReadYearError(false, "")
    }
}