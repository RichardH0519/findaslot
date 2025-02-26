package com.example.findaslot.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

fun checkForPermission(context: Context): Boolean {
    return !(ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED)
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
    var currentLocation: LatLng
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            currentLocation = LatLng(latitude, longitude)
            onLocationFetched(currentLocation)
        }
    }.addOnFailureListener { exception: Exception ->
        // Handle failure to get location
        Log.d("MAP-EXCEPTION", exception.message.toString())
    }
}

/*
* This project uses components from "Jetpack Compose GoogleMaps" by YoursSohail,
* available under the MIT License at https://github.com/YoursSohail/Jetpack-Compose-GoogleMaps.
* */