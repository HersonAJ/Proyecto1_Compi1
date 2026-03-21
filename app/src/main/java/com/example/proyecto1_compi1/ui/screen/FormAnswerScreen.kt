package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Programa
import com.example.proyecto1_compi1.ui.render.FormPreviewContent
import com.example.proyecto1_compi1.ui.render.FormularioState


@Composable
fun FormAnswerScreen(
    programa: Nodo2Programa,
    onBack: () -> Unit
) {
    val estado = remember { FormularioState() }
    var mostrarResultado by remember { mutableStateOf(false) }
    var resultadoTexto by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // Formulario interactivo
        Box(modifier = Modifier.weight(1f)) {
            FormPreviewContent(
                programa = programa,
                esInteractivo = true,
                estado = estado
            )
        }

        // Resultado (si se envió)
        if (mostrarResultado) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = resultadoTexto,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onBack) {
                Text("Back to edit")
            }

            Button(
                onClick = {
                    if (estado.tieneCorrectos()) {
                        val (aciertos, total) = estado.evaluar()
                        resultadoTexto = "Resultado: $aciertos / $total correctas"
                    } else {
                        resultadoTexto = "Formulario enviado exitosamente"
                    }
                    mostrarResultado = true
                }
            ) {
                Text("Send")
            }
        }
    }
}
