package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Seccion

@Composable
fun RenderSeccion(
    nodo: Nodo2Seccion,
    estilosPadre: EstilosResueltos,
    resolverEstilos: ResolverEstilos,
    estado: FormularioState?,
    esInteractivo: Boolean
) {
    val misEstilos = resolverEstilos.combinar(estilosPadre, nodo.estilos)

    val modifier = Modifier
        .width(nodo.width.dp)
        .heightIn(min = nodo.height.dp)
        .background(misEstilos.bgColor)
        .then(
            if (misEstilos.bordeGrosor > 0f) {
                Modifier.border(misEstilos.bordeGrosor.dp, misEstilos.bordeColor)
            } else Modifier
        )
        .padding(4.dp)

    if (nodo.orientacion == "HORIZONTAL") {
        Row(modifier = modifier) {
            for (elemento in nodo.contenido) {
                RenderElemento(elemento, misEstilos, resolverEstilos, estado, esInteractivo)
            }
        }
    } else {
        Column(modifier = modifier) {
            for (elemento in nodo.contenido) {
                RenderElemento(elemento, misEstilos, resolverEstilos, estado, esInteractivo)
            }
        }
    }
}