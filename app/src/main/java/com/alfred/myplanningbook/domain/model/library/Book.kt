package com.alfred.myplanningbook.domain.model.library

data class Book(var id: String?,
                val title: String,
                val read: Int,
                val readYear: Int,
                val have: Int,
                val subtitle: String?,
                val notes: String?,
                val authorName: String?,
                val authorID: Int?,
                val sagaName: String?,
                val sagaID: Int?,
                val editorial: String?,
                val categoryName: String?,
                val categoryID: Int?,
                val language: String?,
                val format: String?,
                )
/**
 * haveit -> { 0: false, 1: true }
 * read   -> { 0: false, 1: true }
 */
