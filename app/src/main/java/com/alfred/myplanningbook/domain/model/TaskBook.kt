package com.alfred.myplanningbook.domain.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
data class TaskBook(
    var id: String?,
    val idPlanningbook: String,
    val name: String,
    val description: String?,
    val dateInMillis: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    var dayOfWeekStr: String?,
    val nature: TaskBookNatureEnum
) {

}
