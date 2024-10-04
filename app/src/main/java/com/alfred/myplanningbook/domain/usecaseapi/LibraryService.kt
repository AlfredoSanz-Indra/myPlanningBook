package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.Book
import com.alfred.myplanningbook.domain.model.SimpleLibraryResponse


/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface LibraryService {

    suspend fun listBooks(filterBook: Book): SimpleLibraryResponse

}