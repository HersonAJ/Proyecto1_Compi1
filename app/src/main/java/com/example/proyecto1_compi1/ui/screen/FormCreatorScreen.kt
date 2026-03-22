package com.example.proyecto1_compi1.ui.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.Logic.ManejadorArchivos
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import com.example.proyecto1_compi1.ui.render.FormPreviewContent
import com.example.proyecto1_compi1.ui.screen.DebugDialog
import com.example.proyecto1_compi1.ui.screen.DialogoOpciones

@Composable
fun FormCreatorScreen(
    viewModel: FormularioViewModel,
    onNavigateToErrors: () -> Unit,
    onNavigateToAnswer: () -> Unit
) {
    val context = LocalContext.current
    val manejadorArchivos = remember { ManejadorArchivos() }

    val codigo by viewModel.codigo.collectAsState()
    val isAnalizando by viewModel.isAnalizando.collectAsState()
    val errores by viewModel.reporteErrores.collectAsState()
    val ast2 by viewModel.ast2.collectAsState()

    //estados para el debug dialog
    val mostrarDebug by viewModel.mostrarDebug.collectAsState()
    val codigoPkm by viewModel.codigoPkm.collectAsState()

    // Estado para el diálogo de opciones
    var mostrarOpciones by remember { mutableStateOf(false) }

    // Cálculo de fila y columna
    val cursorPos = codigo.selection.start
    val textoAntesCursor = codigo.text.take(cursorPos)
    val fila = textoAntesCursor.count { it == '\n' } + 1
    val columna = cursorPos - (textoAntesCursor.lastIndexOf('\n') + 1) + 1

//Abrir archivo .form
    val selectorAbrirForm = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val extension = manejadorArchivos.obtenerExtension(context, uri)
            if (extension in ManejadorArchivos.EXTENSIONES_FORM) {
                val contenido = manejadorArchivos.leerArchivo(context, uri)
                if (contenido != null) {
                    viewModel.actualizarCodigo(TextFieldValue(contenido))
                } else {
                    Toast.makeText(context, "El archivo es demasiado grande o no se pudo leer", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Archivo inválido. Se esperaba .form o .txt", Toast.LENGTH_SHORT).show()
            }
        }
    }

// Abrir archivo .pkm
    val selectorAbrirPkm = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val extension = manejadorArchivos.obtenerExtension(context, uri)
            if (extension in ManejadorArchivos.EXTENSIONES_PKM) {
                val contenido = manejadorArchivos.leerArchivo(context, uri)
                if (contenido != null) {
                    viewModel.cargarPkm(contenido)
                } else {
                    Toast.makeText(context, "El archivo es demasiado grande o no se pudo leer", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Archivo inválido. Se esperaba .pkm o .txt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Guardar archivo .form
    val selectorGuardarForm = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        if (uri != null) {
            manejadorArchivos.guardarArchivo(context, uri, codigo.text)
        }
    }

    // Guardar archivo .pkm
    val selectorGuardarPkm = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        if (uri != null && codigoPkm != null) {
            manejadorArchivos.guardarArchivo(context, uri, codigoPkm!!)
        }
    }

    if (mostrarDebug) {
        DebugDialog(
            codigoPkm = codigoPkm,
            errores = errores,
            ast2Exitoso = ast2 != null,
            onDismiss = { viewModel.cerrarDebug() }
        )
    }

    if (mostrarOpciones) {
        DialogoOpciones(
            onAbrirForm = {
                selectorAbrirForm.launch(arrayOf("*/*"))
            },
            onGuardarForm = {
                selectorGuardarForm.launch("formulario.form")
            },
            onAbrirPkm = {
                selectorAbrirPkm.launch(arrayOf("*/*"))
            },
            onGuardarPkm = {
                selectorGuardarPkm.launch("formulario.pkm")
            },
            onCerrar = { mostrarOpciones = false }
        )
    }

    //interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        // parte superior
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if (ast2 != null) {
                FormPreviewContent(programa = ast2!!, esInteractivo = false)
            }
        }

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
                    modifier = Modifier.fillMaxSize(),
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
                    onClick = { viewModel.analizarCodigo() },
                    icon = {
                        if (isAnalizando) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                        }
                    },
                    label = { Text("Analizar") }
                )

                // Opciones (abrir/guardar archivos)
                NavigationBarItem(
                    selected = false,
                    onClick = { mostrarOpciones = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Opciones") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        val ast2Value = viewModel.ast2.value
                        if (ast2Value != null) {
                            onNavigateToAnswer()
                        }
                    },
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    label = { Text("Responder") }
                )

                // Errores
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