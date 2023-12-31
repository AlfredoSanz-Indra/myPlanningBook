package com.alfred.myplanningbook.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.alfred.myplanningbook.NavigationGraph
import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import org.koin.compose.KoinContext

/**
 * @author Alfredo Sanz
 * @time 2023
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userSigned = FirebaseSession.isUserSigned()
        Klog.line("MainActivity", "onCreate", "userSigned: $userSigned")
        val userSigned2 = FirebaseSession.isUserSignedAndValidated()
        Klog.line("MainActivity", "onCreate", "userSigned2: $userSigned2")

        setContent {
            MaterialTheme {
                KoinContext() {
                    NavigationGraph()
                }
            }
        }
    }
}
