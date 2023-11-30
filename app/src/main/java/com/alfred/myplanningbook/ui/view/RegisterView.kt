package com.alfred.myplanningbook.ui.view

import android.graphics.fonts.FontStyle
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.view.viewmodel.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class RegisterView {

    @Composable
    fun createView(onBack: () -> Unit,
                   onRegister: () -> Unit) {

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                errorGeneralField()
                emailUserData()
                passwordsUserData()

                Spacer(modifier = Modifier.height(30.dp))
                registerButton(onRegister)

                Spacer(modifier = Modifier.height(10.dp))
                backButton(onBack)
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: RegisterViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.generalError) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                uiState.generalErrorText,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    private fun emailUserData() {

        val viewModel: RegisterViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            placeholder = { Text ("Enter your valid Email")}
        )

        if(uiState.emailError) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                uiState.emailErrorText,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )
        }
    }

    @Composable
    private fun passwordsUserData() {

        val viewModel: RegisterViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.pwd01,
            visualTransformation =  PasswordVisualTransformation(),
            onValueChange = { viewModel.updatePwd01(it) },
            label = { Text("Password") },
            placeholder = { Text ("Enter a new password")}
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.pwd02,
            visualTransformation =  PasswordVisualTransformation(),
            onValueChange = { viewModel.updatePwd02(it) },
            label = { Text("Password") },
            placeholder = { Text ("Repeat the new password")}
        )

        if(uiState.pwdError) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                uiState.pwdErrorText,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )
        }
    }

    @Composable
    private fun registerButton(onRegister: () -> Unit) {

        val viewModel: RegisterViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = getRegisterButtonColour(),
            onClick = {
                Klog.line("RegisterView", "registerButton", "register clicked")
                viewModel.registerUser();
            }
        ) {
            Text("Register")
        }

        if(uiState.registerAction) {
            onRegister()
        }
    }

    @Composable
    private fun backButton(onBack: () -> Unit) {

        OutlinedButton(modifier = Modifier
            .width(110.dp)
            .height(50.dp),
            colors = getBackButtonColour(),
            onClick = {
                Klog.line("RegisterView", "backButton", "Back button clicked")
                onBack()
            }
        ) {
            Text("Back")
        }
    }

    @Composable
    private fun getRegisterButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary

        )
    }

    @Composable
    private fun getBackButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary

        )
    }
}