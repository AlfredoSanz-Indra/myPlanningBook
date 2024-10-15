package com.alfred.myplanningbook.data.model

import com.alfred.myplanningbook.domain.model.library.Book

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class SimpleDataLibraryResponse(val result: Boolean,
                                     val code: Int,
                                     val message: String) {

        var book: Book? = null
        var bookList: MutableList<Book>? = null
}