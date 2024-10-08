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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
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
            BookFormComponent_chips()
            BookFormComponent_subtitle()
            BookFormComponent_author()
            BookFormComponent_saga()
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
private fun BookFormComponent_chips() {
    val viewModel: LibraryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row {

    } //Row
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
            OutlinedTextField(value = uiState.bookTitle,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookTitle(it) },
                label = { Text(text="Title")},
                placeholder = { Text("Title (2-40)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.bookTitleError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
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
            OutlinedTextField(value = uiState.bookSubtitle,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookSubtitle(it) },
                label = { Text(text="Subtitle")},
                placeholder = { Text("Subtitle (2-40)") },
                singleLine = true,
                maxLines = 1)

            if(uiState.bookSubtitleError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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
            OutlinedTextField(value = uiState.bookNotes,
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
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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
            OutlinedTextField(value = uiState.bookAuthor,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookAuthor(it) },
                label = { Text(text="Author")},
                placeholder = { Text("Author (5-40)") },
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
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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
            OutlinedTextField(value = uiState.bookSaga,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookSaga(it) },
                label = { Text(text="Saga")},
                placeholder = { Text("Saga (5-40)") },
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
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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
            OutlinedTextField(value = uiState.bookPublisher,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookPublisher(it) },
                label = { Text(text="Publisher")},
                placeholder = { Text("Publisher (5-40)") },
                singleLine = true,
                maxLines = 1,
            )

            if(uiState.bookPublisherError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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
            OutlinedTextField(value = uiState.bookCategory,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookCategory(it) },
                label = { Text(text="Category")},
                placeholder = { Text("Category (5-40)") },
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
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_language() {
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
            OutlinedTextField(value = uiState.bookLanguage,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookLanguage(it) },
                label = { Text(text="Language")},
                placeholder = { Text("Language (5-40)") },
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

            if(uiState.bookLanguageError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
            }
        }
    } //Row
}

@Composable
private fun BookFormComponent_format() {
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
            OutlinedTextField(value = uiState.bookFormat,
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxSize(1f)
                    .padding(10.dp),
                onValueChange = { viewModel.updateBookFormat(it) },
                label = { Text(text="Format")},
                placeholder = { Text("Format (5-40)") },
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

            if(uiState.bookFormatError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
                        fontSize = 15.sp, color = Color.Red
                    )
                )
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
            OutlinedTextField(value = uiState.bookReadYear,
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
                    uiState.bookFieldErrorTxt, color = Color.Red, style = TextStyle(
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