package com.example.gasgo




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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

class MainActivity : ComponentActivity() {

    private val userViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GasGo(userViewModel)


        }
    }
}
