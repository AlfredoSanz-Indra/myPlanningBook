package com.alfred.myplanningbook.domain.model.library

data class Book(var id: String?,
                val title: String,
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
 * haveit -> { 0: false, 1: true }
 * read   -> { 0: false, 1: true }
 */
