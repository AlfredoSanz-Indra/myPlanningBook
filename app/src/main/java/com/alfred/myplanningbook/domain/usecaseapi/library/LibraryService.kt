package com.alfred.myplanningbook.domain.usecaseapi.library

import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.SimpleLibraryResponse


/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface LibraryService {

    suspend fun listBooks(filterBook: Book): SimpleLibraryResponse

}