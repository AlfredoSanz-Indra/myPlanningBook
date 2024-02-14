package com.alfred.myplanningbook.domain.model

data class SimpleResponse(val result: Boolean,
                          val code: Int,
                          val message: String,
                          val errorcode: String) {

    var planningBook: PlanningBook? = null
    var planningBookList: MutableList<PlanningBook>? = null
    var owner: Owner? = null
    var ownerList: MutableList<Owner>? = null
}