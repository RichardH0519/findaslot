package com.example.findaslot.screens.main

import android.content.Context
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findaslot.logic.network.RetrofitViewModel
import com.example.findaslot.screens.login.FirebaseDatabaseManager
import com.example.findaslot.screens.login.LogInScreen
import com.example.findaslot.screens.personaldetails.PersonalDetailScreen
import com.example.findaslot.screens.register.RegisterScreen
import kotlinx.coroutines.launch

@RequiresApi(64)
@Composable
fun DrawerNavigation(context: Context, viewModel: RetrofitViewModel) {
    var navController = rememberNavController()
    var coroutineScope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            DrawerContent(
                menus = if (FirebaseDatabaseManager.loggedInStatus) {
                    menusLoggedIn
                } else {
                    menusBeforeLoggedIn
                }
            ) { route ->
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(route)
            }
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = Routes.Main.value,
            modifier = Modifier.padding(8.dp)
        ) {
            composable(Routes.Main.value) {
                MainScreen(drawerState = drawerState, context, viewModel)
            }
            composable(Routes.SignIn.value) {
                LogInScreen(navHostController = navController)
            }
            composable(Routes.Profile.value) {
                PersonalDetailScreen()
            }
            composable(Routes.Register.value) {
                RegisterScreen(navHostController = navController)
            }
        }
    }
}