package com.alfred.myplanningbook.domain.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
enum class TaskBookNatureEnum(val nature: Int) {
    ORIGIN_TASK(1),
    ORIGIN_ACTIVITY(2),
    IS_ACTIVITY(3)
}