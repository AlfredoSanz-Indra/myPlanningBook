package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.SimpleResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface PlanningBookService {

    suspend fun loadState(email: String): SimpleResponse


}