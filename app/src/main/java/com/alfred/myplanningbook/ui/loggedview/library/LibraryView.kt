package com.alfred.myplanningbook.ui.loggedview.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.common.ErrorGeneralField
import com.alfred.myplanningbook.ui.common.ExecutingRowComponent
import com.alfred.myplanningbook.ui.common.TitleViewComponent
import com.alfred.myplanningbook.ui.loggedview.library.viewmodel.LibraryViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class LibraryView {

    @Composable
    fun createView(onBack: () -> Unit) {
        val viewModel: LibraryViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.isDesiredBookListLoaded) {
            if(!uiState.isDesiredBookListLoaded) {
                viewModel.loadDesiredBookList()
            }
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                if(uiState.isDesiredBookListLoading) {
                    Spacer(modifier = Modifier.height(30.dp))
                    ExecutingRowComponent.show("Loading ...")
                }
                else {
                    Spacer(modifier = Modifier.height(30.dp))
                    if(uiState.generalError) {
                        Spacer(modifier = Modifier.height(10.dp))
                        ErrorGeneralField.showRow(uiState.generalErrorText)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    TitleViewComponent.show(uiState.headerMessage)

                    Spacer(modifier = Modifier.height(30.dp))
                    libraryBodySection(onBack)
                }
            }
        }
    }

    @Composable
    private fun libraryBodySection(onBack: () -> Unit) {
        val viewModel: LibraryViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToAddBook) {
            LibraryCreationSection()
        }
        else if(uiState.isToUpdateBook) {
            LibraryUpdateSection()
        }
        else if(uiState.isToFilterBooks) {
            LibraryFilterSection()
        }
        else {
            LibraryHeaderActions(onBack)
            LibraryListSection()
        }
    }

    @Composable
    private fun LibraryHeaderActions(onBack: () -> Unit) {
        val viewModel: LibraryViewModel = koinViewModel()

        Column {
            Column(Modifier.fillMaxWidth(),
                Arrangement.Top,
                Alignment.CenterHorizontally) {

                Row {
                    OutlinedButton(modifier = Modifier
                        .width(200.dp)
                        .height(70.dp),
                        colors = CommonViewComp.getActionsButtonColour(),
                        onClick = {
                            viewModel.showAddBook(true)
                        }) {
                        Text("Add Book")
                    }

                    OutlinedButton(
                        modifier = Modifier.width(200.dp).height(70.dp),
                        colors = CommonViewComp.getSecondaryButtonColour(),
                        onClick = {
                            onBack()
                        }) {
                        Text("Back")
                    }
                }
            }
            Row {
                Column(Modifier.fillMaxWidth(),
                    Arrangement.Top,
                    Alignment.CenterHorizontally) {

                    OutlinedButton(
                        modifier = Modifier.width(200.dp).height(70.dp),
                        colors = CommonViewComp.getActionsButtonColour(),
                        onClick = {
                            viewModel.showFilterBooks(true);
                        }) {
                        Text("Filter Books")
                    }
                }
            }
        }
    }
}