package com.alfred.myplanningbook.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfred.myplanningbook.core.firebase.FirebaseSession

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class MainView() {

    @Composable
    fun createView(onLogin: () -> Unit, onRegister: () -> Unit) {

        val userSigned = FirebaseSession.isUserSigned()
        println("**MainView createView userSigned= $userSigned")

        buttonsPrimaryActions(onLogin, onRegister)
    }

    @Composable
    private fun buttonsPrimaryActions(onLogin: () -> Unit, onRegister: () -> Unit) {

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                Arrangement.Center,
                Alignment.CenterHorizontally,
            ) {

                OutlinedButton(modifier = Modifier.width(200.dp)
                    .height(70.dp),
                    colors = getLoginButtonColour(),
                    onClick = {
                        println("*** Login button clicked")
                        onLogin()
                    }
                ) {
                    Text("Login")
                }


                Spacer(modifier = Modifier.height(50.dp))

                FilledTonalButton(modifier = Modifier.width(200.dp)
                    .height(70.dp),
                    colors = getRegisterButtonColour(),
                    onClick = {
                        println("*** Register button clicked")
                        onRegister()
                    }
                ) {
                    Text("Register")
                }

            }
        }
    }

    @Composable
    private fun getLoginButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary

        )
    }

    @Composable
    private fun getRegisterButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary
        )
    }
}