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
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TasksManagerViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun taskListSection() {
    val viewModel: TasksManagerViewModel = koinViewModel()
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
                items( uiState.taskBookList.size, itemContent = { item ->
                    val taskBook = uiState.taskBookList[item]
                    TaskListCardComponent(taskBook)
                })
            } //lazy
        } //Box
    }
}

@Composable
private fun TaskListCardComponent(taskBook: TaskBook) {
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
            taskListCardComponentRowName(taskBook)

            Spacer(modifier = Modifier.height(10.dp))
            taskListCardComponentRowDesc(taskBook)

            Spacer(modifier = Modifier.height(10.dp))
            taskListCardComponentRowDays(taskBook)
        } //Column
    } //card
}

@Composable
private fun taskListCardComponentRowName(taskBook: TaskBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = taskBook.name,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column(Modifier.padding(4.dp))
        {
            taskListCardComponentButtonUpdate(taskBook)
        }
    }
}


@Composable
private fun taskListCardComponentButtonUpdate(taskBook: TaskBook) {
    val viewModel: TasksManagerViewModel = koinViewModel()

    OutlinedButton(modifier = Modifier
        .width(110.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardButtonPrimaryColour(),
        onClick = {
            viewModel.showTaskUpdateSection(taskBook)
        }
    ) {
        Text(
            "Update",
            style = MaterialTheme.typography.titleMedium,
        )
    } //button
}

@Composable
private fun taskListCardComponentRowDesc(taskBook: TaskBook) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = taskBook.description ?: "-",
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun taskListCardComponentRowDays(taskBook: TaskBook) {
    val viewModel: TasksManagerViewModel = koinViewModel()

    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = viewModel.formatTaskDateTime(taskBook),
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Column(Modifier.padding(4.dp))
        {
            Text(
                text = taskBook.dayOfWeekStr!!,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}