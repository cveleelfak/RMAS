package com.example.gasgo




import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gasgo.ui.theme.GasGoTheme
import com.example.gasgo.viewmodels.AuthViewModel
import com.example.gasgo.viewmodels.AuthViewModelFactory
import com.example.gasgo.viewmodels.GasStationViewModel
import com.example.gasgo.viewmodels.GasStationViewModelFactory

class MainActivity : ComponentActivity() {

    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }

    private val gasStationViewModel: GasStationViewModel by viewModels{
        GasStationViewModelFactory()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GasGo(userViewModel, gasStationViewModel)


        }
    }
}
