package com.alfred.myplanningbook.domain.usecase.library

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
}