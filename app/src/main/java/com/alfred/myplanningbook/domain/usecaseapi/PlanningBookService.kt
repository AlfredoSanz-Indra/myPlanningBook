package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.SimpleResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface PlanningBookService {



    suspend fun getPlanningBook(id: String): SimpleResponse

    suspend fun getPlanningBooks(plannings: List<String>): SimpleResponse

    suspend fun createPlanningBook(name: String): SimpleResponse


}