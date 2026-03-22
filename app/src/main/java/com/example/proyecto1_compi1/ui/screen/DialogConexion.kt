package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogoSubirServidor(
    onSubir: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Subir al servidor",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del formulario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCerrar) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank()) {
                                onSubir(nombre)
                                onCerrar()
                            }
                        }
                    ) {
                        Text("Subir")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogoConfigServidor(
    ipActual: String,
    onGuardar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var ip by remember { mutableStateOf(ipActual) }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Configurar servidor",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP del servidor (ej: 192.168.1.100)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCerrar) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (ip.isNotBlank()) {
                                onGuardar(ip)
                                onCerrar()
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}