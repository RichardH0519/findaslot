package com.example.findaslot.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerMenu(var icon: ImageVector, var title: String, var route: String)

var menusLoggedIn =

    arrayOf(
        DrawerMenu(Icons.Filled.Person, "My Profile", Routes.Profile.value),
        DrawerMenu(Icons.Filled.ArrowBack, "Exit", "")
    )

var menusBeforeLoggedIn =
    arrayOf(
        DrawerMenu(Icons.Filled.ExitToApp, "Sign In", Routes.SignIn.value)
    )
