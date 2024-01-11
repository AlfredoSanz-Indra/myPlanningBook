package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface PlanningBookRepository {

    suspend fun createPlanningBook(email: String, name: String): SimpleDataResponse
    suspend fun getPlanningBook(email: String): SimpleDataResponse
    suspend fun findOwner(email: String): SimpleDataResponse
    suspend fun createOwner(email: String, name: String): SimpleDataResponse
}