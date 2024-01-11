package com.alfred.myplanningbook.core.firebase

import com.alfred.myplanningbook.core.log.Klog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object FirebaseSession {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var useEmulator: Boolean = false


    // Initialize Firebase Auth
    fun initFirebaseAuth() {
        if(useEmulator) {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Klog.line("FirebaseSession", "initFirebaseAuth", "Using firebase Auth with emulator")
        }

        auth = Firebase.auth
        Klog.line("FirebaseSession", "initFirebaseAuth", "Firebase Auth initialized")
    }

    fun initFirebaseDB() {
        if(useEmulator) {
            Firebase.auth.useEmulator("10.0.2.2", 5003)
            Klog.line("FirebaseSession", "initFirebaseDB", "Using firebase Firestore with emulator")
        }

        db = Firebase.firestore
        Klog.line("FirebaseSession", "initFirebaseDB", "Firebase Firestore initialized with db name: ${db.app.name}")
    }

    fun isUserSigned(): Boolean {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            return true
        }
        return false
    }

    fun isUserSignedAndValidated() : Boolean {

        var result = false

        val currentUser = auth.currentUser
        if(currentUser != null && currentUser.isEmailVerified) {
            result = true
        }

        return result
    }
}