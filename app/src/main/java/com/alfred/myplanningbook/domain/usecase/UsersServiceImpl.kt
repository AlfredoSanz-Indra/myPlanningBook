package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.UserRegisterResp
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.alfred.myplanningbook.domain.usecaseapi.UsersService

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class UsersServiceImpl(val usersRepository: UsersRepository): UsersService {

    override suspend fun newUserRegister(email: String, pwd: String): SimpleResponse {

        var result = SimpleResponse(true, 0, "created")
        println("*** UsersServiceImpl newUserRegister email: $email")
        try {
            val repoResponse = usersRepository.registerUser(email, pwd)
            if(!repoResponse.result) {
                result = SimpleResponse(false, repoResponse.code, repoResponse.message)
            }

        }
        catch(e: Exception) {
            println("*** UsersServiceImpl newUserRegister Error: $e")
            result = SimpleResponse(false, 500, "Error ${e.message }" )
        }

        return result
    }

    override suspend fun logginUser(email: String, pwd: String): SimpleResponse {

        var result = SimpleResponse(true, 0, "logged")

        return result
    }
}