package com.example.findaslot.screens.resuable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingInfoDialog(onDismiss: () -> Unit, parkingName: String, parkingAddress: String) {
    AlertDialog(
        title = {
            Text(text = parkingName)
        },
        text = {
            Text(text = parkingAddress)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {},
    )
}