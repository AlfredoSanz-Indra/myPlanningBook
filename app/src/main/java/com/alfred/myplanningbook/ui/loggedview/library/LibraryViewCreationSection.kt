package com.alfred.myplanningbook.ui.loggedview.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.library.viewmodel.LibraryViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.alfred.myplanningbook.core.log.Klog

/**
 * @author Alfredo Sanz
 * @time 2024
 */
@Composable
fun LibraryCreationSection() {
    LibraryCreationActions()
    Spacer(modifier = Modifier.height(10.dp))
    LibraryDataFieldsComponents()
}

@Composable
fun LibraryUpdateSection() {
    LibraryUpdateActions()
    Spacer(modifier = Modifier.height(10.dp))
    LibraryDataFieldsComponents()
}

@Composable
private fun LibraryCreationActions() {
    val viewModel: LibraryViewModel = koinViewModel()

    Column {
        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    viewModel.createBook();
                }) {
                Text("Save")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    viewModel.showAddBook(false);
                }) {
                Text("Cancel")
            }
        }
    }
}

@Composable
private fun LibraryUpdateActions() {
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
                        viewModel.updateBook();
                    }) {
                    Text("Save")
                }

                OutlinedButton(
                    modifier = Modifier.width(200.dp).height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        viewModel.showUpdateBook(false);
                    }) {
                    Text("Cancel")
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
                        viewModel.cloneBook();
                    }) {
                    Text("Clone with changes")
                }
            }
        }
    }
}

@Composable
private fun LibraryDataFieldsComponents() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.isBookActionWorking) {
        Spacer(modifier = Modifier.height(30.dp))
        Working()
    }

    Box(
        modifier = Modifier
            .padding(15.dp)
            .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
               ) {
            BookFormComponent_title()
            BookFormComponent_switches()
            BookFormComponent_subtitle()
            BookFormComponent_author()
            BookFormComponent_saga()
            BookFormComponent_sagaIndex()
            BookFormComponent_notes()
            BookFormComponent_publisher()
            BookFormComponent_category()
            BookFormComponent_language()
            BookFormComponent_format()
            BookFormComponent_readYear()
        }
    }
}

@Composable
private fun Working() {
    CircularProgressIndicator()

    Text(
        "Working ....",
        color = CommonViewComp.c_card_buttonOneContent,
        style = TextStyle(
            fontSize = 20.sp,
            background = CommonViewComp.c_snow
        )
    )
}

@Composable
private fun BookFormComponent_title() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookTitle,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookTitle(it) },
                label = { Text(text="Title")},
                placeholder = { Text("Title (2-50)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.bookTitleError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookTitleErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_switches() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            Arrangement.Top,
            Alignment.Start
        ) {
            Row(modifier = Modifier, Arrangement.SpaceBetween) {
                Column(modifier = Modifier
                        .padding(vertical = 10.dp)
                        .weight(0.5F)) {
                    Text("I have read the book")
                }

                Column(modifier = Modifier
                        .padding(vertical = 10.dp)) {
                    Text("I have the book")
                }
            }//Row texts

            Row(modifier = Modifier, Arrangement.SpaceBetween) {
                Column(modifier = Modifier
                        .padding(vertical = 0.dp)
                        .weight(0.5F)) {
                    Switch(modifier = Modifier.semantics { contentDescription = "Read" }.padding(0.dp),
                        checked = uiState.bookRead == "y",
                        onCheckedChange = { viewModel.updateBookRead(it) },
                        thumbContent = {
                            if(uiState.bookRead == "y") {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        }
                    )
                }

                Column(modifier = Modifier
                        .padding(vertical = 0.dp)) {
                    Switch(modifier = Modifier.semantics { contentDescription = "Have" }.padding(0.dp),
                        checked = uiState.bookHave == "y",
                        onCheckedChange = { viewModel.updateBookHave(it) },
                        thumbContent = {
                            if(uiState.bookHave == "y") {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        }
                    )
                }
            }//Row Switches
        }
    } //Row
}

@Composable
private fun BookFormComponent_subtitle() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookSubtitle,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookSubtitle(it) },
                label = { Text(text="Subtitle")},
                placeholder = { Text("Subtitle (2-50)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.bookSubtitleError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookSubtitleErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}


@Composable
private fun BookFormComponent_notes() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookNotes,
                onValueChange = { viewModel.updateBookNotes(it) },
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                    //.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
                label = { Text(text="Notes")},
                placeholder = { Text("Notes about the book (5-150)") },
                singleLine = false,
                maxLines = 3
            )
            if(uiState.bookNotesError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookNotesErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_author() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookAuthor,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookAuthor(it) },
                label = { Text(text="Author")},
                placeholder = { Text("Author (2-50)") },
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            //TODO
                            //viewModel.openTimeStartDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.FilterList , contentDescription = null)
                    }
                },
                )

            if(uiState.bookAuthorError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookAuthorErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_saga() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookSaga,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookSaga(it) },
                label = { Text(text="Saga")},
                placeholder = { Text("Saga (2-50)") },
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            //TODO
                            //viewModel.openTimeStartDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.FilterList , contentDescription = null)
                    }
                },
            )

            if(uiState.bookSagaError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookSagaErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}


