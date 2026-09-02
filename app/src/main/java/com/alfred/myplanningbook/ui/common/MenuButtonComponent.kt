package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object MenuButtonComponent {
    @Composable
    fun show(text: String, colors: ButtonColors, onClick: () -> Unit) {
        OutlinedButton(
            modifier = Modifier.width(200.dp)
                               .height(70.dp),
            colors = colors,
            onClick = {
                onClick()
            }
        ) {
            Text(text)
        }
    }
}