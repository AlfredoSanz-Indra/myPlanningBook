package com.alfred.myplanningbook.domain.model.library

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class SimpleLibraryResponse(val result: Boolean,
                                 val code: Int,
                                 val message: String,
                                 val errorcode: String) {

    var bookList: MutableList<Book>? = null
    var book: Book? = null
    var authorList: MutableList<BookField>? = null
    var categoryList: MutableList<BookField>? = null
    var publisherList: MutableList<BookField>? = null
    var sagaList: MutableList<BookField>? = null
}
