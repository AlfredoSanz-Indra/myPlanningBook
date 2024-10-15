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
        var result: SimpleLibraryResponse =  SimpleLibraryResponse(false, 404, "0", "")
        Klog.line("LibraryServiceImpl", "searchBooks", "searching books for userEmail -> $userEmail")

        if(userEmail.isEmpty()) {
            Klog.line("LibraryServiceImpl", "searchBooks", "Missing userEmail")
            result = SimpleLibraryResponse(false, 404, "Fail: Missing userEmail!", "")
            return result
        }

        try {
            val resp = libraryRepository.searchBooks(filterBook, userEmail)

            if(resp.result && resp.code == 200) {
                result = SimpleLibraryResponse(true,200, "found", "")
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
        var result: SimpleLibraryResponse
        Klog.line("LibraryServiceImpl", "createBook", "creating Book -> book: ${book.title}")
        Klog.line("LibraryServiceImpl", "createBook", "creating Book -> userEmail: $userEmail")

        try {
            val resp: SimpleDataLibraryResponse = libraryRepository.createBook(book, userEmail)
            Klog.linedbg("LibraryServiceImpl", "createBook", "resp: $resp")

            if(!resp.result) {
                result = SimpleLibraryResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleLibraryResponse(true, 200, "got it", "")
                result.book = resp.book
            }
        }
        catch(e: Exception) {
            Klog.line("LibraryServiceImpl", "createBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleLibraryResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.linedbg("LibraryServiceImpl", "createBook", "result: $result")
        return result
    }
}