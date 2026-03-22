// ErrorReportScreen.kt
package com.example.proyecto1_compi1.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.RowScope
import androidx.compose.ui.Alignment
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel

@Composable
fun ErrorReportScreen(
    viewModel: FormularioViewModel,
    onBack: () -> Unit
) {
    val errores by viewModel.reporteErrores.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Encabezado superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Reporte de Errores",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onBack) {
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay errores para mostrar")
            }
        } else {
            // Tabla completa
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Encabezado de tabla
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TableHeader("Lexema", 1f)
                        TableHeader("Linea", 1f)
                        TableHeader("Columna", 1f)
                        TableHeader("Tipo", 1f)
                        TableHeader("Descripcion", 3f)
                    }
                    HorizontalDivider()
                }

                // Filas de errores
                items(errores) { error ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TableCell(error.lexema, 1f)
                        TableCell(error.linea.toString(), 1f)
                        TableCell(error.columna.toString(), 1f)
                        TableCell(error.tipo, 1f)
                        TableCell(error.descripcion, 3f)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun RowScope.TableHeader(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(6.dp),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(6.dp),
        style = MaterialTheme.typography.bodySmall
    )
}
