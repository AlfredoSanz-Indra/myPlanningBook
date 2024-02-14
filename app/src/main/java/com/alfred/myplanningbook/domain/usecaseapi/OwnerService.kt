package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.SimpleResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface OwnerService {

    suspend fun getOwner(email: String): SimpleResponse

    suspend fun createOwner(email: String): SimpleResponse

    suspend fun updateOwnerPlanningBooks(owner: Owner): SimpleResponse

    suspend fun updateOwnerActivePlanningBook(pbID: String, ownerID: String): SimpleResponse

    suspend fun sharePlanningBookToOtherOwner(shareToEmail: String, pbID: String): SimpleResponse

    suspend fun forgetSharedPlanningBook(pbID: String): SimpleResponse
}