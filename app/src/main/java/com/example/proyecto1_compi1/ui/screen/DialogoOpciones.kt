package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogoOpciones(
    onAbrirForm: () -> Unit,
    onGuardarForm: () -> Unit,
    onAbrirPkm: () -> Unit,
    onGuardarPkm: () -> Unit,
    onSubirServidor: () -> Unit,
    onExplorarServidor: () -> Unit,
    onConfigServidor: () -> Unit,
    onCerrar: () -> Unit
) {
    Dialog(onDismissRequest = onCerrar) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Opciones",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Archivos locales",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedButton(
                    onClick = { onAbrirForm(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir archivo de código (.form)")
                }

                OutlinedButton(
                    onClick = { onGuardarForm(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar archivo de código (.form)")
                }

                OutlinedButton(
                    onClick = { onAbrirPkm(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir formulario (.pkm)")
                }

                OutlinedButton(
                    onClick = { onGuardarPkm(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar formulario (.pkm)")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Servidor",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedButton(
                    onClick = { onSubirServidor(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Subir formulario al servidor")
                }

                OutlinedButton(
                    onClick = { onExplorarServidor(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Explorar formularios del servidor")
                }

                OutlinedButton(
                    onClick = { onConfigServidor(); onCerrar() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Configurar servidor")
                }

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    onClick = onCerrar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}