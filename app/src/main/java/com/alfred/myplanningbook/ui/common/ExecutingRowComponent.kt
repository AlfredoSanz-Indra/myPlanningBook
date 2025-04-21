package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object ExecutingRowComponent {

    @Composable
    fun show(text: String) {
        Row(
            Modifier
                .background(color = Color(0xFFf7f6ff))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(10.dp).width(20.dp))

            Text(
                text ="$text . . .",
                color = Color(0xFF33812a),
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color(0xFF33812a),
                )
            )

            Spacer(modifier = Modifier.height(0.dp).width(10.dp))

            CircularProgressIndicator()
        }
    }
}