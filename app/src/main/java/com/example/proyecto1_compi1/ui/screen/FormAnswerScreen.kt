package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var mostrarDialogo by remember { mutableStateOf(false) }
    var resumen by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // Formulario interactivo
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            FormPreviewContent(
                programa = programa,
                esInteractivo = true,
                estado = estado
            )
        }


        // Botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onBack) {
                Text("Editar")
            }

            Button(
                onClick = {
                    if (estado.tieneCorrectos()) {
                        resumen = estado.obtenerResumen()
                    } else {
                        resumen = "Formulario enviado exitosamente"
                    }
                    mostrarDialogo = true
                }
            ) { Text("Enviar") }

// El dialogo:
            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("Resultado") },
                    text = { Text(resumen) },
                    confirmButton = {
                        Button(onClick = { mostrarDialogo = false }) { Text("Aceptar") }
                    }
                )
            }
        }
    }
}