package com.alfred.myplanningbook.domain.model

data class SimpleResponse(val result: Boolean,
                          val code: Int,
                          val message: String,
                          val errorcode: String) {
}