package com.alfred.myplanningbook.ui.loggedview.vehicles.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * @author Alfredo Sanz
 * @time 2024
 */
object VehicleCardButton {

    @Composable
    fun show(desc: String, mainColor: Color, size: Dp, icon: ImageVector, onClick: () -> Unit) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val color = if (isPressed) getColor(Color(0xFF5f7f7a)) else getColor(mainColor)
        val borderColor = if (isPressed) Color.Black else Color(0xFF666699)

        OutlinedButton(modifier = Modifier.width(size).height(34.dp),
            colors = color,
            border = ButtonDefaults.outlinedButtonBorder(true).copy(brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(borderColor, borderColor))),
            interactionSource = interactionSource,
            onClick = {
                onClick()
            }
        )
        {
            Text(text=desc, textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterVertically))
            Icon(imageVector = icon, contentDescription = desc, Modifier.size(25.dp))
        }
    }


    @Composable
    private fun getColor(mainColor: Color): ButtonColors  {
        return ButtonColors(containerColor = mainColor,
            contentColor = Color(0xFFfafbfd),
            disabledContentColor = Color(0XFFe83151),
            disabledContainerColor = Color(0XFFe83151)
        )
    }
}