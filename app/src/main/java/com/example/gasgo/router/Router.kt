package com.example.gasgo.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.gasgo.screens.IndexScreen
import com.example.gasgo.screens.LoginScreen

@Composable
fun Router(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.loginScreen){
        composable(Routes.loginScreen){
            LoginScreen()
        }
        composable(Routes.indexScreen){
            IndexScreen()
        }

    }
}