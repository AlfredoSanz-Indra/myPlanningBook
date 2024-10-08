package com.alfred.myplanningbook.domain.model

data class SimpleResponse(val result: Boolean,
                          val code: Int,
                          val message: String,
                          val errorcode: String) {

    var planningBook: PlanningBook? = null
    var planningBookList: MutableList<PlanningBook>? = null
    var task: TaskBook? = null
    var activity: ActivityBook? = null
    var taskBookList: MutableList<TaskBook>? = null
    var activityBookList: MutableList<ActivityBook>? = null
    var owner: Owner? = null
    var ownerList: MutableList<Owner>? = null
}