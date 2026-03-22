package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.Logic.FormularioRemoto
import com.example.proyecto1_compi1.Logic.ServicioServidor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.items

@Composable
fun PantallaExplorar(
    servicioServidor: ServicioServidor,
    onDescargar: (String) -> Unit,
    onVolver: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var formularios by remember { mutableStateOf<List<FormularioRemoto>>(emptyList()) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Cargar al entrar
    LaunchedEffect(Unit) {
        cargando = true
        withContext(Dispatchers.IO) {
            formularios = servicioServidor.listarFormularios()
            if (servicioServidor.getErrores().isNotEmpty()) {
                error = servicioServidor.getErrores().joinToString("\n")
                servicioServidor.limpiarErrores()
            }
        }
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Barra superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            Text(
                text = "Explorar Formularios",
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(onClick = {
                scope.launch {
                    cargando = true
                    error = null
                    withContext(Dispatchers.IO) {
                        formularios = servicioServidor.listarFormularios()
                        if (servicioServidor.getErrores().isNotEmpty()) {
                            error = servicioServidor.getErrores().joinToString("\n")
                            servicioServidor.limpiarErrores()
                        }
                    }
                    cargando = false
                }
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Recargar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Estado
        if (cargando) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error!!,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // Lista de formularios
        if (formularios.isEmpty() && !cargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay formularios en el servidor")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(formularios) { formulario ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = formulario.nombre,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "${formulario.autor} - ${formulario.fecha} ${formulario.hora}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        cargando = true
                                        val contenido = withContext(Dispatchers.IO) {
                                            servicioServidor.descargarFormulario(formulario.nombre)
                                        }
                                        cargando = false
                                        if (contenido != null) {
                                            onDescargar(contenido)
                                        }
                                    }
                                }
                            ) {
                                Text("Abrir")
                            }
                        }
                    }
                }
            }
        }
    }
}
