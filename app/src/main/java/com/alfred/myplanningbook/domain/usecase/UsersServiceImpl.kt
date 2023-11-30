package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import com.google.firebase.auth.FirebaseAuthException

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class UsersServiceImpl(private val usersRepository: UsersRepository): UsersService {

    override suspend fun newUserRegister(email: String, pwd: String): SimpleResponse {

        var result: SimpleResponse

        try {
            val repoResponse = usersRepository.registerUser(email, pwd)
            if(repoResponse.result) {
                result = SimpleResponse(true, repoResponse.code, repoResponse.message, "")
            }
            else {
                result = SimpleResponse(false, repoResponse.code, repoResponse.message, "")
            }
        }
        catch(e: FirebaseAuthException) {
            Klog.line("UsersServiceImpl", "newUserRegister", "FirebaseAuthException localizedMessage: ${e.localizedMessage}")
            Klog.line("UsersServiceImpl", "newUserRegister", "FirebaseAuthException localizedMessage: ${e.errorCode}")

            result = SimpleResponse(false, 400, e.localizedMessage, e.errorCode )
        }
        catch(e: Exception) {
            Klog.line("UsersServiceImpl", "newUserRegister", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("UsersServiceImpl", "newUserRegister", " result: ${result}")
        return result
    }

    override suspend fun logginUser(email: String, pwd: String): SimpleResponse {

        var result = SimpleResponse(true, 0, "logged", "")

        return result
    }
}