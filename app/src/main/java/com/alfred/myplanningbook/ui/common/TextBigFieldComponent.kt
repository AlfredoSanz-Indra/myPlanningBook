package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 * @author Alfredo Sanz
 * @time 2025
 */
object TextBigFieldComponent {
    @Composable
    fun show(initialText: String, maxLines: Int, label: String, onValueChange: (String) -> Unit) {
        var textValue by remember { mutableStateOf(TextFieldValue(initialText, TextRange(3, 100))) }

        OutlinedTextField(
            value = textValue,
            modifier = Modifier.fillMaxSize(1f)
                               .padding(10.dp),
            onValueChange = {
                if (it.text.length <= 70) {
                    textValue = it
                    onValueChange(textValue.text)
                }
            },
            label = { Text(text=label)},
            placeholder = { Text("$label (0-70)") },
            singleLine = false,
            maxLines = maxLines,
            minLines = maxLines - 1,
            trailingIcon = {
                IconButton(
                    onClick = {
                        textValue = TextFieldValue("")
                    }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                }
            }
        )
    }
}