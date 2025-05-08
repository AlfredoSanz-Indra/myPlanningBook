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
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailUiState
import com.alfred.myplanningbook.ui.loggedview.vehicles.viewmodel.VehiclesDetailViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object VehDetailHeaderSection {
    @Composable
    fun show(onSave: () -> Unit, onCancel: () -> Unit) {
        val viewModel: VehiclesDetailViewModel = koinViewModel()
        val uiState: State<VehiclesDetailUiState> = viewModel.uiState.collectAsStateWithLifecycle()

        rowActionButtons(onSave, onCancel)
    }

    @Composable
    private fun rowActionButtons(onSave: () -> Unit, onCancel: () -> Unit) {
        Row(
            Modifier.background(color = Color(0xFFf7f6ff))
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(20.dp))
            ActionButtonComponent.show("Save",
                                     CommonViewComp.getActionsButtonColour(),
                                     onClick = {
                                        onSave()
                                     } )
            Spacer(Modifier.width(20.dp))
            ActionButtonComponent.show("Cancel",
                                     CommonViewComp.getSecondaryButtonColour(),
                                     onClick = {
                                         onCancel()
                                     } )
        }//Row
    }
}