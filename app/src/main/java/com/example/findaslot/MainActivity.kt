package com.example.findaslot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.findaslot.logic.network.RetrofitViewModel
import com.example.findaslot.screens.main.DrawerNavigation
import com.example.findaslot.screens.main.LocationPermissionScreen
import com.example.findaslot.ui.theme.FindASlotTheme
import com.example.findaslot.utilities.checkForPermission

class MainActivity : ComponentActivity() {
    private val viewModel: RetrofitViewModel by viewModels()

    @RequiresApi(64)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindASlotTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    var hasLocationPermission by remember {
                        mutableStateOf(checkForPermission(this))
                    }

                    if (hasLocationPermission) {
                        DrawerNavigation(this, viewModel)
                    } else {
                        LocationPermissionScreen {
                            hasLocationPermission = true
                        }
                    }
                }
            }
        }
    }
}

/*
* This project uses components from "Jetpack Compose GoogleMaps" by YoursSohail,
* available under the MIT License at https://github.com/YoursSohail/Jetpack-Compose-GoogleMaps.
* */