package com.alfred.myplanningbook.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
object TextFieldComponent {
    @Composable
    fun show(initialText: String, label: String, onValueChange: (String) -> Unit) {
        var textValue by remember { mutableStateOf(TextFieldValue(initialText, TextRange(3, 100))) }

        OutlinedTextField(
            value = textValue,
            modifier = Modifier.height(90.dp)
                               .fillMaxSize(1f)
                               .padding(10.dp),
            onValueChange = {
                if (it.text.length <= 50) {
                    textValue = it
                    onValueChange(textValue.text)
                }
            },
            label = { Text(text=label)},
            placeholder = { Text("$label (0-50)") },
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                IconButton(
                    onClick = {
                        textValue = TextFieldValue("")
                        onValueChange("")
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                }
            },)
    }
}