package com.example.proyecto1_compi1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel

@Composable
fun FormCreatorScreen(
    viewModel: FormularioViewModel
) {
    val codigo by viewModel.codigo.collectAsState()
    val tokens by viewModel.tokens.collectAsState()
    val erroresLexicos by viewModel.erroresLexicos.collectAsState()
    val erroresSintacticos by viewModel.erroresSintacticos.collectAsState()

    val isAnalizando by viewModel.isAnalizando.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            when {
                tokens.isNotEmpty() -> {
                    // Mostrar tokens
                }
                erroresLexicos.isNotEmpty() -> {
                    Column {
                        Text("Errores Léxicos (${erroresLexicos.size}):", color = Color.Red)
                        erroresLexicos.take(5).forEach { error ->
                            Text("• Línea ${error.linea}: ${error.descripcion} (${error.lexema})", color = Color.Red)
                        }
                    }
                }
                erroresSintacticos.isNotEmpty() -> {
                    Column {
                        Text("Errores Sintácticos (${erroresSintacticos.size}):", color = Color.Red)
                        erroresSintacticos.take(5).forEach { error ->
                            Text("• Línea ${error.linea}: ${error.descripcion}", color = Color.Red)
                        }
                    }
                }
                else -> Text("FORMULARIO", style = MaterialTheme.typography.headlineMedium)
            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        // Área de código
        OutlinedTextField(
            value = codigo,
            onValueChange = { viewModel.actualizarCodigo(it) },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            placeholder = { Text("Escribe aqui el codigo...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.White, //momentaneamente blanco cuando se analize se cambiara
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.analizarLexicamente() },
                modifier = Modifier.weight(1f),
                enabled = !isAnalizando
            ) {
                if (isAnalizando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Analizar")
                }
            }

            Button(
                onClick = { /* Add */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Add")
            }

            Button(
                onClick = { /* Finish */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Finish")
            }
        }

        // Errores
        if (erroresLexicos.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE0E0)
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Errores Lexicos:",
                        color = Color.Red,
                        style = MaterialTheme.typography.titleSmall
                    )
                    erroresLexicos.take(3).forEach { error ->
                        Text(
                            text = "Lexema: ${error.lexema}, Línea ${error.linea}: ${error.descripcion}",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}