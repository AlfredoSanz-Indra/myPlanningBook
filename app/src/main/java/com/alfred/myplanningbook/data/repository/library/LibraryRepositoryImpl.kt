package com.alfred.myplanningbook.data.repository.library

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.repositoryapi.library.LibraryRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class LibraryRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): LibraryRepository {

    override suspend fun createBook(book: Book, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not created")
        Klog.line("LibraryRepositoryImpl", "createBook", "name: ${book.title}")

        val bookData = hashMapOf(
            Documents.LIBRARYBOOK_USEREMAIL to userEmail,
            Documents.LIBRARYBOOK_TITLE to book.title,
            Documents.LIBRARYBOOK_AUTHOR to book.authorName,
            Documents.LIBRARYBOOK_SUBTITLE to book.subtitle,
            Documents.LIBRARYBOOK_NOTES to book.notes,
            Documents.LIBRARYBOOK_CATEGORY to book.categoryName,
            Documents.LIBRARYBOOK_SAGA to book.sagaName,
            Documents.LIBRARYBOOK_SAGA_INDEX to book.sagaIndex,
            Documents.LIBRARYBOOK_PUBLISHER to book.editorial,
            Documents.LIBRARYBOOK_FORMAT to book.format,
            Documents.LIBRARYBOOK_LANGUAGE to book.language,
            Documents.LIBRARYBOOK_READ to book.read,
            Documents.LIBRARYBOOK_HAVE to book.have,
            Documents.LIBRARYBOOK_READYEAR to book.readYear
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.LIBRARYBOOKS)
                    .add(bookData)
                    .addOnSuccessListener {}

                task.await()

                var bookResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "createBook", "task is successful")
                    bookResp = SimpleDataLibraryResponse(true, 200, "Book created - ${task.result.id}")
                    book.id = task.result.id
                    Klog.line("LibraryRepositoryImpl", "createBook", "book: $book")
                    bookResp.book = book
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "createBook", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "createBook", "error message: ${task.exception?.message}")

                    bookResp = SimpleDataLibraryResponse(false, 400, "Creating Book failed.")
                }

                return@async bookResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "createBook", "result: $result")
        return result
    }

    override suspend fun searchBooks(filterBook: Book, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not found")
        Klog.line("LibraryRepositoryImpl", "searchBooks", "filterBook: $filterBook")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {

                var query: Query = FirebaseSession.db.collection(Collections.LIBRARYBOOKS)
                query = query.whereEqualTo(Documents.LIBRARYBOOK_USEREMAIL, userEmail)
                if(filterBook.read != "t") {
                    query = query.whereEqualTo(Documents.LIBRARYBOOK_READ, filterBook.read)
                }
                if(filterBook.have != "t") {
                    query = query.whereEqualTo(Documents.LIBRARYBOOK_HAVE, filterBook.have)
                }

                if(!filterBook.authorName.isNullOrBlank()) {
                    query = query.whereGreaterThanOrEqualTo(Documents.LIBRARYBOOK_AUTHOR, filterBook.authorName)
                    query = query.whereLessThan(Documents.LIBRARYBOOK_AUTHOR, filterBook.authorName + "z")
                }
                if(!filterBook.title.isNullOrBlank()) {
                    query = query.whereGreaterThanOrEqualTo(Documents.LIBRARYBOOK_TITLE, filterBook.title)
                    query = query.whereLessThan(Documents.LIBRARYBOOK_TITLE, filterBook.title + "z")
                }
                if(!filterBook.sagaName.isNullOrBlank()) {
                    query = query.whereGreaterThanOrEqualTo(Documents.LIBRARYBOOK_SAGA, filterBook.sagaName)
                    query = query.whereLessThan(Documents.LIBRARYBOOK_SAGA, filterBook.sagaName + "z")
                }
                if(!filterBook.categoryName.isNullOrBlank()) {
                    query = query.whereGreaterThanOrEqualTo(Documents.LIBRARYBOOK_CATEGORY, filterBook.categoryName)
                    query = query.whereLessThan(Documents.LIBRARYBOOK_CATEGORY, filterBook.categoryName + "z")
                }
                if(!filterBook.editorial.isNullOrBlank()) {
                    query = query.whereGreaterThanOrEqualTo(Documents.LIBRARYBOOK_PUBLISHER, filterBook.editorial)
                    query = query.whereLessThan(Documents.LIBRARYBOOK_PUBLISHER, filterBook.editorial + "z")
                }
                if(!filterBook.format.isNullOrBlank() && filterBook.format != "todos") {
                    query = query.whereEqualTo(Documents.LIBRARYBOOK_FORMAT, filterBook.format)
                }
                if(!filterBook.language.isNullOrBlank() && filterBook.language != "todos") {
                    query = query.whereEqualTo(Documents.LIBRARYBOOK_LANGUAGE, filterBook.language)
                }

                val task: Task<QuerySnapshot> = query
                    .orderBy(Documents.LIBRARYBOOK_SAGA_INDEX)
                    .orderBy(Documents.LIBRARYBOOK_TITLE)
                    .get()
                    .addOnSuccessListener {  }

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "searchBooks", "task is successful")
                    var bookList: MutableList<Book> = mutableListOf()
                    for (document in task.result.documents) {
                        var bookFound: Book = Book(
                            document.id,
                            document.get(Documents.LIBRARYBOOK_TITLE) as String,
                            document.get(Documents.LIBRARYBOOK_READ) as String,
                            (document.get(Documents.LIBRARYBOOK_READYEAR) as? Long)?.toInt(),
                            document.get(Documents.LIBRARYBOOK_HAVE) as String,
                            document.get(Documents.LIBRARYBOOK_SUBTITLE) as String?,
                            document.get(Documents.LIBRARYBOOK_NOTES) as String?,
                            document.get(Documents.LIBRARYBOOK_AUTHOR) as String,
                            document.get(Documents.LIBRARYBOOK_SAGA) as String?,
                            (document.get(Documents.LIBRARYBOOK_SAGA_INDEX) as? Long)?.toInt(),
                            document.get(Documents.LIBRARYBOOK_PUBLISHER) as String?,
                            document.get(Documents.LIBRARYBOOK_CATEGORY) as String?,
                            document.get(Documents.LIBRARYBOOK_LANGUAGE) as String,
                            document.get(Documents.LIBRARYBOOK_FORMAT) as String,
                        )
                        bookList.add(bookFound)
                    }

                    taskResp = SimpleDataLibraryResponse(true, 200, "book list - $bookList")
                    taskResp.bookList = bookList
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "searchBooks", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "searchBooks", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Searching books failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("LibraryRepositoryImpl", "searchBooks", "result: $result")
        return result
    }
}