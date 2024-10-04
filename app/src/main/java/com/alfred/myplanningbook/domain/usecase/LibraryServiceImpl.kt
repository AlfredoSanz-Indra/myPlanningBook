package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.domain.model.Book
import com.alfred.myplanningbook.domain.model.SimpleLibraryResponse
import com.alfred.myplanningbook.domain.repositoryapi.LibraryRepository
import com.alfred.myplanningbook.domain.usecaseapi.LibraryService

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