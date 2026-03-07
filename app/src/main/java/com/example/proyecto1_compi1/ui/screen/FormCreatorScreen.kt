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
    val errores by viewModel.errores.collectAsState()
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
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tokens (${tokens.size}):",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Mostrar los primeros 10 tokens para no saturar
                        tokens.take(10).forEachIndexed { index, token ->
                            Text(
                                text = "${index + 1}. ${token.tipo}: \"${token.lexema}\" (Línea ${token.linea})",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }

                        if (tokens.size > 10) {
                            Text(
                                text = "... y ${tokens.size - 10} más",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
                errores.isNotEmpty() -> {
                    Column {
                        Text(
                            text = "Errores (${errores.size}):",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        errores.take(5).forEach { error ->
                            Text(
                                text = "• Línea ${error.linea}: ${error.descripcion} (${error.lexema})",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                else -> Text(
                    text = "FORMULARIO",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
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
        if (errores.isNotEmpty()) {
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
                    errores.take(3).forEach { error ->
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