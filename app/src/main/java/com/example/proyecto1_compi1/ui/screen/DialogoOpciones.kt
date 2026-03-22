package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Opciones",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

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