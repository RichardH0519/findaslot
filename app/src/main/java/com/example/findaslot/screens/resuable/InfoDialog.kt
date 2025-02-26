package com.example.findaslot.screens.resuable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDialog(onDismiss: () -> Unit, title: String, content: String) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = content)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {},
    )
}