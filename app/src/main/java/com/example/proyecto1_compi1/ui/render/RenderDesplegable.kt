package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Drop

@Composable
fun RenderDesplegable(
    nodo: Nodo2Drop,
    estilosPadre: EstilosResueltos,
    resolverEstilos: ResolverEstilos,
    estado: FormularioState?,
    esInteractivo: Boolean
) {
    val misEstilos = resolverEstilos.combinar(estilosPadre, nodo.estilos)

    Column(
        modifier = Modifier
            .width(nodo.width.dp)
            .background(misEstilos.bgColor)
            .padding(4.dp)
    ) {
        Text(
            text = EmojiConverter.convertir(nodo.label),
            color = misEstilos.color,
            fontFamily = misEstilos.fontFamily,
            fontSize = misEstilos.fontSize
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (esInteractivo && estado != null) {
            val id = remember {
                val newId = estado.siguienteId()
                if (nodo.correcto >= 0) {
                    estado.registrarCorrecto(newId, nodo.correcto.toInt())
                }
                newId
            }

            var expandido by remember { mutableStateOf(false) }
            val seleccion = estado.getRespuestaIndice(id)
            val textoMostrar = if (seleccion >= 0 && seleccion < nodo.opciones.size) {
                EmojiConverter.convertir(nodo.opciones[seleccion])
            } else "Selecciona..."

            Box {
                OutlinedButton(onClick = { expandido = true }) {
                    Text(textoMostrar)
                }

                DropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    nodo.opciones.forEachIndexed { index, opcion ->
                        DropdownMenuItem(
                            text = { Text(EmojiConverter.convertir(opcion)) },
                            onClick = {
                                estado.setRespuestaIndice(id, index)
                                expandido = false
                            }
                        )
                    }
                }
            }
        } else {
            // Preview
            OutlinedButton(onClick = {}, enabled = false) {
                Text("Selecciona...")
            }
        }
    }
}