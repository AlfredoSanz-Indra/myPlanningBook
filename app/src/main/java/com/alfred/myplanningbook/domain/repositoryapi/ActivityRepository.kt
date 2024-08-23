package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.ActivityBook

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface ActivityRepository {

    suspend fun createActivity(activity: ActivityBook): SimpleDataResponse


    suspend fun getActivityList(planningBookId: String, isActive: Int): SimpleDataResponse
}