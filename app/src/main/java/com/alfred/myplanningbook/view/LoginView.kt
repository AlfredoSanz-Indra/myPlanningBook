package com.alfred.myplanningbook.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class LoginView: IView {

    @Composable
    private fun getLoginButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF41521F),
            contentColor = Color(0xFFEFF2EF),
            disabledContentColor = Color(0xFF41521F)
        )
    }

    @Preview
    @Composable
    override fun createView() {

        Spacer(modifier = Modifier.width(20.dp))
        buttonDoLogin()
    }

    @Composable
    private fun buttonDoLogin() {
        OutlinedButton(modifier = Modifier.width(200.dp)
                                          .height(70.dp),
            colors = getLoginButtonColour(),
            onClick = {
                println("button clicked")
            }
        ) {
            Text("Login")
        }
    }

}