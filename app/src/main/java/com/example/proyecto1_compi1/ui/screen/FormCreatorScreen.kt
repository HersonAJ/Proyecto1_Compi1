package com.example.proyecto1_compi1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel

@Composable
fun FormCreatorScreen(
    viewModel: FormularioViewModel,
    onNavigateToErrors: () -> Unit
) {

    val codigo by viewModel.codigo.collectAsState()
    val isAnalizando by viewModel.isAnalizando.collectAsState()
    val errores by viewModel.reporteErrores.collectAsState()

    // cálculo de fila y columna

    val cursorPos = codigo.selection.start
    val textoAntesCursor = codigo.text.take(cursorPos)

    val fila = textoAntesCursor.count { it == '\n' } + 1
    val columna = cursorPos - (textoAntesCursor.lastIndexOf('\n') + 1) + 1

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

            // Editor + indicador
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { viewModel.actualizarCodigo(it) },
                    modifier = Modifier
                        .fillMaxSize(),
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

                // indicador fila / columna

                Text(
                    text = "Ln $fila : Col $columna",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color(0xAA000000))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra inferior

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
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                        }
                    },
                    label = { Text("Analizar") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Add") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    label = { Text("Finish") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToErrors,
                    icon = { Icon(Icons.Default.Warning, contentDescription = null) },
                    label = { Text("Errores (${errores.size})") }
                )
            }
        }
    }
}