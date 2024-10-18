package com.alfred.myplanningbook.data.repository.library

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataLibraryResponse
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.domain.model.library.BookField
import com.alfred.myplanningbook.domain.repositoryapi.library.LibraryRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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

    override suspend fun saveAuthor(author: String, authorid: String, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not updated")
        Klog.line("LibraryRepositoryImpl", "saveAuthor", "authorid: $authorid")

        val authorData = hashMapOf(
            Documents.LIBRARYAUTHORS_USEREMAIL to userEmail,
            Documents.LIBRARYAUTHORS_NAME to author,
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.LIBRARYAUTHORS).document(authorid)
                    .set(authorData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "saveAuthor", "task is successful")
                    taskResp = SimpleDataLibraryResponse(true, 200, "Author saved - $author")
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "saveAuthor", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "saveAuthor", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Saving author failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "saveAuthor", "result: $result")
        return result
    }

    override suspend fun getAuthorList(userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not found")
        Klog.line("LibraryRepositoryImpl", "getAuthorList", "userEmail: $userEmail")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {

                var query: Query = FirebaseSession.db.collection(Collections.LIBRARYAUTHORS)
                query = query.whereEqualTo(Documents.LIBRARYAUTHORS_USEREMAIL, userEmail)

                val task: Task<QuerySnapshot> = query
                    .orderBy(Documents.LIBRARYAUTHORS_NAME)
                    .get()
                    .addOnSuccessListener {  }

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "getAuthorList", "task is successful")
                    var bookFieldList: MutableList<BookField> = mutableListOf()
                    for (document in task.result.documents) {
                        var bookFieldFound = BookField(
                            document.id,
                            document.get(Documents.LIBRARYAUTHORS_NAME) as String,
                        )
                        bookFieldList.add(bookFieldFound)
                    }

                    taskResp = SimpleDataLibraryResponse(true, 200, "author list - $bookFieldList")
                    taskResp.authorList = bookFieldList
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "getAuthorList", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "getAuthorList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Searching authors failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("LibraryRepositoryImpl", "getAuthorList", "result: $result")
        return result
    }

    override suspend fun saveCategory(categ: String, categID: String, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not updated")
        Klog.line("LibraryRepositoryImpl", "saveCategory", "categID: $categID")

        val categoryData = hashMapOf(
            Documents.LIBRARYCATEGORY_USEREMAIL to userEmail,
            Documents.LIBRARYCATEGORY_NAME to categ,
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.LIBRARYCATEGORY).document(categID)
                    .set(categoryData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "saveCategory", "task is successful")
                    taskResp = SimpleDataLibraryResponse(true, 200, "Category saved - $categ")
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "saveCategory", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "saveCategory", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Saving Category failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "saveCategory", "result: $result")
        return result
    }

    override suspend fun getCategoryList(userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not found")
        Klog.line("LibraryRepositoryImpl", "getCategoryList", "userEmail: $userEmail")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {

                var query: Query = FirebaseSession.db.collection(Collections.LIBRARYCATEGORY)
                query = query.whereEqualTo(Documents.LIBRARYCATEGORY_USEREMAIL, userEmail)

                val task: Task<QuerySnapshot> = query
                    .orderBy(Documents.LIBRARYCATEGORY_NAME)
                    .get()
                    .addOnSuccessListener {  }

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "getCategoryList", "task is successful")
                    var bookFieldList: MutableList<BookField> = mutableListOf()
                    for (document in task.result.documents) {
                        var bookFieldFound = BookField(
                            document.id,
                            document.get(Documents.LIBRARYCATEGORY_NAME) as String,
                        )
                        bookFieldList.add(bookFieldFound)
                    }

                    taskResp = SimpleDataLibraryResponse(true, 200, "category list - $bookFieldList")
                    taskResp.categoryList = bookFieldList
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "getCategoryList", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "getCategoryList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Searching categories failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("LibraryRepositoryImpl", "getCategoryList", "result: $result")
        return result
    }

    override suspend fun savePublisher(publisher: String, publisherID: String, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not updated")
        Klog.line("LibraryRepositoryImpl", "savePublisher", "publisherID: $publisherID")

        val publisherData = hashMapOf(
            Documents.LIBRARYPUBLISHER_NAME to publisher,
            Documents.LIBRARYPUBLISHER_USEREMAIL to userEmail,
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.LIBRARYPUBLISHER).document(publisherID)
                    .set(publisherData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "savePublisher", "task is successful")
                    taskResp = SimpleDataLibraryResponse(true, 200, "Category saved - $publisher")
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "savePublisher", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "savePublisher", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Saving Publisher failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "savePublisher", "result: $result")
        return result
    }

    override suspend fun getPublisherList(userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not found")
        Klog.line("LibraryRepositoryImpl", "getPublisherList", "userEmail: $userEmail")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {

                var query: Query = FirebaseSession.db.collection(Collections.LIBRARYPUBLISHER)
                query = query.whereEqualTo(Documents.LIBRARYPUBLISHER_USEREMAIL, userEmail)

                val task: Task<QuerySnapshot> = query
                    .orderBy(Documents.LIBRARYPUBLISHER_NAME)
                    .get()
                    .addOnSuccessListener {  }

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "getPublisherList", "task is successful")
                    var bookFieldList: MutableList<BookField> = mutableListOf()
                    for (document in task.result.documents) {
                        var bookFieldFound = BookField(
                            document.id,
                            document.get(Documents.LIBRARYPUBLISHER_NAME) as String,
                        )
                        bookFieldList.add(bookFieldFound)
                    }

                    taskResp = SimpleDataLibraryResponse(true, 200, "publisher list - $bookFieldList")
                    taskResp.publisherList = bookFieldList
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "getPublisherList", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "getPublisherList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Searching publishers failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("LibraryRepositoryImpl", "getPublisherList", "result: $result")
        return result
    }

    override suspend fun saveSaga(saga: String, sagaID: String, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not updated")
        Klog.line("LibraryRepositoryImpl", "saveSaga", "publisherID: $sagaID")

        val sagaData = hashMapOf(
            Documents.LIBRARYSAGA_NAME to saga,
            Documents.LIBRARYSAGA_USEREMAIL to userEmail,
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.LIBRARYSAGA).document(sagaID)
                    .set(sagaData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "saveSaga", "task is successful")
                    taskResp = SimpleDataLibraryResponse(true, 200, "Category saved - $saga")
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "saveSaga", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "saveSaga", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Saving Saga failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "saveSaga", "result: $result")
        return result
    }

    override suspend fun getSagaList(userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not found")
        Klog.line("LibraryRepositoryImpl", "getSagaList", "userEmail: $userEmail")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {

                var query: Query = FirebaseSession.db.collection(Collections.LIBRARYSAGA)
                query = query.whereEqualTo(Documents.LIBRARYSAGA_USEREMAIL, userEmail)

                val task: Task<QuerySnapshot> = query
                    .orderBy(Documents.LIBRARYSAGA_NAME)
                    .get()
                    .addOnSuccessListener {  }

                task.await()

                var taskResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "getSagaList", "task is successful")
                    var bookFieldList: MutableList<BookField> = mutableListOf()
                    for (document in task.result.documents) {
                        var bookFieldFound = BookField(
                            document.id,
                            document.get(Documents.LIBRARYSAGA_NAME) as String,
                        )
                        bookFieldList.add(bookFieldFound)
                    }

                    taskResp = SimpleDataLibraryResponse(true, 200, "saga list - $bookFieldList")
                    taskResp.sagaList = bookFieldList
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "getSagaList", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "getSagaList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataLibraryResponse(false, 400, "Searching sagas failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("LibraryRepositoryImpl", "getSagaList", "result: $result")
        return result
    }

    override suspend fun updateBook(book: Book, userEmail: String): SimpleDataLibraryResponse {
        var result = SimpleDataLibraryResponse(false, 100, "not created")
        Klog.line("LibraryRepositoryImpl", "updateBook", "name: ${book.title}")

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
                val task: Task<Void> = FirebaseSession.db.collection(Collections.LIBRARYBOOKS).document(book.id!!)
                    .set(bookData)
                    .addOnSuccessListener {}

                task.await()

                var bookResp: SimpleDataLibraryResponse
                if(task.isSuccessful) {
                    Klog.line("LibraryRepositoryImpl", "updateBook", "task is successful")
                    bookResp = SimpleDataLibraryResponse(true, 200, "Book updated - ${book.id}")
                    bookResp.book = book
                }
                else {
                    Klog.line("LibraryRepositoryImpl", "updateBook", "error cause: ${task.exception?.cause}")
                    Klog.line("LibraryRepositoryImpl", "updateBook", "error message: ${task.exception?.message}")

                    bookResp = SimpleDataLibraryResponse(false, 400, "Updating Book failed.")
                }

                return@async bookResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("LibraryRepositoryImpl", "updateBook", "result: $result")
        return result
    }
}