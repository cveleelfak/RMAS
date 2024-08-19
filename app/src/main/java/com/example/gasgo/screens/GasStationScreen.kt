package com.example.gasgo.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gasgo.data.Resource
import com.example.gasgo.model.GasStation
import com.example.gasgo.model.Rate
import com.example.gasgo.router.Routes
import com.example.gasgo.screens.components.CustomBackButton
import com.example.gasgo.screens.components.CustomCrowdIndicator
import com.example.gasgo.screens.components.CustomGasStationGallery
import com.example.gasgo.screens.components.CustomGasStationLocation
import com.example.gasgo.screens.components.CustomGasStationRate
import com.example.gasgo.screens.components.CustomRateButton
import com.example.gasgo.screens.components.GasStationMainImage
import com.example.gasgo.screens.components.greyTextBigger
import com.example.gasgo.screens.components.headingText
import com.example.gasgo.screens.dialogs.RateGasStationDialog
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.GasStationViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GasStationScreen(
    navController: NavController,
    gasStationViewModel: GasStationViewModel,
    gasStation: GasStation,
    viewModel: AuthViewModel,
    gasStations : MutableList<GasStation>?
){
    val ratesResources = gasStationViewModel.rates.collectAsState()
    val newRateResource = gasStationViewModel.newRate.collectAsState()

    val rates = remember {
        mutableListOf<Rate>()
    }
    val averageRate = remember {
        mutableStateOf(0.0)
    }
    val showRateDialog = remember {
        mutableStateOf(false)
    }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val myPrice = remember {
        mutableStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GasStationMainImage(gasStation.mainImage)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            item{ CustomBackButton {
                if(gasStations == null) {
                    navController.popBackStack()
                }else{
                    val isCameraSet = true
                    val latitude = gasStation.location.latitude
                    val longitude = gasStation.location.longitude

                    val gasStationsJson = Gson().toJson(gasStations)
                    val encodedGasStationsJson = URLEncoder.encode(gasStationsJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.indexScreenWithParams + "/$isCameraSet/$latitude/$longitude/$encodedGasStationsJson")
                }
            }
            }
            item{ Spacer(modifier = Modifier.height(220.dp)) }
            item{ CustomCrowdIndicator(crowd = gasStation.crowd) }
            item{ Spacer(modifier = Modifier.height(20.dp)) }
            item{ headingText(textValue = "Benzinska stanica u blizini") }
            item{ Spacer(modifier = Modifier.height(10.dp)) }
            item{ CustomGasStationLocation(location = LatLng(gasStation.location.latitude, gasStation.location.longitude)) }
            item{ Spacer(modifier = Modifier.height(10.dp)) }
            item{ CustomGasStationRate(average = averageRate.value) }
            item{ Spacer(modifier = Modifier.height(10.dp)) }
            item{ greyTextBigger(textValue = gasStation.description.replace('+', ' ')) }
            item{ Spacer(modifier = Modifier.height(20.dp)) }
            item{ Text(text = "Galerija Benzinske Stanice", style= TextStyle(fontSize = 20.sp)) };

            item{ Spacer(modifier = Modifier.height(10.dp)) }
            item { CustomGasStationGallery(images = gasStation.galleryImages) }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            CustomRateButton(
                enabled = if(gasStation.userId == viewModel.currentUser?.uid) false else true,
                onClick = {
                    val rateExist = rates.firstOrNull{
                        it.gasStationId == gasStation.id && it.userId == viewModel.currentUser!!.uid
                    }
                    if(rateExist != null)
                        myPrice.value = rateExist.rate
                    showRateDialog.value = true
                })
        }


        if(showRateDialog.value){
            RateGasStationDialog(
                showRateDialog = showRateDialog,
                rate = myPrice,
                rateGasStation = {

                    val rateExist = rates.firstOrNull{
                        it.gasStationId == gasStation.id && it.userId == viewModel.currentUser!!.uid
                    }
                    if(rateExist != null){
                        isLoading.value = true
                        gasStationViewModel.updateRate(
                            rid = rateExist.id,
                            rate = myPrice.value
                        )
                    }else {
                        isLoading.value = true
                        gasStationViewModel.addRate(
                            gid = gasStation.id,
                            rate = myPrice.value,
                            gasStation = gasStation
                        )
                    }
                },
                isLoading = isLoading
            )
        }
    }

    ratesResources.value.let {
        when(it){
            is Resource.Success -> {
                rates.addAll(it.result)
                var sum = 0.0
                for (rate in it.result){
                    sum += rate.rate.toDouble()
                }
                if(sum != 0.0) {
                    val rawPositive = sum / it.result.count()
                    val rounded = rawPositive.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                    averageRate.value = rounded
                }  else {}
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
        }
    }
    newRateResource.value.let {
        when(it){
            is Resource.Success -> {
                isLoading.value = false

                val rateExist = rates.firstOrNull{rate ->
                    rate.id == it.result
                }
                if(rateExist != null){
                    rateExist.rate = myPrice.value
                }
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, "Došlo je do greške prilikom ocenjivanja benzinske stanice", Toast.LENGTH_LONG).show()
                isLoading.value = false
            }
            null -> {
                isLoading.value = false
            }
        }
    }
}