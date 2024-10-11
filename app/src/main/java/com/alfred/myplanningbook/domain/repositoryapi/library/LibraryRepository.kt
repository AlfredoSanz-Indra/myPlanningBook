package com.alfred.myplanningbook.domain.repositoryapi.library

import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.domain.model.library.Book

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface LibraryRepository {
    suspend fun createBook(book: Book, userEmail: String): SimpleDataLibraryResponse
}