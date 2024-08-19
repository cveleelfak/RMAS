package com.example.gasgo.router

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gasgo.data.Resource
import com.example.gasgo.model.CustomUser
import com.example.gasgo.model.GasStation
import com.example.gasgo.screens.IndexScreen
import com.example.gasgo.screens.LoginScreen
import com.example.gasgo.screens.RegisterScreen
import com.example.gasgo.screens.UserProfileScreen
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.GasStationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Router(
    viewModel: AuthViewModel,
    gasStationViewModel: GasStationViewModel
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.loginScreen){
        composable(Routes.loginScreen){
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Routes.indexScreen){
            val gasStationsResource = gasStationViewModel.gasStations.collectAsState()
            val gasStationMarkers = remember {
                mutableListOf<GasStation>()
            }
            gasStationsResource.value.let {
                when(it){
                    is Resource.Success -> {
                        gasStationMarkers.clear()
                        gasStationMarkers.addAll(it.result)
                    }
                    is Resource.loading -> {

                    }
                    is Resource.Failure -> {
                        Log.e("Podaci", it.toString())
                    }
                    null -> {}
                }
            }
            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                gasStationViewModel = gasStationViewModel,
                gasStationMarkers = gasStationMarkers
            )
        }
        composable(Routes.registerScreen){
            RegisterScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Routes.userProfileScreen + "/{userData}",
            arguments = listOf(navArgument("userData"){
                type = NavType.StringType
            })
        ){backStackEntry ->
            val userDataJson = backStackEntry.arguments?.getString("userData")
            val userData = Gson().fromJson(userDataJson, CustomUser::class.java)
            val isMy = FirebaseAuth.getInstance().currentUser?.uid == userData.id
            UserProfileScreen(
                navController = navController,
                viewModel = viewModel,
                gasStationViewModel = gasStationViewModel,
                userData = userData,
                isMy = isMy
            )
        }

    }
}