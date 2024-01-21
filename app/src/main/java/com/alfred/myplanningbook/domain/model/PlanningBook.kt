package com.alfred.myplanningbook.domain.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class PlanningBook(val id: String, val name: String, val idOwner: String) {

    var isActive: Boolean = false
}
