package com.alfred.myplanningbook.core.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object FirebaseSession {

    lateinit var auth: FirebaseAuth

    // Initialize Firebase Auth
    fun initFirebaseAuth() {
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        println("*** FirebaseSession initFirebaseAuth Using firebase with emulator")

        auth = Firebase.auth
    }

    fun isUserSigned(): Boolean {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            return true
        }
        return false
    }
}