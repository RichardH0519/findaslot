package com.example.findaslot.screens.personaldetails

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.findaslot.screens.login.FirebaseDatabaseManager
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(0)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }
    val carTypes = listOf("Family cars", "SUVs", "Trucks", "Motorcycles")
    var selectedCarType by remember { mutableStateOf(carTypes.first()) }
    var submittedName by remember { mutableStateOf<String?>(null) }
    var submittedEmail by remember { mutableStateOf<String?>(null) }
    var submittedCarType by remember { mutableStateOf<String?>(null) }
    var submittedDate by remember { mutableStateOf<Long?>(null) }
    val currentUser = FirebaseDatabaseManager.currentUserAccount

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("User Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (currentUser != null) {
                Text(
                    "Logged in as: ${currentUser.username}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !isValidEmail(email)
                },
                label = { Text("Email") },
                placeholder = { Text("Example: xxx@gmail.com") },
                isError = emailError
            )
            if (emailError) {
                Text("Wrong email address", color = MaterialTheme.colorScheme.error)
            }

            DisplayDatePicker(selectedDate) { newDate -> selectedDate = newDate }
            DropdownMenuBox(carTypes, selectedCarType) { newCarType ->
                selectedCarType = newCarType
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (isValidEmail(email)) {
                    submittedName = name
                    submittedEmail = email
                    submittedCarType = selectedCarType
                    submittedDate = selectedDate
                    println("Calling submitToFirebase with $name, $email, $selectedDate, $selectedCarType")
                    submitToFirebase(name, email, selectedDate, selectedCarType)
                } else {
                    emailError = true
                }
            }) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (submittedName != null) {
                Text("Submitted Name: $submittedName", style = MaterialTheme.typography.bodyLarge)
                Text("Submitted Email: $submittedEmail", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Submitted Car Type: $submittedCarType",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Submitted Date of Birth: ${
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(Date(submittedDate!!))
                    }", style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayDatePicker(selectedDate: Long, onDateSelected: (Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        onDateSelected(datePickerState.selectedDateMillis ?: selectedDate)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Button(onClick = { showDatePicker = true }) {
            Text("Enter Date of Birth")
        }
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        Text("Date of Birth: ${formatter.format(Date(selectedDate))}")
    }
}

@Composable
fun DropdownMenuBox(
    carTypes: List<String>,
    selectedCarType: String,
    onCarTypeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedCarType,
            onValueChange = { },
            label = { Text("Car Type") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            carTypes.forEach { carType ->
                DropdownMenuItem(
                    onClick = {
                        onCarTypeSelected(carType)
                        expanded = false
                    },
                    text = { Text(carType) }
                )
            }
        }
    }
}


fun submitToFirebase(name: String, email: String, dateOfBirth: Long, carType: String) {
    println("Attempting to submit data...")
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = database.push().key ?: return  // Generate a unique key for the user

    val userMap = hashMapOf(
        "username" to (FirebaseDatabaseManager.currentUserAccount?.username
            ?: "Unknown"),  // Include username
        "name" to name,
        "email" to email,
        "dateOfBirth" to dateOfBirth,
        "carType" to carType
    )

    println("Data prepared for submission: $userMap")

    userId.let {
        database.child(it).setValue(userMap)
            .addOnSuccessListener {
                println("Data submitted successfully!")
            }
            .addOnFailureListener {
                println("Error submitting data: ${it.message}")
            }
    }
}

