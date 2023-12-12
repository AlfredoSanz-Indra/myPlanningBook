package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.UserRegisterResp

/**
 * @author Alfredo Sanz
 * @time 2023
 */

interface UsersService {

    suspend fun newUserRegister(email: String, pwd: String): SimpleResponse

    /**
     * @return SimpleResponse
     */
    suspend fun logginUser(email: String, pwd: String): SimpleResponse

    suspend fun logoutUser(): SimpleResponse

    suspend fun sendResetEmail(email: String): SimpleResponse

}