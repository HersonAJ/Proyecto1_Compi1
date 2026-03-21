package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Select

@Composable
fun RenderSeleccion(
    nodo: Nodo2Select,
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
        if (esInteractivo && estado != null) {
            val id = remember {
                val newId = estado.siguienteId()
                if (nodo.correcto >= 0) {
                    estado.registrarCorrecto(newId, nodo.correcto.toInt())
                }
                newId
            }

            val seleccion = estado.getRespuestaIndice(id)

            nodo.opciones.forEachIndexed { index, opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    RadioButton(
                        selected = seleccion == index,
                        onClick = { estado.setRespuestaIndice(id, index) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = opcion,
                        color = misEstilos.color,
                        fontFamily = misEstilos.fontFamily,
                        fontSize = misEstilos.fontSize
                    )
                }
            }
        } else {
            // Preview
            nodo.opciones.forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    RadioButton(selected = false, onClick = null, enabled = false)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = opcion,
                        color = misEstilos.color,
                        fontFamily = misEstilos.fontFamily,
                        fontSize = misEstilos.fontSize
                    )
                }
            }
        }
    }
}