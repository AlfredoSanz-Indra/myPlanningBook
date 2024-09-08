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
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.model.TaskBookNatureEnum
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.TasksManagerViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListSection() {
    val viewModel: TasksManagerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.isToDeleteTask) {
        alertDialogDeleteTask()
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
            TaskListCardComponentRowName(taskBook)

            Spacer(modifier = Modifier.height(10.dp))
            TaskListCardComponentRowDesc(taskBook)

            Spacer(modifier = Modifier.height(10.dp))
            TaskListCardComponentRowDate(taskBook)
            TaskListCardComponentRowTime(taskBook)
        } //Column
    } //card
}

@Composable
private fun TaskListCardComponentRowName(taskBook: TaskBook) {
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
            TaskListCardComponentButtonUpdate(taskBook)
        }

        if(taskBook.nature != TaskBookNatureEnum.IS_ACTIVITY) {
            Column(Modifier.padding(4.dp)) {
                TaskListCardComponentButtonDelete(taskBook)
            }
        }
    }
}

@Composable
private fun TaskListCardComponentButtonUpdate(taskBook: TaskBook) {
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
private fun TaskListCardComponentButtonDelete(taskBook: TaskBook) {
    val viewModel: TasksManagerViewModel = koinViewModel()

    OutlinedButton(modifier = Modifier
        .width(110.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
        onClick = {
            Klog.line("taskListSection","taskListCardComponentButtonDelete","delete task button clicked")
            viewModel.confirmDeleteTask(taskBook, true)
        }
    ) {
        Text(
            "Delete",
            style = MaterialTheme.typography.titleMedium,
        )
    } //button
}

@Composable
private fun TaskListCardComponentRowDesc(taskBook: TaskBook) {
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
private fun TaskListCardComponentRowDate(taskBook: TaskBook) {
    val viewModel: TasksManagerViewModel = koinViewModel()

    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.weight(1F),
            Arrangement.Top,
            Alignment.Start)
        {
            Text(
                text = viewModel.formatTaskDate(taskBook),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun TaskListCardComponentRowTime(taskBook: TaskBook) {
    val viewModel: TasksManagerViewModel = koinViewModel()

    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column()
        {
            Text(
                text = viewModel.formatTaskTime(taskBook),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Column()
        {
            Text(
                text = taskBook.dayOfWeekStr!!,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun alertDialogDeleteTask() {
    val viewModel: TasksManagerViewModel = koinViewModel()

    BasicAlertDialog(
        onDismissRequest = {
            viewModel.confirmDeleteTask(null, false )
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
                    text = "You are about to delete a Task.  Do you want to continue?",
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
                            Klog.line("taskListSection","alertDialogDeleteTask","confirm delete button clicked")
                            viewModel.deleteTask()
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
                            Klog.line("taskListSection","alertDialogDeleteTask","cancel delete button clicked")
                            viewModel.confirmDeleteTask(null, false)
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