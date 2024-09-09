package com.alfred.myplanningbook.ui.common

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object CommonViewComp {
    //https://www.cdmon.com/es/apps/tabla-colores
    val c_navajowhite: Color = Color(0xFFF4A460)
    val c_snow: Color = Color(0xFFFFFAFA)
    val c_turquoise: Color = Color(0xFF40E0D0)
    val c_lightslategray: Color = Color(0xFFD8BFD8)
    val c_tres: Color = Color(0xFFC6A969)
    val c_cardContent: Color = Color(0xFF000080) //navy
    val c_cardContainer: Color = Color(0xFFFDF5E6) //oldlace
    val c_cardContentSecondary: Color = Color(0xFF000080) //
    val c_cardContainerSecondary: Color = Color(0xFFF4A0A0) //
    val c_card_buttonOneContainer: Color = Color(0xFFF1E4C3)
    val c_card_buttonOneContent: Color = Color(0xFF597E52)
    val c_card_buttonWarnContainer: Color = Color(0xFFFF4500) //orangeRed
    val c_card_buttonWarnContent: Color = Color(0xFFF5F5F5) //whitesmoke

    @Composable
    fun getActionsButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary

        )
    }

    @Composable
    fun getSecondaryButtonColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary

        )
    }

    @Composable
    fun gePlanningBookCardColour(): CardColors {
        return CardDefaults.cardColors(
            containerColor = c_cardContainer,
            contentColor = c_cardContent
        )
    }

    @Composable
    fun gePlanningBookCardSecondaryColour(): CardColors {
        return CardDefaults.cardColors(
            containerColor = c_cardContainerSecondary,
            contentColor = c_cardContentSecondary
        )
    }

    @Composable
    fun getPlanningBookCardButtonPrimaryColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = c_card_buttonOneContainer,
            contentColor = c_card_buttonOneContent
        )
    }

    @Composable
    fun getPlanningBookCardButtonSecondaryColour(): ButtonColors {
        return ButtonDefaults.outlinedButtonColors(
            containerColor = c_card_buttonWarnContainer,
            contentColor = c_card_buttonWarnContent
        )
    }

    @Composable
    fun getPlanningBookCardIconButtonPrimaryColour(): IconButtonColors {
        return IconButtonColors(
            containerColor = c_card_buttonOneContainer,
            contentColor = c_card_buttonOneContent,
            disabledContainerColor = c_card_buttonOneContainer,
            disabledContentColor = c_card_buttonOneContent
        )
    }

    @Composable
    fun getPlanningBookCardIconButtonSecondaryColour(): IconButtonColors {
        return IconButtonColors(
            containerColor = c_card_buttonWarnContainer,
            contentColor = c_card_buttonWarnContent,
            disabledContainerColor = c_card_buttonWarnContainer,
            disabledContentColor = c_card_buttonWarnContent
        )
    }
}