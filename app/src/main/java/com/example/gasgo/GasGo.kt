package com.example.gasgo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.gasgo.location.LocationService
import com.example.gasgo.router.Router
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.GasStationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GasGo(
    viewModel: AuthViewModel,
    gasStationViewModel: GasStationViewModel
){

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val isTrackingServiceEnabled = sharedPreferences.getBoolean("tracking_location", true)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    } else {
        if(isTrackingServiceEnabled) {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_FIND_NEARBY
                context.startForegroundService(this)
            }
        }else{
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                context.startForegroundService(this)
            }
        }

    }

    Surface (modifier = Modifier.fillMaxSize()){
        Router(viewModel, gasStationViewModel)
    }


}