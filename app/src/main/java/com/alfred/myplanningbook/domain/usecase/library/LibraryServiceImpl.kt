package com.alfred.myplanningbook.domain.usecase.library

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.SimpleLibraryResponse
import com.alfred.myplanningbook.domain.repositoryapi.library.LibraryRepository
import com.alfred.myplanningbook.domain.usecaseapi.library.LibraryService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class LibraryServiceImpl(private val libraryRepository: LibraryRepository): LibraryService {

    override suspend fun searchBooks(filterBook: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse = SimpleLibraryResponse(false, 404, "0", "")
        Klog.line("LibraryServiceImpl", "searchBooks", "searching books for userEmail -> $userEmail")

        if(userEmail.isEmpty()) {
            Klog.line("LibraryServiceImpl", "searchBooks", "Missing userEmail")
            result = SimpleLibraryResponse(false, 404, "Fail: Missing userEmail!", "")
            return result
        }

        try {
            val resp = libraryRepository.searchBooks(filterBook, userEmail)

            if(resp.result && resp.code == 200) {
                result = SimpleLibraryResponse(true, 200, "found", "")
                result.bookList = resp.bookList
            }
            else {
                result = SimpleLibraryResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "searchBooks", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("LibraryServiceImpl", "searchBooks", "result: $result")
        return result
    }

    override suspend fun createBook(book: Book, userEmail: String): SimpleLibraryResponse {
        Klog.line("LibraryServiceImpl", "createBook", "creating Book -> userEmail: $userEmail")

        var respAddBook = addBook(book, userEmail)
        if(respAddBook.result) {
            val respSaveAuth = saveAuthor(book, userEmail)
            val respSaveCategory = saveCategory(book, userEmail)
            val respSavePublisher = savePublisher(book, userEmail)
            val respSaveSaga = saveSaga(book, userEmail)

            val respTotal = checkBookFieldsResponses("updating",respSaveAuth, respSaveCategory, respSavePublisher, respSaveSaga)
            if(!respTotal.result) {
                respAddBook.code = 501
                respAddBook.message = respTotal.message
            }
        }

        return respAddBook
    }


    private suspend fun addBook(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "addBook", "adding Book -> book: ${book.title}")

        try {
            val resp: SimpleDataLibraryResponse = libraryRepository.createBook(book, userEmail)
            Klog.linedbg("LibraryServiceImpl", "addBook", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "addBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "addBook", "result: $result")
        return result
    }

    override suspend fun saveAuthor(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "saveAuthor", "saving author -> book.authorName: ${book.authorName}")
        Klog.line("LibraryServiceImpl", "saveAuthor", "saving author -> userEmail: $userEmail")

        try {
            val authorID = book.authorName!!.replace(" ", "")

            val resp: SimpleDataLibraryResponse = libraryRepository.saveAuthor(book.authorName!!, authorID, userEmail)
            Klog.linedbg("LibraryServiceImpl", "saveAuthor", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "saveAuthor", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "saveAuthor", "result: $result")
        return result
    }

    override suspend fun saveCategory(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "saveCategory", "saving author -> book.authorName: ${book.categoryName}")
        Klog.line("LibraryServiceImpl", "saveCategory", "saving author -> userEmail: $userEmail")

        if(book.categoryName.isNullOrBlank()) {
            return SimpleLibraryResponse(true, 404, "missing field category", "")
        }

        try {
            val categID = book.categoryName!!.replace(" ", "")

            val resp: SimpleDataLibraryResponse = libraryRepository.saveCategory(book.categoryName!!, categID, userEmail)
            Klog.linedbg("LibraryServiceImpl", "saveCategory", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "saveCategory", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "saveCategory", "result: $result")
        return result
    }

    override suspend fun savePublisher(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "savePublisher", "saving author -> book.authorName: ${book.editorial}")
        Klog.line("LibraryServiceImpl", "savePublisher", "saving author -> userEmail: $userEmail")

        if(book.editorial.isNullOrBlank()) {
            return SimpleLibraryResponse(true, 404, "missing field publisher", "")
        }

        try {
            val publisherID = book.editorial!!.replace(" ", "")

            val resp: SimpleDataLibraryResponse = libraryRepository.savePublisher(book.editorial!!, publisherID, userEmail)
            Klog.linedbg("LibraryServiceImpl", "savePublisher", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "savePublisher", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "savePublisher", "result: $result")
        return result
    }

    override suspend fun saveSaga(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "saveSaga", "saving author -> book.authorName: ${book.sagaName}")
        Klog.line("LibraryServiceImpl", "saveSaga", "saving author -> userEmail: $userEmail")

        if(book.sagaName.isNullOrBlank()) {
            return SimpleLibraryResponse(true, 404, "missing field saga", "")
        }

        try {
            val sagaID = book.sagaName!!.replace(" ", "")

            val resp: SimpleDataLibraryResponse = libraryRepository.saveSaga(book.sagaName!!, sagaID, userEmail)
            Klog.linedbg("LibraryServiceImpl", "saveSaga", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "saveSaga", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "saveSaga", "result: $result")
        return result
    }

    override suspend fun loadBookFields(userEmail: String): SimpleLibraryResponse {
        var result = SimpleLibraryResponse(true, 200, "bookFields list obtained", "")
        Klog.line("LibraryServiceImpl", "loadBookFields", "loading book fields for userEmail -> $userEmail")

        if(userEmail.isEmpty()) {
            Klog.line("LibraryServiceImpl", "loadBookFields", "Missing userEmail")
            result = SimpleLibraryResponse(false, 404, "Fail: Missing userEmail!", "")
            return result
        }

        try {
            val respAuthor = getBookFieldList(userEmail, 1)
            val respCategory = getBookFieldList(userEmail, 2)
            val respPublisher = getBookFieldList(userEmail, 3)
            val respSaga = getBookFieldList(userEmail, 4)

            result.authorList = respAuthor.authorList
            result.categoryList = respCategory.categoryList
            result.publisherList = respPublisher.publisherList
            result.sagaList = respSaga.sagaList

            val respTotal = checkBookFieldsResponses("updating",respAuthor, respCategory, respPublisher, respSaga)
            if(!respTotal.result) {
                result.code = 501
                result.message = respTotal.message
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "loadBookFields", "* Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("LibraryServiceImpl", "loadBookFields", "result: $result")
        return result
    }

    override suspend fun getBookFieldList(userEmail: String, field: Int): SimpleLibraryResponse {
        var result = SimpleLibraryResponse(false, 404, "Error getting some book Field", "")
        Klog.line("LibraryServiceImpl", "getBookFieldList", "getting bookField for userEmail -> $userEmail")
        Klog.line("LibraryServiceImpl", "getBookFieldList", "getting bookField for field -> $field")

        if(userEmail.isEmpty()) {
            Klog.line("LibraryServiceImpl", "getBookFieldList", "Missing userEmail")
            result = SimpleLibraryResponse(false, 404, "Fail: Missing userEmail!", "")
            return result
        }

        try {
            val resp = when(field) {
                1    -> libraryRepository.getAuthorList(userEmail)
                2    -> libraryRepository.getCategoryList(userEmail)
                3    -> libraryRepository.getPublisherList(userEmail)
                4    -> libraryRepository.getSagaList(userEmail)
                else -> SimpleDataLibraryResponse(false, 100, "not found")
            }

            if(resp.result && resp.code == 200) {
                result = SimpleLibraryResponse(true, 200, "found", "")
                when(field) {
                    1    -> result.authorList = resp.authorList
                    2    -> result.categoryList = resp.categoryList
                    3    -> result.publisherList = resp.publisherList
                    4    -> result.sagaList = resp.sagaList
                    else -> SimpleDataLibraryResponse(false, 100, "not found")
                }
            }
            else {
                result = SimpleLibraryResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "getBookFieldList", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("LibraryServiceImpl", "getBookFieldList", "result: $result")
        return result
    }

    override suspend fun updateBook(book: Book, userEmail: String): SimpleLibraryResponse {
        Klog.line("LibraryServiceImpl", "updateBook", "updating Book -> book: ${book.title}")
        Klog.line("LibraryServiceImpl", "updateBook", "updating Book -> userEmail: $userEmail")

        var respUpdateBook = updatingBook(book, userEmail)
        if(respUpdateBook.result) {
            val respSaveAuth = saveAuthor(book, userEmail)
            val respSaveCategory = saveCategory(book, userEmail)
            val respSavePublisher = savePublisher(book, userEmail)
            val respSaveSaga = saveSaga(book, userEmail)

            val respTotal = checkBookFieldsResponses("updating",respSaveAuth, respSaveCategory, respSavePublisher, respSaveSaga)
            if(!respTotal.result) {
                respUpdateBook.code = 501
                respUpdateBook.message = respTotal.message
            }
        }

        return respUpdateBook
    }

    private suspend fun updatingBook(book: Book, userEmail: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "updatingBook", "updating Book -> book: ${book.title}")

        try {
            val resp: SimpleDataLibraryResponse = libraryRepository.updateBook(book, userEmail)
            Klog.linedbg("LibraryServiceImpl", "updatingBook", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "updatingBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "updatingBook", "result: $result")
        return result
    }

    private fun checkBookFieldsResponses(action: String,
                                                 respAuth: SimpleLibraryResponse,
                                                 respCateg: SimpleLibraryResponse,
                                                 respPublish: SimpleLibraryResponse,
                                                 respSaga: SimpleLibraryResponse,): SimpleLibraryResponse {

        var result = SimpleLibraryResponse(true, 200, "", "")
        var errMsg = ""
        var empty = true
        if(!respAuth.result) {
            errMsg = "error $action Authors ->  ${respAuth.code}"
            empty = false
        }
        if(!respCateg.result) {
            if(!empty) errMsg += ", "
            errMsg += "error $action Categories ->  ${respCateg.code}"
            empty = false
        }
        if(!respPublish.result) {
            if(!empty) errMsg += ", "
            errMsg += "error $action Publishers ->  ${respPublish.code}"
            empty = false
        }
        if(!respSaga.result) {
            if(!empty) errMsg += ", "
            errMsg += "error $action Sagas ->  ${respSaga.code}"
            empty = false
        }

        if(!empty) {
            result = SimpleLibraryResponse(false, 501, errMsg, "")
        }

        return result
    }

    override suspend fun deleteBook(bookID: String): SimpleLibraryResponse {
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "deleteBook", "updating Book -> book: $bookID")

        try {
            val resp = libraryRepository.deleteBook(bookID)

            result = if (resp.result && resp.code == 200) {
                SimpleLibraryResponse(true, 200, "got it", "")
            }
            else {
                SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "deleteBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("LibraryServiceImpl", "deleteBook", "result: $result")
        return result
    }
}