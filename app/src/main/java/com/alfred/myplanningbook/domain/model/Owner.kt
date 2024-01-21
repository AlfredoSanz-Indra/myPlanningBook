package com.alfred.myplanningbook.domain.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class Owner(val id: String,
                 val name: String,
                 val email: String,
                 var activePlanningBook: String?,
                 var planningBooks: MutableList<String>?) {
}