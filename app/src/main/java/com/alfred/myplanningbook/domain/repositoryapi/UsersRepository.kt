package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse

/**
 * @author Alfredo Sanz
 * @time 2023
 */
interface UsersRepository {

    suspend fun registerUser(email: String, password: String): SimpleDataResponse

    suspend fun sendEmailVerification(): SimpleDataResponse

    suspend fun logginUser(email: String, password: String): SimpleDataResponse

    suspend fun logoutUser(): SimpleDataResponse

    suspend fun sendResetEmail(email: String): SimpleDataResponse
}