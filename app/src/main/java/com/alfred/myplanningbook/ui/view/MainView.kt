package com.alfred.myplanningbook.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.view.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class MainView() {

    @Composable
    fun createView(onLogin: () -> Unit, onRegister: () -> Unit, onBook: () -> Unit) {

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

                buttonsPrimaryActions(onLogin, onRegister, onBook)
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: MainViewModel = koinViewModel()
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
    private fun buttonsPrimaryActions(onLogin: () -> Unit, onRegister: () -> Unit, onBook: () -> Unit) {

        val viewModel: MainViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier.width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
                Klog.line("MainView", "buttonsPrimaryActions", "Login button clicked")
                viewModel.loginAction()
            }
        ) {
            Text("Login")
        }

        LaunchedEffect(uiState.isToLogin) {
            if(uiState.isToLogin) {
                viewModel.clearFields()
                onLogin()
            }
        }
        LaunchedEffect(uiState.isToBook) {
            if(uiState.isToBook) {
                viewModel.clearFields()
                onBook()
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        FilledTonalButton(modifier = Modifier.width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getSecondaryButtonColour(),
            onClick = {
                Klog.line("MainView", "buttonsPrimaryActions", "Register button clicked")
                onRegister()
            }
        ) {
            Text("Register")
        }
    }
}