package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyecto1_compi1.models.ErrorReporte

@Composable
fun DebugDialog(
    codigoPkm: String?,
    errores: List<ErrorReporte>,
    ast2Exitoso: Boolean = false,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    val textoDebug = remember(codigoPkm, errores, ast2Exitoso) {
        buildString {
            if (errores.isNotEmpty()) {
                append("========== ERRORES (${errores.size}) ==========\n\n")
                for (error in errores) {
                    append("[${error.tipo}] Línea ${error.linea}, Col ${error.columna}\n")
                    append("  ${error.descripcion}\n\n")
                }
            } else {
                append("========== SIN ERRORES ==========\n\n")
            }

            append("========== ANALISIS L2 ==========\n\n")
            if (ast2Exitoso) {
                append("AST2 construido exitosamente \n\n")
            } else {
                append("AST2 no se construyo \n\n")
            }

            append("========== CODIGO .PKM GENERADO ==========\n\n")
            if (codigoPkm != null) {
                append(codigoPkm)
            } else {
                append("(null - no se genero codigo)")
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    text = "Debug - Salida del Compilador",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .verticalScroll(scrollState)
                        .horizontalScroll(horizontalScroll)
                ) {
                    Text(
                        text = textoDebug,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(textoDebug))
                        }
                    ) {
                        Text("Copiar")
                    }

                    Button(onClick = onDismiss) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}