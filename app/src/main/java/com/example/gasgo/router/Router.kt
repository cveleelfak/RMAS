package com.example.gasgo.router

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gasgo.data.Resource
import com.example.gasgo.model.CustomUser
import com.example.gasgo.model.GasStation
import com.example.gasgo.screens.GasStationScreen
import com.example.gasgo.screens.IndexScreen
import com.example.gasgo.screens.LoginScreen
import com.example.gasgo.screens.RankingScreen
import com.example.gasgo.screens.RegisterScreen
import com.example.gasgo.screens.SettingScreen
import com.example.gasgo.screens.TableScreen
import com.example.gasgo.screens.UserProfileScreen
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.GasStationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.maps.android.compose.rememberCameraPositionState

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

        composable(Routes.settingsScreen){
            SettingScreen(navController = navController)
        }

        composable(Routes.rankingScreen){
            RankingScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Routes.tableScreen + "/{gasStations}",
            arguments = listOf(navArgument("gasStations") { type = NavType.StringType })
        ){ backStackEntry ->
            val gasStationsJson = backStackEntry.arguments?.getString("gasStations")
            val gasStations = Gson().fromJson(gasStationsJson, Array<GasStation>::class.java).toList()
            TableScreen(gasStations = gasStations, navController = navController, gasStationViewModel = gasStationViewModel)
        }

        //

        composable(
            route = Routes.gasStationScreen + "/{gasStation}",
            arguments = listOf(
                navArgument("gasStation"){ type = NavType.StringType }
            )
        ){backStackEntry ->
            val gasStationJson = backStackEntry.arguments?.getString("gasStation")
            val gasStation = Gson().fromJson(gasStationJson, GasStation::class.java)
            gasStationViewModel.getGasStationAllRates(gasStation.id)
            GasStationScreen(
                gasStation = gasStation,
                navController = navController,
                gasStationViewModel = gasStationViewModel,
                viewModel = viewModel,
                gasStations = null
            )
        }
        composable(
            route = Routes.gasStationScreen + "/{gasStation}/{gasStations}",
            arguments = listOf(
                navArgument("gasStation"){ type = NavType.StringType },
                navArgument("gasStations"){ type = NavType.StringType },
            )
        ){backStackEntry ->
            val gasStationsJson = backStackEntry.arguments?.getString("gasStations")
            val gasStations = Gson().fromJson(gasStationsJson, Array<GasStation>::class.java).toList()
            val gasStationJson = backStackEntry.arguments?.getString("gasStation")
            val gasStation = Gson().fromJson(gasStationJson, GasStation::class.java)
            gasStationViewModel.getGasStationAllRates(gasStation.id)

            GasStationScreen(
                gasStation = gasStation,
                navController = navController,
                gasStationViewModel = gasStationViewModel,
                viewModel = viewModel,
                gasStations = gasStations.toMutableList()
            )
        }

        //
        //

        composable(
            route = Routes.indexScreenWithParams + "/{isCameraSet}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("isCameraSet") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val isCameraSet = backStackEntry.arguments?.getBoolean("isCameraSet")
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")

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
                isCameraSet = remember { mutableStateOf(isCameraSet!!) },
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude!!.toDouble(), longitude!!.toDouble()), 17f)
                },
                gasStationMarkers = gasStationMarkers
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{isCameraSet}/{latitude}/{longitude}/{gasStations}",
            arguments = listOf(
                navArgument("isCameraSet") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("gasStations") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val isCameraSet = backStackEntry.arguments?.getBoolean("isCameraSet")
            val latitude = backStackEntry.arguments?.getFloat("latitude")
            val longitude = backStackEntry.arguments?.getFloat("longitude")
            val gasStationsJson = backStackEntry.arguments?.getString("gasStations")
            val gasStations = Gson().fromJson(gasStationsJson, Array<GasStation>::class.java).toList()

            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                gasStationViewModel = gasStationViewModel,
                isCameraSet = remember { mutableStateOf(isCameraSet!!) },
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude!!.toDouble(), longitude!!.toDouble()), 17f)
                },
                gasStationMarkers = gasStations.toMutableList(),
                isFilteredParam = true
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{gasStations}",
            arguments = listOf(
                navArgument("gasStations") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gasStationsJson = backStackEntry.arguments?.getString("gasStations")
            val gasStations = Gson().fromJson(gasStationsJson, Array<GasStation>::class.java).toList()
            IndexScreen(
                viewModel = viewModel,
                navController = navController,
                gasStationViewModel = gasStationViewModel,
                gasStationMarkers = gasStations.toMutableList(),
                isFilteredParam = true
            )
        }

    }
}