package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.SimpleResponse

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface StateService {

    suspend fun loadState(email: String): SimpleResponse

    suspend fun updateState_activePlanningBook(pbID: String, ownerID: String): SimpleResponse

}