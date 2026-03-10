// FormCreatorScreen.kt
package com.example.proyecto1_compi1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning

@Composable
fun FormCreatorScreen(
    viewModel: FormularioViewModel,
    onNavigateToErrors: () -> Unit
) {

    val codigo by viewModel.codigo.collectAsState()
    val isAnalizando by viewModel.isAnalizando.collectAsState()
    val errores by viewModel.reporteErrores.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // parte superior
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        //parte inferior
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            // Editor de código con fondo negro
            OutlinedTextField(
                value = codigo,
                onValueChange = { viewModel.actualizarCodigo(it) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                placeholder = { Text("Escribe aquí el código...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Analizar
                NavigationBarItem(
                    selected = false,
                    onClick = { viewModel.analizarLexicamente() },
                    icon = {
                        if (isAnalizando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Box(modifier = Modifier.size(0.dp))
                        }
                    },
                    label = { Text("Analizar") }
                )

                // Add
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Box(modifier = Modifier.size(0.dp)) },
                    label = { Text("Add") }
                )

                // Finish
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Box(modifier = Modifier.size(0.dp)) },
                    label = { Text("Finish") }
                )

                // Errores
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToErrors,
                    icon = { Box(modifier = Modifier.size(0.dp)) },
                    label = { Text("Errores (${errores.size})") }
                )
            }
        }
    }
}