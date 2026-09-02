package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object ErrorGeneralField {
    @Composable
    fun showRow(errorText: String) {
        Row(
            Modifier.background(color = Color(0xFFf7f6ff))
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(10.dp)
                                      .width(20.dp))
            Text(errorText,
                 color = Color.Red,
                 style = TextStyle(fontSize = 15.sp, color = Color.Red)
            )
        }
    }
}