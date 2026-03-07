package com.example.proyecto1_compi1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto1_compi1.ui.theme.Proyecto1_Compi1Theme
import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import com.example.proyecto1_compi1.models.Token
import com.example.proyecto1_compi1.ui.screens.FormCreatorScreen
import java.io.StringReader

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
                    // Pasamos el ViewModel como parámetro
                    FormCreatorScreen(viewModel = viewModel)
                }
            }
        }
    }
}

