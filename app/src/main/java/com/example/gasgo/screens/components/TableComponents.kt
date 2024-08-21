package com.example.gasgo.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gasgo.model.GasStation
import com.example.gasgo.router.Routes
import com.example.gasgo.ui.theme.greyTextColor
import com.example.gasgo.ui.theme.lightGreyColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CustomTable(
    gasStations: List<GasStation>?,
    navController: NavController
) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ) {
        Column {
            CustomTableHeader()

            Box(
                modifier = Modifier
                    .width(500.dp)
                    .height(2.dp)
                    .background(greyTextColor)
            )

            gasStations?.forEachIndexed { index, gasStation ->
                CustomTableRow(type = index%2, gasStation, openGasStationScreen = {
                    val gasStationJson = Gson().toJson(gasStation)
                    val encodedGasStationJson = URLEncoder.encode(gasStationJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.gasStationScreen + "/$encodedGasStationJson")
                },
                    openGasStationLocation = {
                        val isCameraSet = true
                        val latitude = gasStation.location.latitude
                        val longitude = gasStation.location.longitude

                        val gasStationsJson = Gson().toJson(gasStations)
                        val encodedGasStationsJson = URLEncoder.encode(gasStationsJson, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.indexScreenWithParams + "/$isCameraSet/$latitude/$longitude/$encodedGasStationsJson")
                    }

                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun CustomTableHeader() {
    val boxModifier = Modifier.padding(12.dp)
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.width(60.dp))

        Box(modifier = boxModifier.width(200.dp)) {
            Text(
                text = "Opis",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }

        Box(modifier = boxModifier.width(120.dp)) {
            Text(
                text = "Gužva",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }

        Box(modifier = boxModifier.width(50.dp)) {}
    }
}

@Composable
fun CustomTableRow(
    type: Int,
    gasStation: GasStation,
    openGasStationScreen: () -> Unit,
    openGasStationLocation: () -> Unit
) {
    val boxModifier = Modifier.padding(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (type == 0) Color.Transparent else lightGreyColor
            )
            .clickable { openGasStationScreen() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(60.dp)){
            AsyncImage(
                model = gasStation.mainImage,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .height(60.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }


        Box(modifier = boxModifier.width(200.dp)) {
            Text(
                text = if(gasStation.description.length > 20) gasStation.description.substring(0, 20).replace('+', ' ') + "..." else gasStation.description.replace('+', ' '),
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }

        Box(modifier = boxModifier.width(120.dp)) {
            CustomCrowdIndicator(crowd = gasStation.crowd)
        }
        Box(modifier = boxModifier.width(50.dp)) {
            IconButton(
                onClick = openGasStationLocation,
            ){
                Icon(
                    imageVector = Icons.Outlined.ShareLocation,
                    contentDescription = ""
                )
            }
        }
    }
}