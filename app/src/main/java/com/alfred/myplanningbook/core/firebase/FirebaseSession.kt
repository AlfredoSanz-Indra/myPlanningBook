package com.alfred.myplanningbook.core.firebase

import com.alfred.myplanningbook.core.log.Klog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object FirebaseSession {

    lateinit var auth: FirebaseAuth
    var useEmulator: Boolean = false


    // Initialize Firebase Auth
    fun initFirebaseAuth() {
        if(useEmulator) {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Klog.line("FirebaseSession", "initFirebaseAuth", "Using firebase with emulator")
        }

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