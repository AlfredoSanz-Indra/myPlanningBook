package com.alfred.myplanningbook.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.view.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class LoginView {

    @Composable
    fun createView(onBack: () -> Unit,  onLogin: () -> Unit, onReset: () -> Unit) {

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
                passwordUserData()

                Spacer(modifier = Modifier.height(30.dp))
                loginButton(onLogin)

                Spacer(modifier = Modifier.height(10.dp))
                resetPasswordButton(onReset)

                Spacer(modifier = Modifier.height(50.dp))
                backButton(onBack)
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: LoginViewModel = koinViewModel()
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

        val viewModel: LoginViewModel = koinViewModel()
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
    private fun passwordUserData() {

        val viewModel: LoginViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.pwd,
            visualTransformation =  PasswordVisualTransformation(),
            onValueChange = { viewModel.updatePwd(it) },
            label = { Text("Password") },
            placeholder = { Text ("Enter your password")}
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
    private fun loginButton(onLogin: () -> Unit) {

        val viewModel: LoginViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                viewModel.loginUser();
            }
        ) {
            Text("Login")
        }

        LaunchedEffect(uiState.isToLogin) {
            if(uiState.isToLogin) {
                onLogin()
            }
        }
    }

    @Composable
    private fun resetPasswordButton(onReset: () -> Unit) {

        TextButton(modifier = Modifier
            .width(150.dp)
            .height(50.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                Klog.line("LoginView", "resetPasswordButton", "reset password clicked")
                onReset()
            }
        ) {
            Text("Reset Password")
        }
    }

    @Composable
    private fun backButton(onBack: () -> Unit) {

        OutlinedButton(modifier = Modifier
            .width(110.dp)
            .height(50.dp),
            colors = CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                Klog.line("LoginView", "backButton", "Back button clicked")
                onBack()
            }
        ) {
            Text("Back")
        }
    }
}