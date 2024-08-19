package com.alfred.myplanningbook.domain.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class ActivityBook(
    var id: String?,
    val idPlanningbook: String,
    val name: String,
    val description: String?,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val weekDaysList: MutableList<String>
) {}