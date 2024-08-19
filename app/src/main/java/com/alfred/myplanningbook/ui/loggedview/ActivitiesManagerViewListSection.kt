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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.ActivitiesManagerViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun activitiesListSection() {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    activityListCardComponent(activityBook)
                })
            } //lazy
        } //Box
    }
}

@Composable
private fun activityListCardComponent(activityBook: ActivityBook) {
    OutlinedCard(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(150.dp)
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
            activityListCardComponentRowName(activityBook)

            Spacer(modifier = Modifier.height(10.dp))
            activityListCardComponentRowDesc(activityBook)

            Spacer(modifier = Modifier.height(10.dp))
            activityListCardComponentRowDate(activityBook)
        } //Column
    } //card
}

@Composable
private fun activityListCardComponentRowName(activityBook: ActivityBook) {
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
            activityListCardComponentButtonUpdate(activityBook)
        }
    }
}


@Composable
private fun activityListCardComponentButtonUpdate(activityBook: ActivityBook) {
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
private fun activityListCardComponentRowDesc(activityBook: ActivityBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = activityBook.description ?: "-",
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun activityListCardComponentRowDate(activityBook: ActivityBook) {
    val viewModel: ActivitiesManagerViewModel = koinViewModel()

    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {

        }
    }
}