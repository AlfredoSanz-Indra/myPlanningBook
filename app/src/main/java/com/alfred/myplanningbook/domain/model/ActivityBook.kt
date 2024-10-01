package com.alfred.myplanningbook.domain.model

import com.alfred.myplanningbook.core.util.DateTimeUtils

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
    val weekDaysList: MutableList<String>,
    var isActive: Int?
) {
    companion object {
        const val DESCRIPTION_LENGTH_SHORT = 60
    }

    fun getStringWeekDaysList(): String {
        return weekDaysList.joinToString(", ")
    }

    fun getFormattedStartTime(): String {
        return DateTimeUtils.formatTime(startHour, startMinute)
    }

    fun getFormattedEndTime(): String {
        return DateTimeUtils.formatTime(endHour, endMinute)
    }

    fun getDescriptionInShort(): String {
        var result = "*"
        if(!description.isNullOrBlank()) {
          if(description.length > DESCRIPTION_LENGTH_SHORT) {
              result =  "${description.substring(0, DESCRIPTION_LENGTH_SHORT)}..."
          }
          else {
              result = description
          }
        }

        return result
    }


}