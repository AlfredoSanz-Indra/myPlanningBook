package com.alfred.myplanningbook.ui.loggedview.library.viewmodel

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.validators.ChainTextValidator
import com.alfred.myplanningbook.core.validators.TextValidatorLength
import com.alfred.myplanningbook.core.validators.TextValidatorOnlyNumber
import com.alfred.myplanningbook.core.validators.ValidatorResult
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.LibraryState
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.LMaster
import com.alfred.myplanningbook.domain.usecaseapi.library.LibraryService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class LibraryUiState(
    var generalError: Boolean = false,
    var generalErrorText: String = "",
    var headerMessage: String = "",
    var isDesiredBookListLoaded: Boolean = false,
    var isDesiredBookListLoading: Boolean = false,
    var isBookActionWorking: Boolean = false,
    var isToAddBook: Boolean = false,
    var isToUpdateBook: Boolean = false,
    var isToFilterBooks: Boolean = false,
    var isToDeleteBook: Boolean = false,
    var bookTitleError: Boolean = false,
    var bookSubtitleError: Boolean = false,
    var bookNotesError: Boolean = false,
    var bookAuthorError: Boolean = false,
    var bookSagaError: Boolean = false,
    var bookSagaIndexError: Boolean = false,
    var bookPublisherError: Boolean = false,
    var bookCategoryError: Boolean = false,
    var bookLanguageError: Boolean = false,
    var bookFormatError: Boolean = false,
    var bookReadYearError: Boolean = false,
    var bookTitleErrorTxt: String = "",
    var bookSubtitleErrorTxt: String = "",
    var bookNotesErrorTxt: String = "",
    var bookAuthorErrorTxt: String = "",
    var bookSagaErrorTxt: String = "",
    var bookSagaIndexErrorTxt: String = "",
    var bookPublisherErrorTxt: String = "",
    var bookCategoryErrorTxt: String = "",
    var bookReadYearErrorTxt: String = "",
    var bookTitle: String = "",
    var bookRead: String = "n",
    var bookHave: String = "n",
    var bookSubtitle: String = "",
    var bookNotes: String = "",
    var bookAuthor: String = "",
    var bookSaga: String = "",
    var bookSagaIndex: String = "",
    var bookPublisher: String = "",
    var bookCategory: String = "",
    var bookLanguage: String = "",
    var bookLanguageCode: String = "",
    var bookFormat: String = "",
    var bookFormatCode: String = "",
    var bookReadYear: String = "",
    var formatsMaster: MutableList<LMaster>? = mutableListOf(),
    var formatsMasterFilter: MutableList<LMaster>? = mutableListOf(),
    var languagesMaster: MutableList<LMaster>? = mutableListOf(),
    var languagesMasterFilter: MutableList<LMaster>? = mutableListOf(),
    var bookToDelete: Book? = null,
    var bookList: MutableList<Book> = mutableListOf(),
)

