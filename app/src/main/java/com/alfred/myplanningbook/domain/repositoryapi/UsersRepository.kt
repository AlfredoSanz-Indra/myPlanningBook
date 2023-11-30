package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse

/**
 * @author Alfredo Sanz
 * @time 2023
 */
interface UsersRepository {

    suspend fun registerUser(email: String, password: String): SimpleDataResponse

    suspend fun sendEmailVerification(): SimpleDataResponse

    fun logginUser(email: String, password: String): SimpleDataResponse
}