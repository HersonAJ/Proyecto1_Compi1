// MainActivity.kt
package com.example.proyecto1_compi1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import com.example.proyecto1_compi1.ui.screens.MainScreen
import com.example.proyecto1_compi1.ui.theme.Proyecto1_Compi1Theme

class MainActivity : ComponentActivity() {

    private val viewModel: FormularioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Proyecto1_Compi1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}