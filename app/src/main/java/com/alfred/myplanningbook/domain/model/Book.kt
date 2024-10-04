package com.alfred.myplanningbook.domain.model

data class Book(var id: String?,
                val title: String,
                val read: Int,
                val haveit: Int,
                val subtitle: String?,
                val description: String?,
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
