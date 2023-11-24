package com.alfred.myplanningbook.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.alfred.myplanningbook.NavigationGraph
import com.alfred.myplanningbook.core.firebase.FirebaseSession

/**
 * @author Alfredo Sanz
 * @time 2023
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userSigned = FirebaseSession.isUserSigned()
        println("*** MainActivity onCreate userSigned= $userSigned")

        setContent {
            MaterialTheme {
                NavigationGraph()
            }
        }
    }
}
