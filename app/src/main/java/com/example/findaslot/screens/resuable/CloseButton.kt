package com.example.findaslot.screens.reuseable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun CloseButton() {
    IconButton(onClick = { }) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
    }
}