package com.alfred.myplanningbook.ui.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class LoginView {

    @Composable
    fun createView(onBack: () -> Unit) {

        Spacer(modifier = Modifier.width(20.dp))
        buttonDoLogin(onBack)
    }

    @Composable
    private fun buttonDoLogin(onBack: () -> Unit) {
        val ctx: Context = LocalContext.current

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterHorizontally,
            ) {

                OutlinedButton(modifier = Modifier.width(200.dp)
                    .height(70.dp),
                    colors = getLoginButtonColour(),
                    onClick = {
                        println("*** Login button to back clicked")
                        onBack()
                    }
                ) {
                    Text("Back to main")
                }
            }
        }
    }

    @Composable
    private fun getLoginButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    /*
    private fun doLogin(ctx: Context) {
        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null) {
            Toast.makeText(ctx, "Tengo user", Toast.LENGTH_SHORT)
            println("***  User OK")
        }
        else {
            Toast.makeText(ctx, "No hay user", Toast.LENGTH_SHORT)
            println("***  NO USER")
        }
    }
*/
}