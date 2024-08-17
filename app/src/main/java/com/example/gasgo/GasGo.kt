package com.example.gasgo

import android.view.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gasgo.router.Router

@Composable
fun GasGo(){
    Surface (modifier = Modifier.fillMaxSize()){
        Router()
    }


}