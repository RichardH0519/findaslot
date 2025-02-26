package com.example.findaslot.screens.main

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.findaslot.R
import com.example.findaslot.logic.network.RetrofitViewModel
import com.example.findaslot.models.parking.Parking
import com.example.findaslot.screens.resuable.ParkingInfoDialog
import com.example.findaslot.ui.theme.MediumBlue
import com.example.findaslot.ui.theme.NavyBlue
import com.example.findaslot.utilities.getCurrentLocation
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(drawerState: DrawerState, context: Context, viewModel: RetrofitViewModel) {
    var location by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var showMap by remember {
        mutableStateOf(false)
    }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    var currentLocation = "${location.latitude},${location.longitude}"

    var parkingsReturned by viewModel.retrofitResponse

    var listOfParking: List<Parking> = ArrayList()

    viewModel.getResponse(currentLocation)

    var list = parkingsReturned.results
    if (list.isNotEmpty()) {
        listOfParking = list
    }

    Scaffold(topBar = {
        var coroutineScope = rememberCoroutineScope()
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(titleContentColor = NavyBlue),
            title = { Text(text = "Find A Slot", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(Icons.Filled.Menu, contentDescription = "", tint = MediumBlue)
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.Top) {
                Box(
                    modifier = Modifier
                        .height(550.dp)
                        .padding(bottom = 20.dp)
                ) {
                    if (showMap) {
                        CustomisedMap(context, location, listOfParking)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.recent_parking_icon),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        text = "Nearby Parking",
                        modifier = Modifier.padding(start = 15.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(110.dp))
                    /*Icon(
                        painter = painterResource(id = R.drawable.star_filled),
                        contentDescription = "",
                        tint = GoldenRod,
                        modifier = Modifier.size(30.dp)
                    )*/
                }
                LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
                    items(listOfParking) {
                        ParkingLocationCard(it)
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingLocationCard(parking: Parking) {
    var openAlertDialog = remember {
        mutableStateOf(false)
    }

    Card(
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            openAlertDialog.value = true
        },
    ) {
        if (openAlertDialog.value) {
            ParkingInfoDialog(
                onDismiss = { openAlertDialog.value = false }, parking.name,
                parking.vicinity
            )
        }

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.parking_info),
                contentDescription = "",
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 5.dp)
            )
            Text(text = parking.name, fontWeight = FontWeight.Bold, color = MediumBlue)
        }
    }
}

/*
* This project uses components from "Jetpack Compose GoogleMaps" by YoursSohail,
* available under the MIT License at https://github.com/YoursSohail/Jetpack-Compose-GoogleMaps.
* */