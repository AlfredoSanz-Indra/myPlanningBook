package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.TaskBook

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface ActivityService {

    suspend fun createActivity(activity: ActivityBook): SimpleResponse

    suspend fun updateActivity(activity: ActivityBook): SimpleResponse

    suspend fun getActivityList(planningBookId: String, isActive: Int): SimpleResponse
}