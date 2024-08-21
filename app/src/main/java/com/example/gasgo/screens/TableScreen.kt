package com.example.gasgo.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gasgo.R
import com.example.gasgo.data.Resource
import com.example.gasgo.model.GasStation
import com.example.gasgo.router.Routes
import com.example.gasgo.screens.components.CustomTable
import com.example.gasgo.screens.components.mapFooter
import com.example.gasgo.viewmodels.GasStationViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TableScreen(
    gasStations: List<GasStation>?,
    navController: NavController,
    gasStationViewModel: GasStationViewModel
){
    val newGasStations = remember {
        mutableListOf<GasStation>()
    }
    if (gasStations.isNullOrEmpty()){
        val gasStationsResource = gasStationViewModel.gasStations.collectAsState()
        gasStationsResource.value.let {
            when(it){
                is Resource.Success -> {
                    Log.d("Podaci", it.toString())
                    newGasStations.clear()
                    newGasStations.addAll(it.result)
                }
                is Resource.loading -> {

                }
                is Resource.Failure -> {
                    Log.e("Podaci", it.toString())
                }
                null -> {}
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pregled benzinskih stanica",
                    modifier = Modifier.fillMaxWidth(),
                    style= TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            if(gasStations.isNullOrEmpty()){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(300.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.not_found),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Nije pronađena nijedna benzinska stanica")
                    }
                }
            }else {
                CustomTable(
                    gasStations = gasStations,
                    navController = navController
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            mapFooter(
                openAddNewGasStation = {},
                active = 1,
                onHomeClick = {
                    val gasStationsJson = Gson().toJson(gasStations)
                    val encodedGasStationsJson = URLEncoder.encode(gasStationsJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.indexScreenWithParams + "/$encodedGasStationsJson")
                },
                onTableClick = {
                },
                onRankingClick = {
                    navController.navigate(Routes.rankingScreen)
                },
                onSettingsClick = {
                    navController.navigate(Routes.settingsScreen)
                }
            )
        }
    }
}