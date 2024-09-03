package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.ActivitiesManagerViewModel
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TasksManagerViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesListSection() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.isToDeleteActivity) {
        AlertDialogDeleteActivity()
    }

    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp)
            ) {
                //@see https://developer.android.com/codelabs/basic-android-compose-training-add-scrollable-list?hl=es-419#2
                items( uiState.activityBookList.size, itemContent = { item ->
                    val activityBook = uiState.activityBookList[item]
                    ActivityListCardComponent(activityBook)
                })
            } //lazy
        } //Box
    }
}

@Composable
private fun ActivityListCardComponent(activityBook: ActivityBook) {
    OutlinedCard(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(170.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CommonViewComp.gePlanningBookCardColour(),
        elevation = CardDefaults.outlinedCardElevation(),
        border = CardDefaults.outlinedCardBorder(),
    )
    {
        Column(
            Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start)
        {
            ActivityListCardComponentRowName(activityBook)

            Spacer(modifier = Modifier.height(10.dp))
            ActivityListCardComponentRowDesc(activityBook)

            Spacer(modifier = Modifier.height(10.dp))
            ActivityListCardComponentRowDays(activityBook)

            Spacer(modifier = Modifier.height(5.dp))
            ActivityListCardComponentRowHours(activityBook)
        } //Column
    } //card
}

@Composable
private fun ActivityListCardComponentRowName(activityBook: ActivityBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = activityBook.name,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column(Modifier.padding(4.dp))
        {
            ActivityListCardComponentButtonUpdate(activityBook)
        }
        Column(Modifier.padding(4.dp))
        {
            ActivityListCardComponentButtonDelete(activityBook)
        }
    }
}


@Composable
private fun ActivityListCardComponentButtonUpdate(activityBook: ActivityBook) {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

    OutlinedButton(modifier = Modifier
        .width(110.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardButtonPrimaryColour(),
        onClick = {
            viewModel.showActivityUpdateSection(activityBook)
        }
    ) {
        Text(
            "Update",
            style = MaterialTheme.typography.titleMedium,
        )
    } //button
}

@Composable
private fun ActivityListCardComponentButtonDelete(activityBook: ActivityBook) {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

    OutlinedButton(modifier = Modifier
        .width(110.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
        onClick = {
            Klog.line("ActivitiesListSection","ActivityListCardComponentButtonDelete","delete Activity button clicked")
            viewModel.confirmDeleteActivity(activityBook, true)
        }
    ) {
        Text(
            "Delete",
            style = MaterialTheme.typography.titleMedium,
        )
    } //button
}

@Composable
private fun ActivityListCardComponentRowDesc(activityBook: ActivityBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = activityBook.getDescriptionInShort(),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun ActivityListCardComponentRowDays(activityBook: ActivityBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = activityBook.getStringWeekDaysList() ,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun ActivityListCardComponentRowHours(activityBook: ActivityBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = "De ${activityBook.getFormattedStartTime()} a  ${activityBook.getFormattedEndTime()}",
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}


@ExperimentalMaterial3Api
@Composable
private fun AlertDialogDeleteActivity() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

    BasicAlertDialog(
        onDismissRequest = {
            viewModel.confirmDeleteActivity(null, false )
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "You are about to delete an Activity.  Do you want to continue?",
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(modifier = Modifier
                        .width(110.dp)
                        .height(35.dp),
                        colors = CommonViewComp.getPlanningBookCardButtonPrimaryColour(),
                        onClick = {
                            Klog.line("activitiesListSection","alertDialogDeleteActivity","confirm delete button clicked")
                            viewModel.deleteActivity()
                        }
                    ) {
                        Text(
                            "Confirm",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }//button

                    OutlinedButton(modifier = Modifier
                        .width(110.dp)
                        .height(35.dp),
                        colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
                        onClick = {
                            Klog.line("activitiesListSection","alertDialogDeleteActivity","cancel delete button clicked")
                            viewModel.confirmDeleteActivity(null, false)
                        }
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }//button
                }
            }
        }
    }
}