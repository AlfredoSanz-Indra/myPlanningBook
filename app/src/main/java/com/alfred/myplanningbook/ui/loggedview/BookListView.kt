package com.alfred.myplanningbook.ui.loggedview

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfred.myplanningbook.core.log.Klog

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class BookListView {

    @Composable
    fun createView(onLogout: () -> Unit) {

        Spacer(modifier = Modifier.width(20.dp))
        bookList(onLogout)
    }

    @Composable
    private fun bookList(onLogout: () -> Unit) {

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier.background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                OutlinedButton(modifier = Modifier.width(200.dp)
                    .height(70.dp),
                    colors = getLogoutButtonColour(),
                    onClick = {
                        Klog.line("BookListView", "bookList", "Logout button clicked")
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }

            }
        }
    }

    @Composable
    private fun getLogoutButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary

        )
    }

}