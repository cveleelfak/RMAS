package com.example.gasgo

import android.view.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gasgo.router.Router
import com.example.gasgo.viewmodels.AuthViewModel

@Composable
fun GasGo(
    viewModel: AuthViewModel,
){
    Surface (modifier = Modifier.fillMaxSize()){
        Router(viewModel)
    }


}