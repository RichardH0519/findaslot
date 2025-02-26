package com.example.findaslot.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextButton
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
import com.example.findaslot.ui.theme.CornFlowerBlue
import com.example.findaslot.ui.theme.MediumBlue
import com.example.findaslot.ui.theme.NavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(navHostController: NavHostController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(title = {/*Null*/ })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        painter = painterResource(id = R.drawable.login_pic),
                        contentDescription = "Login picture"
                    )
                }

                Box(modifier = Modifier.padding(vertical = 50.dp, horizontal = 50.dp)) {
                    Column {
                        Text(
                            text = "Login Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = MediumBlue, //Check color later
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = username.value,
                            onValueChange = { username.value = it },
                            label = { Text(text = "Username") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        PasswordField(password = password)

                        TextButton(
                            modifier = Modifier.padding(top = 15.dp, bottom = 20.dp),
                            content = {
                                Text(
                                    text = "Don't have an account? Sign up.",
                                    color = CornFlowerBlue
                                )
                            }, onClick = {
                                navHostController.navigate(Routes.Register.value)
                            }
                        )

                        LoginButton(username.value, password.value, navHostController)
                    }
                }
            }
        }
    }
}


@Composable
// function for login button
fun LoginButton(username: String, password: String, navHostController: NavHostController) {
    var openAlertDialog = remember {
        mutableStateOf(false)
    }

    if (openAlertDialog.value) {
        InfoDialog(
            onDismiss = { openAlertDialog.value = false }, "ALERT",
            "Incorrect username or password"
        )
    }

    ElevatedButton(
        onClick = {
            // calls loginUser to handle login logic
            FirebaseDatabaseManager.loginUser(
                username = username,
                password = password,
                // action when matching account found
                onSuccess = {
                    // @ richard - put code to move to main screen here
                    navHostController.navigate(Routes.Main.value) {
                        popUpTo(Routes.Main.value) {
                            inclusive = true
                        }
                    }
                },
                // action when matching account not found
                onFailure = {
                    // @ richard - either show login failure pop-up message or no action
                    openAlertDialog.value = true
                }
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = NavyBlue),
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(text = "Login", fontSize = 30.sp)
    }
}



