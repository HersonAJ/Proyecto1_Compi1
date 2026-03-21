package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Tabla

@Composable
fun RenderTabla(
    nodo: Nodo2Tabla,
    estilosPadre: EstilosResueltos,
    resolverEstilos: ResolverEstilos,
    estado: FormularioState?,
    esInteractivo: Boolean
) {
    val misEstilos = resolverEstilos.combinar(estilosPadre, nodo.estilos)
    val totalFilas = nodo.filas.size.coerceAtLeast(1)
    val altoFila = (nodo.height / totalFilas).dp

    val modifier = Modifier
        .width(nodo.width.dp)
        .heightIn(min = nodo.height.dp)
        .background(misEstilos.bgColor)
        .then(
            if (misEstilos.bordeGrosor > 0f) {
                Modifier.border(misEstilos.bordeGrosor.dp, misEstilos.bordeColor)
            } else Modifier
        )

    Column(modifier = modifier) {
        for (fila in nodo.filas) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = altoFila)
            ) {
                for (celda in fila) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .border(0.5.dp, misEstilos.bordeColor)
                            .padding(4.dp)
                    ) {
                        RenderElemento(celda, misEstilos, resolverEstilos, estado, esInteractivo)
                    }
                }
            }
        }
    }
}