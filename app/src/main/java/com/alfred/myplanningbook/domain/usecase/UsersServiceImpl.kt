package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.firebase.FirebaseSession
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
            result = if(repoResponse.result) {
                val repoResponseVerif = usersRepository.sendEmailVerification()
                if(repoResponseVerif.result) {
                    SimpleResponse(true, repoResponseVerif.code, repoResponseVerif.message, "")
                }
                else {
                    SimpleResponse(false, repoResponse.code, repoResponse.message, "")
                }
            }
            else {
                SimpleResponse(false, repoResponse.code, repoResponse.message, "")
            }
        }
        catch(e: FirebaseAuthException) {
            Klog.line("UsersServiceImpl", "newUserRegister", "FirebaseAuthException localizedMessage: ${e.localizedMessage}")
            Klog.line("UsersServiceImpl", "newUserRegister", "FirebaseAuthException errorCode: ${e.errorCode}")

            result = SimpleResponse(false, 400, e.localizedMessage, e.errorCode )
        }
        catch(e: Exception) {
            Klog.line("UsersServiceImpl", "newUserRegister", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("UsersServiceImpl", "newUserRegister", " result: $result")
        return result
    }

    override suspend fun logginUser(email: String, pwd: String): SimpleResponse {

        var result: SimpleResponse

        try {
            val repoResponse = usersRepository.logginUser(email, pwd)
            result = if(repoResponse.result) {
                if(FirebaseSession.isUserSignedAndValidated()) {
                    SimpleResponse(true, repoResponse.code, repoResponse.message, "")
                }
                else {
                    SimpleResponse(false, 404, "User email not validated", "")
                }
            }
            else {
                SimpleResponse(false, repoResponse.code, repoResponse.message, "")
            }
        }
        catch(e: FirebaseAuthException) {
            Klog.line("UsersServiceImpl", "logginUser", "FirebaseAuthException localizedMessage: ${e.localizedMessage}")
            Klog.line("UsersServiceImpl", "logginUser", "FirebaseAuthException errorCode: ${e.errorCode}")

            result = SimpleResponse(false, 400, "Loggin Failed", e.errorCode )
        }
        catch(e: Exception) {
            Klog.line("UsersServiceImpl", "logginUser", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("UsersServiceImpl", "logginUser", " result: $result")
        return result
    }

    override suspend fun logoutUser(): SimpleResponse {

        var result: SimpleResponse

        try {
            val repoResponse = usersRepository.logoutUser()
            result = if(repoResponse.result) {
                SimpleResponse(true, repoResponse.code, repoResponse.message, "")
            }
            else {
                SimpleResponse(false, repoResponse.code, repoResponse.message, "")
            }
        }
        catch(e: FirebaseAuthException) {
            Klog.line("UsersServiceImpl", "logoutUser", "FirebaseAuthException localizedMessage: ${e.localizedMessage}")
            Klog.line("UsersServiceImpl", "logoutUser", "FirebaseAuthException errorCode: ${e.errorCode}")

            result = SimpleResponse(false, 400, "Logout Failed", e.errorCode )
        }
        catch(e: Exception) {
            Klog.line("UsersServiceImpl", "logoutUser", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("UsersServiceImpl", "logoutUser", " result: $result")
        return result
    }

    override suspend fun sendResetEmail(email: String): SimpleResponse {
        var result: SimpleResponse

        try {
            val repoResponse = usersRepository.sendResetEmail(email)
            result = if(repoResponse.result) {
                SimpleResponse(true, repoResponse.code, repoResponse.message, "")
            }
            else {
                SimpleResponse(false, repoResponse.code, repoResponse.message, "")
            }
        }
        catch(e: FirebaseAuthException) {
            Klog.line("UsersServiceImpl", "sendResetEmail", "FirebaseAuthException localizedMessage: ${e.localizedMessage}")
            Klog.line("UsersServiceImpl", "sendResetEmail", "FirebaseAuthException errorCode: ${e.errorCode}")

            when(e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> result = SimpleResponse(false, 400, "Email not found", e.errorCode )
                else -> result = SimpleResponse(false, 400, e.errorCode, e.errorCode )
            }

        }
        catch(e: Exception) {
            Klog.line("UsersServiceImpl", "sendResetEmail", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("UsersServiceImpl", "sendResetEmail", " result: $result")
        return result
    }
}