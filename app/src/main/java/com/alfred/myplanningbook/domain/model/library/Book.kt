package com.alfred.myplanningbook.domain.model.library

data class Book(var id: String?,
                val title: String?,
                val read: String,
                val readYear: Int?,
                val have: String,
                val subtitle: String?,
                val notes: String?,
                val authorName: String?,
                val sagaName: String?,
                val sagaIndex: Int?,
                val editorial: String?,
                val categoryName: String?,
                val language: String?,
                val format: String?,
                )
/**
 * haveit -> { n: false, y: true }
 * read   -> { n: false, y: true }
 */
