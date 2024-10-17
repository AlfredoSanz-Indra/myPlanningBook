package com.alfred.myplanningbook.domain.usecaseapi.library

import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.SimpleLibraryResponse


/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface LibraryService {

    suspend fun searchBooks(filterBook: Book, userEmail: String): SimpleLibraryResponse

    suspend fun createBook(book: Book, userEmail: String): SimpleLibraryResponse

    suspend fun saveAuthor(book: Book, userEmail: String): SimpleLibraryResponse

    suspend fun saveCategory(book: Book, userEmail: String): SimpleLibraryResponse

    suspend fun savePublisher(book: Book, userEmail: String): SimpleLibraryResponse

    suspend fun saveSaga(book: Book, userEmail: String): SimpleLibraryResponse

    suspend fun loadBookFields(userEmail: String): SimpleLibraryResponse

    suspend fun getBookFieldList(userEmail: String, field: Int): SimpleLibraryResponse
}
