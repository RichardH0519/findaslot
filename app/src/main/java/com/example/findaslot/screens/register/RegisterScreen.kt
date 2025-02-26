package com.example.findaslot.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.findaslot.R
import com.example.findaslot.screens.main.Routes
import com.example.findaslot.screens.resuable.InfoDialog
import com.example.findaslot.screens.reuseable.PasswordField
import com.example.findaslot.ui.theme.IndianRed
import com.example.findaslot.ui.theme.OrangeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    var openAlertDialogForEmptyFields = remember {
        mutableStateOf(false)
    }

    var openAlertDialogForExistedAccount = remember {
        mutableStateOf(false)
    }

    if (openAlertDialogForEmptyFields.value) {
        InfoDialog(
            onDismiss = { openAlertDialogForEmptyFields.value = false }, "ALERT",
            "Username and password cannot be empty"
        )
    }

    if (openAlertDialogForExistedAccount.value) {
        InfoDialog(
            onDismiss = { openAlertDialogForExistedAccount.value = false }, "ALERT",
            "Account already exist"
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = {/*Null*/ }, navigationIcon = {})
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        painter = painterResource(id = R.drawable.register_pic),
                        contentDescription = "Register picture"
                    )
                }

                Box(modifier = Modifier.padding(vertical = 50.dp, horizontal = 50.dp)) {
                    Column {
                        Text(
                            text = "User Registration",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = IndianRed, //Check color later
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = username.value,
                            onValueChange = { username.value = it },
                            label = { Text(text = "Username") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        PasswordField(password = password)

                        RegisterButton(username.value, password.value) { usr, pwd ->
                            // calls registerUser to handle register logic
                            FirebaseDatabaseManager.registerUser(
                                username = usr,
                                password = pwd,
                                onSuccess = {
                                    // @ richard - put code to move to login screen here
                                    com.example.findaslot.screens.login.FirebaseDatabaseManager.loginUser(
                                        username = username.value,
                                        password = password.value,
                                        onSuccess = {
                                            navHostController.navigate(Routes.Main.value) {
                                                popUpTo(Routes.Main.value) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onFailure = {})
                                },
                                onFailure = { error ->
                                    // @ richard - either show register failure pop-up message or no action
                                    if (error.equals("Username already exists")) {
                                        openAlertDialogForExistedAccount.value = true
                                    } else {
                                        openAlertDialogForEmptyFields.value = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
// function for register button
fun RegisterButton(username: String, password: String, onClick: (String, String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        ElevatedButton(
            onClick = { onClick(username, password) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = OrangeRed),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(15.dp)
        ) {
            Text(text = "Register", fontSize = 30.sp)
        }
    }
}