class LibraryViewModel(private val libraryService: LibraryService): ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    val BOOK_TITLE_MAXLENGTH = 50
    val BOOK_AUTHOR_MAXLENGTH = 50
    val BOOK_NOTES_MAXLENGTH = 150
    val BOOK_YEAR_MAXLENGTH = 4
    val BOOK_SAGA_NUMBER_MAXLENGTH = 2

    fun loadDesiredBookList() {
        Klog.line("LibraryViewModel", "loadDesiredBookList", "loading desired books")
        updateIsDesiredBookListLoaded(false)

        updateHeaderMessage("Library")

        if(_uiState.value.isDesiredBookListLoading) {
            return
        }

        val masterTodos = LMaster("todos", "Todos")
        if(_uiState.value.formatsMaster.isNullOrEmpty()) {
            val libraryMasterFormat = LibraryState.libraryMasters?.masters?.filter { it -> it.name == "format" }
            updateFormatMaster(libraryMasterFormat?.get(0)?.values?.toMutableList() ?: mutableListOf())

            val libraryMasterFormatFilter = libraryMasterFormat?.get(0)?.values?.toMutableList()
            libraryMasterFormatFilter?.add(0, masterTodos)
            updateFormatMasterFilter(libraryMasterFormatFilter ?: mutableListOf())
        }
        if(_uiState.value.languagesMaster.isNullOrEmpty()) {
            val libraryMasterLanguages = LibraryState.libraryMasters?.masters?.filter { it -> it.name == "language" }
            updateLanguagesMaster(libraryMasterLanguages?.get(0)?.values?.toMutableList() ?: mutableListOf())

            val libraryMasterLanguagesFilter = libraryMasterLanguages?.get(0)?.values?.toMutableList()
            libraryMasterLanguagesFilter?.add(0, masterTodos)
            updateLanguagesMasterFilter(libraryMasterLanguagesFilter ?: mutableListOf())
        }

        val filterBook = fillEmptyBookObj()
        updateIsDesiredBookListLoading(true)
        searchBooks(filterBook)
    }

    fun showAddBook(action: Boolean) {
        updateIsToAddBook(action)
        val msg = if(action) "-> Adding Book" else ""
        updateHeaderMessage("Library $msg")

        clearForm()
        clearErrors()
    }

    fun showUpdateBook(action: Boolean, book: Book?) {
        updateIsToUpdateBook(action)
        val msg = if(action) "-> Editing Book" else ""
        updateHeaderMessage("Library $msg")

        clearForm()
        clearErrors()
    }

    fun showFilterBooks(action: Boolean) {
        updateIsToFilterBooks(action)
        val msg = if(action) "-> Filter" else ""
        updateHeaderMessage("Library $msg")

        clearForm()
        clearErrors()
        updateBookFormat(uiState.value.formatsMasterFilter?.get(0)?.value ?: "No data")
        updateBookFormatCode(uiState.value.formatsMasterFilter?.get(0)?.name ?: "")
        updateBookLanguage(uiState.value.languagesMasterFilter?.get(0)?.value ?: "No data")
        updateBookLanguageCode(uiState.value.languagesMasterFilter?.get(0)?.name ?: "")
        updateBookRead("t")
        updateBookHave("t")
    }

    private fun clearForm() {
        updateBookFormat(uiState.value.formatsMaster?.get(0)?.value ?: "No data")
        updateBookFormatCode(uiState.value.formatsMaster?.get(0)?.name ?: "")
        updateBookLanguage(uiState.value.languagesMaster?.get(0)?.value ?: "No data")
        updateBookLanguageCode(uiState.value.languagesMaster?.get(0)?.name ?: "")

        updateBookTitle("")
        updateBookSubtitle("")
        updateBookAuthor("")
        updateBookNotes("")
        updateBookSaga("")
        updateBookSagaIndex("")
        updateBookNotes("")
        updateBookPublisher("")
        updateBookCategory("")
        updateBookReadYear("")
        updateBookRead("n")
        updateBookHave("n")
    }

    fun createBook() {
        Klog.line("LibraryViewModel", "createBook", "-")
        updateBookActionWorking(true)

        if(!validateFields()) {
            Klog.linedbg("LibraryViewModel", "createBook", "Validation was unsuccessfully")
            updateBookActionWorking(false)
            return
        }
        Klog.linedbg("LibraryViewModel", "createBook", "Validation has been success")

        if(AppState.useremail.isNullOrBlank()) {
            setGeneralError("You're not currently logged in")
            updateBookActionWorking(false)
            return
        }

        val book = fillBookObj()

        viewModelScope.launch {
            val resp = libraryService.createBook(book, AppState.useremail!!)
            Klog.line("LibraryViewModel", "createBook", "resp: $resp")
            if(resp.result) {
                clearErrors()
                clearState()
                updateIsToAddBook(false)
            }
            else {
                setGeneralError(" ${resp.code}: ${resp.message}")
            }
        }

        updateBookActionWorking(false)
        Klog.linedbg("LibraryViewModel", "createBook", "is created")
    }

    fun updateBook() {

    }

    fun cloneBook() {

    }

    fun filterBooks() {
        Klog.line("LibraryViewModel", "filterBooks", "-")
        updateIsDesiredBookListLoaded(false)

        if(!validateFields()) {
            Klog.linedbg("LibraryViewModel", "filterBooks", "Validation was unsuccessfully")
            updateIsDesiredBookListLoaded(true)
            return
        }
        Klog.linedbg("LibraryViewModel", "filterBooks", "Validation has been success")

        if(AppState.useremail.isNullOrBlank()) {
            setGeneralError("You're not currently logged in")
            updateIsDesiredBookListLoaded(true)
            return
        }

        val book = fillBookObj()
        updateIsDesiredBookListLoading(true)
        updateIsToFilterBooks(false)
        Klog.linedbg("LibraryViewModel", "filterBooks", "book->$book")

        searchBooks(book)
    }

    private fun validateFields(): Boolean {
        clearErrors()

        val chainTxtAuthor = ChainTextValidator(
            TextValidatorLength(2, BOOK_AUTHOR_MAXLENGTH)
        )
        val chainTxtTitleFields = ChainTextValidator(
            TextValidatorLength(2, BOOK_TITLE_MAXLENGTH)
        )
        val chainTxtNoteFields = ChainTextValidator(
            TextValidatorLength(2, BOOK_NOTES_MAXLENGTH)
        )
        val chainTxtYear = ChainTextValidator(
            TextValidatorLength(4, BOOK_YEAR_MAXLENGTH)
        )
        val chainTxtSagaIndex = ChainTextValidator(
            TextValidatorLength(1, BOOK_SAGA_NUMBER_MAXLENGTH)
        )
        val chainTxtIsOnlyNumber = ChainTextValidator(
            TextValidatorOnlyNumber()
        )

        var valResultAuthor: ValidatorResult
        var valResultTitle: ValidatorResult
        if(uiState.value.isToFilterBooks) {
            valResultAuthor = if(uiState.value.bookAuthor.isNotBlank()) {
                chainTxtAuthor.validate(uiState.value.bookAuthor.trim())
            }
            else {
                ValidatorResult.NoResult
            }
            valResultTitle = if(uiState.value.bookTitle.isNotBlank()) {
                chainTxtTitleFields.validate(uiState.value.bookTitle.trim())
            }
            else {
                ValidatorResult.NoResult
            }
        }
        else {
            valResultAuthor = chainTxtAuthor.validate(uiState.value.bookAuthor.trim())
            valResultTitle = chainTxtTitleFields.validate(uiState.value.bookTitle.trim())
        }

        val valResultSubtitle = if(uiState.value.bookSubtitle.isNotBlank()) {
            chainTxtTitleFields.validate(uiState.value.bookSubtitle.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultSaga = if(uiState.value.bookSaga.isNotBlank()) {
            chainTxtTitleFields.validate(uiState.value.bookSaga.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultSagaIndex = if(uiState.value.bookSagaIndex.isNotBlank()) {
            chainTxtSagaIndex.validate(uiState.value.bookSagaIndex.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultSagaIndexNumber = if(uiState.value.bookSagaIndex.trim().isNotBlank()) {
            chainTxtIsOnlyNumber.validate(uiState.value.bookSagaIndex.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultCategory = if(uiState.value.bookCategory.isNotBlank()) {
            chainTxtTitleFields.validate(uiState.value.bookCategory.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultPublisher = if(uiState.value.bookPublisher.isNotBlank()) {
            chainTxtTitleFields.validate(uiState.value.bookPublisher.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultNotes = if(uiState.value.bookNotes.isNotBlank()) {
            chainTxtNoteFields.validate(uiState.value.bookNotes.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultYear = if(uiState.value.bookReadYear.isNotBlank()) {
            chainTxtYear.validate(uiState.value.bookReadYear.trim())
        }
        else {
            ValidatorResult.NoResult
        }
        val valResultYearNumber = if(uiState.value.bookReadYear.trim().isNotBlank()) {
            chainTxtIsOnlyNumber.validate(uiState.value.bookReadYear.trim())
        }
        else {
            ValidatorResult.NoResult
        }

        var result = true
        if(valResultAuthor is ValidatorResult.Error) {
            updateBookAuthorError(true, valResultAuthor.message)
            result = false
        }
        if(valResultTitle is ValidatorResult.Error) {
            updateBookTitleError(true, valResultTitle.message)
            result = false
        }
        if(valResultSubtitle is ValidatorResult.Error) {
            updateBookSubtitleError(true, valResultSubtitle.message)
            result = false
        }
        if(valResultSaga is ValidatorResult.Error) {
            updateBookSagaError(true, valResultSaga.message)
            result = false
        }
        if(valResultSagaIndex is ValidatorResult.Error) {
            updateBookSagaIndexError(true, valResultSagaIndex.message)
            result = false
        }
        if(valResultSagaIndexNumber is ValidatorResult.Error) {
            updateBookSagaIndexError(true, valResultSagaIndexNumber.message)
            result = false
        }

        if(valResultCategory is ValidatorResult.Error) {
            updateBookCategoryError(true, valResultCategory.message)
            result = false
        }
        if(valResultPublisher is ValidatorResult.Error) {
            updateBookPublisherError(true, valResultPublisher.message)
            result = false
        }
        if(valResultNotes is ValidatorResult.Error) {
            updateBookNotesError(true, valResultNotes.message)
            result = false
        }
        if(valResultYear is ValidatorResult.Error) {
            updateBookReadYearError(true, valResultYear.message)
            result = false
        }
        if(valResultYearNumber is ValidatorResult.Error) {
            updateBookReadYearError(true, valResultYearNumber.message)
            result = false
        }

        return result
    }

    private fun fillBookObj(): Book {
        val result = Book(null,
            capitalizeString(uiState.value.bookTitle),
            uiState.value.bookRead,
            if(uiState.value.bookReadYear.isNotBlank()) uiState.value.bookReadYear.toInt() else 1900,
            uiState.value.bookHave,
            uiState.value.bookSubtitle,
            uiState.value.bookNotes,
            capitalizeString(uiState.value.bookAuthor),
            capitalizeString(uiState.value.bookSaga),
            if(uiState.value.bookSagaIndex.isNotBlank()) uiState.value.bookSagaIndex.toInt() else 0,
            capitalizeString(uiState.value.bookPublisher),
            capitalizeString(uiState.value.bookCategory),
            uiState.value.bookLanguageCode,
            uiState.value.bookFormatCode
        )

        return result
    }

    private fun capitalizeString(words: String): String {
        if(words.isNullOrBlank()) {
            return words
        }
        val lowWords = words.lowercase()
        val lowWordsList: MutableList<String> = lowWords.split(" ").toMutableList()
        val firstWord = lowWordsList[0].replaceFirstChar { char ->
            char.titlecase()
        }
        lowWordsList[0] = firstWord
        return lowWordsList.joinToString(" ")
    }

    private fun fillEmptyBookObj(): Book {
        val result = Book(null,
            null,
            "n",
            null,
            "n",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null)
        return result
    }

    private fun searchBooks(filterBook: Book) {
        Klog.linedbg("LibraryViewModel", "searchBooks", "filterBook->$filterBook")

        viewModelScope.launch {
            val resp = libraryService.searchBooks(filterBook, AppState.useremail!!)
            Klog.line("LibraryViewModel", "searchBooks", "resp: $resp")
            if(resp.result) {
                updateBookList(resp.bookList ?: mutableListOf())
                clearErrors()
                clearState()
            }
            else {
                Klog.line("LibraryViewModel", "searchBooks", "error")
                setGeneralError(" ${resp.code}: ${resp.message}")
                updateBookList(mutableListOf())
            }
            updateIsDesiredBookListLoaded(true)
            updateIsDesiredBookListLoading(false)
        }
    }

    fun deleteBook() {
        Klog.linedbg("LibraryViewModel", "deleteBook", "Book id: ${_uiState.value.bookToDelete?.id}")
    }

    fun confirmDeleteBook(book: Book?, action: Boolean) {
        Klog.linedbg("LibraryViewModel","confirmDeleteBook", "click, book -> $book, action-> $action")
        clearErrors()

        updateBookToDelete(book)
        updateIsToDeleteBook(action)
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

    private fun updateBookActionWorking(action: Boolean) {
        _uiState.update {
            it.copy(isBookActionWorking = action)
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

    private fun updateIsToFilterBooks(action: Boolean) {
        _uiState.update {
            it.copy(isToFilterBooks = action)
        }
    }

    private fun updateBookToDelete(book: Book?) {
        _uiState.update {
            it.copy(bookToDelete = book)
        }
    }

    private fun updateIsToDeleteBook(action: Boolean) {
        _uiState.update {
            it.copy(isToDeleteBook = action)
        }
    }

    private fun updateHeaderMessage(msg: String) {
        _uiState.update {
            it.copy(headerMessage = msg)
        }
    }

    private fun updateBookTitleError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookTitleError = err)
        }
        updateBookTitleErrorTxt(txt)
    }

    private fun updateBookTitleErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookTitleErrorTxt = err)
        }
    }

    private fun updateBookSubtitleError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookSubtitleError = err)
        }
        updateBookSubTitleErrorTxt(txt)
    }

    private fun updateBookSubTitleErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookSubtitleErrorTxt = err)
        }
    }

    private fun updateBookNotesError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookNotesError = err)
        }
        updateBookNotesErrorTxt(txt)
    }

    private fun updateBookNotesErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookNotesErrorTxt = err)
        }
    }

    private fun updateBookAuthorError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookAuthorError = err)
        }

        updateBookAuthorErrorTxt(txt)
    }

    private fun updateBookAuthorErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookAuthorErrorTxt = err)
        }
    }

    private fun updateBookSagaError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookSagaError = err)
        }

        updateBookSagaErrorTxt(txt)
    }

    private fun updateBookSagaErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookSagaErrorTxt = err)
        }
    }

    private fun updateBookSagaIndexError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookSagaIndexError = err)
        }

        updateBookSagaIndexErrorTxt(txt)
    }

    private fun updateBookSagaIndexErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookSagaIndexErrorTxt = err)
        }
    }

    private fun updateBookPublisherError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookPublisherError = err)
        }

        updateBookPublisherErrorTxt(txt)
    }

    private fun updateBookPublisherErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookPublisherErrorTxt = err)
        }
    }

    private fun updateBookCategoryError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookCategoryError = err)
        }

        updateBookCategoryErrorTxt(txt)
    }

    private fun updateBookCategoryErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookCategoryErrorTxt = err)
        }
    }

    private fun updateBookReadYearError(err: Boolean, txt: String) {
        _uiState.update {
            it.copy(bookReadYearError = err)
        }

        updateBookReadYearErrorTxt(txt)
    }

    private fun updateBookReadYearErrorTxt(err: String) {
        _uiState.update {
            it.copy(bookReadYearErrorTxt = err)
        }
    }

    fun updateBookTitle(txt: String) {
        if(txt.length <= BOOK_TITLE_MAXLENGTH) {
            _uiState.update {
                it.copy(bookTitle = txt)
            }
        }
    }

    fun updateBookRead(isChecked: String) {
        _uiState.update {
            it.copy(bookRead = isChecked)
        }
    }

    fun updateBookHave(isChecked: String) {
        _uiState.update {
            it.copy(bookHave = isChecked)
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
        if(txt.length <= BOOK_NOTES_MAXLENGTH) {
            _uiState.update {
                it.copy(bookNotes = txt)
            }
        }
    }

    fun updateBookAuthor(txt: String) {
        if(txt.length <= BOOK_AUTHOR_MAXLENGTH) {
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

    fun updateBookSagaIndex(txt: String) {
        if(txt.length <= BOOK_SAGA_NUMBER_MAXLENGTH) {
            _uiState.update {
                it.copy(bookSagaIndex = txt)
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
        _uiState.update {
            it.copy(bookLanguage = txt)
        }
    }

    fun updateBookLanguageCode(txt: String) {
        _uiState.update {
            it.copy(bookLanguageCode = txt)
        }
    }

    fun updateBookFormat(txt: String) {
        _uiState.update {
            it.copy(bookFormat = txt)
        }
    }

    fun updateBookFormatCode(txt: String) {
        _uiState.update {
            it.copy(bookFormatCode = txt)
        }
    }

    fun updateBookReadYear(txt: String) {
        if(txt.length <= BOOK_YEAR_MAXLENGTH) {
            _uiState.update {
                it.copy(bookReadYear = txt)
            }
        }
    }

    private fun updateFormatMaster(formatMaster: MutableList<LMaster>) {
        _uiState.update {
            it.copy(formatsMaster = formatMaster)
        }
    }

    private fun updateFormatMasterFilter(formatMaster: MutableList<LMaster>) {
        _uiState.update {
            it.copy(formatsMasterFilter = formatMaster)
        }
    }

    private fun updateLanguagesMaster(lanMaster: MutableList<LMaster>) {
        _uiState.update {
            it.copy(languagesMaster = lanMaster)
        }
    }

    private fun updateLanguagesMasterFilter(lanMaster: MutableList<LMaster>) {
        _uiState.update {
            it.copy(languagesMasterFilter = lanMaster)
        }
    }
    private fun updateBookList(bookList: MutableList<Book>) {
        _uiState.update {
            it.copy(bookList = bookList)
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
        updateBookTitleError(false, "")
        updateBookSubtitleError(false, "")
        updateBookNotesError(false, "")
        updateBookAuthorError(false, "")
        updateBookSagaError(false, "")
        updateBookSagaIndexError(false, "")
        updateBookPublisherError(false, "")
        updateBookCategoryError(false, "")
        updateBookReadYearError(false, "")
    }
}