package com.alfred.myplanningbook.data.repository.library

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.repositoryapi.library.LibraryRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
}