package com.example.gasgo.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gasgo.R
import com.example.gasgo.data.Resource
import com.example.gasgo.model.CustomUser
import com.example.gasgo.model.GasStation
import com.example.gasgo.router.Routes
import com.example.gasgo.screens.components.CustomBackButton
import com.example.gasgo.screens.components.LogoutButton
import com.example.gasgo.screens.components.PhotosSection
import com.example.gasgo.screens.components.TextWithLabel
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.GasStationViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserProfileScreen(
    navController: NavController?,
    viewModel: AuthViewModel?,
    gasStationViewModel: GasStationViewModel?,
    userData: CustomUser?,
    isMy: Boolean
){
    gasStationViewModel?.getUserGasStations(userData?.id!!)
    val gasStationsResource = gasStationViewModel?.userGasStations?.collectAsState()
    val gasStations = remember {
        mutableStateListOf<GasStation>()
    }
    gasStationsResource?.value.let {
        when(it){
            is Resource.Success -> {
                Log.d("Podaci", it.toString())
                gasStations.clear()
                gasStations.addAll(it.result)
            }
            is Resource.loading -> {

            }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
            null -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gasstationwallpaper),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 140.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = userData?.profileImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(5.dp, Color.White, CircleShape)
                                .background(
                                    Color.White,
                                    RoundedCornerShape(70.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userData?.fullName!!.replace('+', ' '),
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text =
                            if(userData.points <= 25) "Novajlija"
                            else if(userData.points <= 60) "Iskusni Vozač"
                            else "Veteran Puta"
                            ,
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TextWithLabel(label = "Dodatih benzinskih stanica", count = gasStations.count().toString())
                        TextWithLabel(label = "Posećenih benzinskih stanica", count = "2")
                        TextWithLabel(label = "Broj bodova", count = userData?.points.toString())
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
                    Text(text = "Osnovne informacije", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(13.dp))
                    if(isMy) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.Email, contentDescription = "")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = viewModel?.currentUser?.email ?: "Nema email-a")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.Phone, contentDescription = "")
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = userData?.phoneNumber ?: "Nema broja telefona")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                PhotosSection(gasStations = gasStations, navController = navController!!)
                Spacer(modifier = Modifier.height(30.dp))
                if(isMy) {
                    LogoutButton {

                        viewModel?.logout()
                        navController.navigate(Routes.loginScreen) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.padding(16.dp)) {
            CustomBackButton {
                navController?.popBackStack()
            }
        }
    }
}