@Composable
private fun BookFormComponent_sagaIndex() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookSagaIndex,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookSagaIndex(it) },
                label = { Text(text="Saga Index")},
                placeholder = { Text("Saga Index (number)") },
                singleLine = true,
                maxLines = 1,
            )

            if(uiState.bookSagaIndexError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookSagaIndexErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_publisher() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookPublisher,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookPublisher(it) },
                label = { Text(text="Publisher")},
                placeholder = { Text("Publisher (2-50)") },
                singleLine = true,
                maxLines = 1,
            )

            if(uiState.bookPublisherError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookPublisherErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_category() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookCategory,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookCategory(it) },
                label = { Text(text="Category")},
                placeholder = { Text("Category (2-50)") },
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            //TODO
                            //viewModel.openTimeStartDi()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.FilterList , contentDescription = null)
                    }
                },
            )

            if(uiState.bookCategoryError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookCategoryErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookFormComponent_language() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isExpanded by remember { mutableStateOf(false) }

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { newValue ->
                    isExpanded = newValue
                }
            ) {
                OutlinedTextField(
                    value = uiState.bookLanguage,
                    modifier = Modifier.height(90.dp).fillMaxSize(1f).padding(10.dp),
                    onValueChange = { },
                    label = { Text(text = "Language") },
                    placeholder = { Text("Language") },
                    readOnly = true,
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded,
                            modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable),
                        )
                    })

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    uiState.languagesMaster?.forEach { it ->
                        DropdownMenuItem(
                            text = { Text(it.value, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                viewModel.updateBookLanguage(it.value)
                                viewModel.updateBookLanguageCode(it.name)
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    } //Row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookFormComponent_format() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isExpanded by remember { mutableStateOf(false) }

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { newValue ->
                    isExpanded = newValue
                }
            ) {
                OutlinedTextField(
                    value = uiState.bookFormat,
                    modifier = Modifier.height(90.dp).fillMaxSize(1f).padding(10.dp),
                    onValueChange = { },
                    label = { Text(text = "Format") },
                    placeholder = { Text("Format") },
                    readOnly = true,
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded,
                            modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable),
                        )
                    })

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    uiState.formatsMaster?.forEach { it ->
                        DropdownMenuItem(
                            text = { Text(it.value, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                viewModel.updateBookFormat(it.value)
                                viewModel.updateBookFormatCode(it.name)
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_readYear() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.bookReadYear,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookReadYear(it) },
                label = { Text(text="Read year")},
                placeholder = { Text("Read Year (1990)") },
                singleLine = true,
                maxLines = 1,
            )

            if(uiState.bookReadYearError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookReadYearErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}
/**
 *                 val title: String,
 *                 val read: Int,
 *                 val haveit: Int,
 *                 val subtitle: String?,
 *                 val description: String?,
 *                 val notes: String?,
 *                 val authorName: String?,
 *                 val authorID: Int?,
 *                 val sagaName: String?,
 *                 val sagaID: Int?,
 *                 val editorial: String?,
 *                 val categoryName: String?,
 *                 val categoryID: Int?,
 *                 val language: String?,
 *                 val format: String?,
 */