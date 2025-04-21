package com.alfred.myplanningbook.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object TitleViewComponent {
    @Composable
    fun show(text: String) {
        Text(text,
             color = CommonViewComp.c_card_buttonOneContent,
             style = TextStyle(
                 fontSize = 20.sp,
                 background = CommonViewComp.c_snow
             )
        )
    }
}