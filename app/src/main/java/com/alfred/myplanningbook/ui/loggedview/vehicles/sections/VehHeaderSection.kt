package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.MenuButtonComponent
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object VehHeaderSection {
    @Composable
    fun showSection() {

        val _uiState = MutableStateFlow(VehiclesUiState())
        val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()

        rowActionButtons(onNewVeh = {}, onBack = {})
    }

    @Composable
    private fun rowActionButtons(onNewVeh: () -> Unit, onBack: () -> Unit) {
        Row(
            Modifier.background(color = Color(0xFFf7f6ff))
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(20.dp))
            MenuButtonComponent.show("New",
                                     CommonViewComp.getActionsButtonColour(),
                                     onClick = {

                                     } )
            Spacer(Modifier.width(20.dp))
            MenuButtonComponent.show("Back",
                                     CommonViewComp.getSecondaryButtonColour(),
                                     onClick = {

                                     } )
        }//Row
    }
}