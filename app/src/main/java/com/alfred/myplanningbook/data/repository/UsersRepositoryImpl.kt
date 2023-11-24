package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class UsersRepositoryImpl: UsersRepository {

    override fun registerUser(email: String, password: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 100, "not registered")
        println("*** UsersRepositoryImpl registerUser email: $email")
        FirebaseSession.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                task ->
                    if(task.isSuccessful) {
                        println("*** UsersRepositoryImpl registerUser  user registered")
                        val user = FirebaseSession.auth.currentUser
                        result = SimpleDataResponse(true, 200, "Registered - ${user?.uid}")
                    }
                    else {
                        println("*** UsersRepositoryImpl registerUser  Error registering user")
                        println("*** UsersRepositoryImpl registerUser  task.result= ${task.result}")

                        result = SimpleDataResponse(false, 300, "Register user failed.")
                    }
                }

        return result
    }

    override fun logginUser(email: String, password: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 100, "not logged")

        FirebaseSession.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                task ->
                    if(task.isSuccessful) {
                        println("*** UsersRepositoryImpl logginUser  user logged")
                        val user = FirebaseSession.auth.currentUser
                        result = SimpleDataResponse(true, 200, "logged - ${user?.uid}")
                    }
                    else {
                        println("*** UsersRepositoryImpl logginUser  Error logging")
                        result = SimpleDataResponse(false, 300, "Authentication failed.")
                    }
            }

        return result
    }

}