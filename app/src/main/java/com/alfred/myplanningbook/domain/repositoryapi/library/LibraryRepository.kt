package com.alfred.myplanningbook.domain.repositoryapi.library

import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.domain.model.library.Book

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface LibraryRepository {
    suspend fun createBook(book: Book, userEmail: String): SimpleDataLibraryResponse

    suspend fun searchBooks(filterBook: Book, userEmail: String): SimpleDataLibraryResponse

    suspend fun saveAuthor(author: String, authorid: String, userEmail: String): SimpleDataLibraryResponse

    suspend fun saveCategory(categ: String, categID: String, userEmail: String): SimpleDataLibraryResponse

    suspend fun savePublisher(publisher: String, publisherID: String, userEmail: String): SimpleDataLibraryResponse

    suspend fun saveSaga(saga: String, sagaID: String, userEmail: String): SimpleDataLibraryResponse

    suspend fun getAuthorList(userEmail: String): SimpleDataLibraryResponse

    suspend fun getCategoryList(userEmail: String): SimpleDataLibraryResponse

    suspend fun getPublisherList(userEmail: String): SimpleDataLibraryResponse

    suspend fun getSagaList(userEmail: String): SimpleDataLibraryResponse

    suspend fun updateBook(book: Book, userEmail: String): SimpleDataLibraryResponse

    suspend fun deleteBook(bookID: String): SimpleDataLibraryResponse
}

