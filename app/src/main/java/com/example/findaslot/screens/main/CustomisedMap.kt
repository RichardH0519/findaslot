package com.example.findaslot.screens.main

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.findaslot.R
import com.example.findaslot.models.parking.Parking
import com.example.findaslot.ui.theme.CornFlowerBlue
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun CustomisedMap(context: Context, latLng: LatLng, listOfParking: List<Parking>) {
    var isMapLoaded by remember { mutableStateOf(false) }

    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    var uiSettings = MapUiSettings(tiltGesturesEnabled = false, mapToolbarEnabled = false)

    var mapProperties = MapProperties(mapType = MapType.TERRAIN, isMyLocationEnabled = true)

    GoogleMap(
        modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState,
        properties = mapProperties, uiSettings = uiSettings, onMapLoaded = {
            isMapLoaded = true
        }
    ) {
        listOfParking.toList().forEach {
            var title = it.name
            var address = it.vicinity

            MarkerInfoWindow(
                state = MarkerState(
                    position = LatLng(
                        it.geometry.location.lat.toDouble(),
                        it.geometry.location.lng.toDouble()
                    )
                ), icon = BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            RoundedCornerShape(10)
                        )
                        .clip(RoundedCornerShape(10))
                        .background(CornFlowerBlue)
                        .padding(20.dp)
                ) {
                    Text(title, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(address, fontWeight = FontWeight.Medium, color = Color.White)
                }
            }
        }
    }
}

/*
* Özcan, R. (Year). How to Use Google Maps in Jetpack Compose: Step by Step Android Guide. Medium.
* Available at:
* https://medium.com/@ridvanozcan48/how-to-use-google-maps-in-jetpack-compose-step-by-step-android-guide-55aedac89e43
* */