package com.example.gasgo.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.gasgo.screens.IndexScreen
import com.example.gasgo.screens.LoginScreen
import com.example.gasgo.viewmodels.AuthViewModel

@Composable
fun Router(
    viewModel: AuthViewModel
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
            IndexScreen()
        }

    }
}