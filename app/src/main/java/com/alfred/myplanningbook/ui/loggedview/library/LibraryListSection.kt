package com.alfred.myplanningbook.ui.loggedview.library

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
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
import com.alfred.myplanningbook.domain.model.library.Book
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.library.viewmodel.LibraryViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryListSection() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.isToDeleteBook) {
        AlertDialogDeleteBook()
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
                items( uiState.bookList.size, itemContent = { item ->
                    val book = uiState.bookList[item]
                    LibraryListCardComponent(book)
                })
            } //lazy
        } //Box
    }
}

@Composable
private fun LibraryListCardComponent(book: Book) {
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedCard(
        modifier = Modifier
            .padding(vertical = 0.dp)
            .fillMaxWidth()
            .height(130.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = when(book.have) {
                "y" -> CommonViewComp.getLibraryCardColour()
                else ->CommonViewComp.getLibraryCardSecondaryColour()
            },
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
            LibraryListCardComponentRowName(book)

            Spacer(modifier = Modifier.height(1.dp))
            LibraryListCardComponentRowDesc(book)

            Spacer(modifier = Modifier.height(1.dp))
            LibraryListCardComponentRowSaga(book)
        } //Column
    } //card
}


@Composable
private fun LibraryListCardComponentRowName(book: Book) {
    Row (
        Modifier.fillMaxWidth().height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(modifier = Modifier
            .padding(4.dp)
            .weight(0.7F))
        {
            Text(
                text = book.title!!,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Column(Modifier.padding(4.dp))
        {
            LibraryListCardComponentButtonUpdate(book)
        }
        Column(Modifier.padding(4.dp))
        {
            LibraryListCardComponentButtonDelete(book)
        }
    }
}

@Composable
private fun LibraryListCardComponentButtonUpdate(book: Book) {
    val viewModel: LibraryViewModel = koinViewModel()

    OutlinedIconButton(modifier = Modifier
        .width(35.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardIconButtonPrimaryColour(),
        onClick = {
            viewModel.showUpdateBook(true, book)
        }
    ) {
        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit book", Modifier.size(25.dp))
    } //button
}

@Composable
private fun LibraryListCardComponentButtonDelete(book: Book) {
    val viewModel: LibraryViewModel = koinViewModel()

    OutlinedIconButton(modifier = Modifier
        .width(35.dp)
        .height(35.dp),
        colors = CommonViewComp.getPlanningBookCardIconButtonSecondaryColour(),
        onClick = {
            Klog.line("libraryListSection","LibraryListCardComponentButtonDelete","delete book button clicked")
            viewModel.confirmDeleteBook(book, true)
        },
    ) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete book", Modifier.size(25.dp))
    } //button
}

@Composable
private fun LibraryListCardComponentRowDesc(book: Book) {
    Row (
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Column(Modifier.padding(4.dp))
        {
            Text(
                text = book.authorName!!,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun LibraryListCardComponentRowSaga(book: Book) {
    var saga = if(!book.sagaName.isNullOrBlank()) book.sagaName else ""
    saga += if(book.sagaIndex != null && book.sagaIndex > 0) " (${book.sagaIndex})" else ""

    var isRead = if(book.read == "y") "Read" else "Not Read"
    var isMine = if(book.have == "y") "Got it" else "Wish List"

    Row(
        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.padding(4.dp)) {
            Text(
                text = saga,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Column(Modifier.padding(4.dp)) {
            Text(
                text = isRead,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Column(Modifier.padding(4.dp)) {
            Text(
                text = isMine,
                style = MaterialTheme.typography.titleSmall,
            )
        }

    }
}

@ExperimentalMaterial3Api
@Composable
private fun AlertDialogDeleteBook() {
    val viewModel: LibraryViewModel = koinViewModel()

    BasicAlertDialog(
        onDismissRequest = {
            viewModel.confirmDeleteBook(null, false )
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
                    text = "You are about to delete a Book.  Do you want to continue?",
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
                            Klog.line("LibraryListSection","AlertDialogDeleteBook","confirm delete button clicked")
                            viewModel.deleteBook()
                        }
                    ) {
                        Text(
                            "Confirm",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    } //button

                    OutlinedButton(modifier = Modifier
                        .width(110.dp)
                        .height(35.dp),
                        colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
                        onClick = {
                            Klog.line("LibraryListSection","AlertDialogDeleteBook","cancel delete button clicked")
                            viewModel.confirmDeleteBook(null, false)
                        }
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    } //button
                }
            }
        }
    }
}