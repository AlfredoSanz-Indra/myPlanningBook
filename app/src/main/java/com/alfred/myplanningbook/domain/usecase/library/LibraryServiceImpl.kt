package com.alfred.myplanningbook.domain.usecase.library

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.SimpleLibraryResponse
import com.alfred.myplanningbook.domain.repositoryapi.library.LibraryRepository
import com.alfred.myplanningbook.domain.usecaseapi.library.LibraryService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class LibraryServiceImpl(private val libraryRepository: LibraryRepository): LibraryService {

    override suspend fun listBooks(filterBook: Book): SimpleLibraryResponse {

        var result: SimpleLibraryResponse =  SimpleLibraryResponse(false, 404, "0", "")

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