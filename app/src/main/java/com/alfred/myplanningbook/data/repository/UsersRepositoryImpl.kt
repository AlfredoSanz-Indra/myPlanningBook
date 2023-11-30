package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class UsersRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): UsersRepository {

    override suspend fun registerUser(email: String, password: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 100, "not registered")
        Klog.line("UsersRepositoryImpl", "registerUser", "email: $email")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<AuthResult> = FirebaseSession.auth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    val user = FirebaseSession.auth.currentUser
                    taskResp = SimpleDataResponse(true, 200, "Registered - ${user?.uid}")
                }
                else {
                    Klog.line("UsersRepositoryImpl", "registerUser", "error cause: ${task.exception?.cause}")
                    Klog.line("UsersRepositoryImpl", "registerUser", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 300, "Register user failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("UsersRepositoryImpl", "registerUser", "result-end: $result")
        return result
    }

    override fun logginUser(email: String, password: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 100, "not logged")

        FirebaseSession.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                task ->
                    if(task.isSuccessful) {
                        Klog.line("UsersRepositoryImpl", "logginUser", "user logged")

                        val user = FirebaseSession.auth.currentUser
                        result = SimpleDataResponse(true, 200, "logged - ${user?.uid}")
                    }
                    else {
                        Klog.line("UsersRepositoryImpl", "logginUser", "Error logging")
                        result = SimpleDataResponse(false, 300, "Authentication failed.")
                    }
            }

        return result
    }

}