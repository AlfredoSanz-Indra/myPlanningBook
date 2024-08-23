package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.SimpleResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface ActivityService {

    suspend fun createActivity(activity: ActivityBook): SimpleResponse

    suspend fun getActivityList(planningBookId: String, isActive: Int): SimpleResponse
}