package com.alfred.myplanningbook.domain

import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object AppState {

    var useremail: String? = null
    lateinit var planningBook: PlanningBook
    lateinit var owner: Owner

    /**
     * Can be set to null when log out.
     */
    fun setUserEmail(_email: String?) {
        useremail = _email
    }
}