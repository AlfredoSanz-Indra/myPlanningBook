package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface OwnerRepository {
    suspend fun getOwner(email: String): SimpleDataResponse

    suspend fun createOwner(email: String, name: String): SimpleDataResponse

    suspend fun updateOwnerPlanningBooks(ownerid: String, pblist: MutableList<String>): SimpleDataResponse

    suspend fun updateOwnerActivePlanningBook(ownerid: String, planningbookID: String): SimpleDataResponse
}
