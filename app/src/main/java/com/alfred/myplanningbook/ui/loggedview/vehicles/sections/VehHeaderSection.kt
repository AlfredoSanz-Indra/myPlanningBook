package com.alfred.myplanningbook.ui.loggedview.vehicles.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.ActionButtonComponent
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object VehHeaderSection {
    @Composable
    fun show(onNew: () -> Unit, onBack: () -> Unit) {
        val viewModel: VehiclesViewModel = koinViewModel()
        val uiState: State<VehiclesUiState> = viewModel.uiState.collectAsStateWithLifecycle()

        rowActionButtons(onNew, onBack)
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
            ActionButtonComponent.show("New",
                                     CommonViewComp.getActionsButtonColour(),
                                     onClick = {
                                         onNewVeh()
                                     } )
            Spacer(Modifier.width(5.dp))
            ActionButtonComponent.show("Back",
                                     CommonViewComp.getSecondaryButtonColour(),
                                     onClick = {
                                         onBack()
                                     } )
        }//Row
    }
}