package com.example.wander


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.theme.WanderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val sharedPreferences =
                        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    val viewModel = LoginViewModel(sharedPreferences)
                    WanderApp(viewModel)

                }
            }
        }
    }

}
