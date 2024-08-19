package com.alfred.myplanningbook.data.model

import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.TaskBook

/**
 * @author Alfredo Sanz
 * @time 2023
 */
data class SimpleDataResponse(val result: Boolean,
                              val code: Int,
                              val message: String) {

    var planningBook: PlanningBook? = null
    var owner: Owner? = null
    var ownerList: MutableList<Owner>? = null
    var task: TaskBook? = null
    var activity: ActivityBook? = null
    var taskBookList: MutableList<TaskBook>? = null
}