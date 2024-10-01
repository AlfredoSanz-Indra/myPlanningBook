package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface PlanningBookRepository {

    suspend fun createPlanningBook(email: String, idOwner: String): SimpleDataResponse
    suspend fun getPlanningBook(idPlanningBook: String): SimpleDataResponse
    suspend fun removePlanningBook(id: String): SimpleDataResponse
}

