package com.alfred.myplanningbook.domain

import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object AppState {

    var useremail: String? = null
    var owner: Owner? = null
    var activePlanningBook: PlanningBook? = null
    var planningBooks: MutableList<PlanningBook> = mutableListOf()

    /**
     * Can be set to null when log out.
     */
    fun setUserEmail(_email: String?) {
        useremail = _email
    }

    fun cleanState() {
        useremail = null
        owner = null
        activePlanningBook = null
        planningBooks = mutableListOf()
    }

}