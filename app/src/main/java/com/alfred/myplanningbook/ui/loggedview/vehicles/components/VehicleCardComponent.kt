package com.alfred.myplanningbook.ui.loggedview.vehicles.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfallPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object VehicleCardComponent {

    @Composable
    fun show(veh: Vehicle, onEdit: () -> Unit,  onNavigate: () -> Unit) {
        OutlinedCard(
            modifier = Modifier
                .padding(vertical = 3.dp)
                .fillMaxWidth()
                .height(90.dp)
                .waterfallPadding(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color(0xFF8387c2),
                contentColor = Color.Black,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.Black
            ),
            elevation = CardDefaults.outlinedCardElevation(),
            border = BorderStroke(1.dp, Color.White),
        )
        {
            Column(
                Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            )
            {
                Spacer(modifier = Modifier.height(5.dp))
                rowOne(veh)
                Spacer(modifier = Modifier.height(20.dp))
                rowTwo(veh, onEdit, onNavigate)
            } //Column
        } //card
    }

    @Composable
    private fun rowOne(veh: Vehicle) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 2.dp),
        ) {
            Column(modifier = Modifier
                .padding(horizontal = 0.dp)
                .fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {
                Text(
                    text = "${veh.name} ${veh.model}",
                    style = TextStyle(
                        color = Color.White
                    ),
                )
            }
        }
    }

    @Composable
    private fun rowTwo(veh: Vehicle,
                       onEdit: () -> Unit,
                       onNavigate: () -> Unit) {
        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 20.dp, vertical = 2.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.Start) {

                Row(horizontalArrangement = Arrangement.Start,) {
                    VehicleCardButton.show("Edit",
                                            Color(0xFF35682d),
                                            90.dp,
                                            Icons.Filled.Edit,
                                            onClick = {
                                                onEdit()
                                            }
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth(1f),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.End) {

                Row(horizontalArrangement = Arrangement.End,) {
                    VehicleCardButton.show("Maint list",
                                           Color(0xFF35682d),
                                           140.dp,
                                           Icons.Filled.East,
                                           onClick = {
                                               onNavigate()
                                           }
                    )

                }
            }
        }
    }
}