package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Programa

@Composable
fun FormPreviewContent(
    programa: Nodo2Programa,
    esInteractivo: Boolean = false,
    estado: FormularioState? = null
) {
    val resolverEstilos = remember { ResolverEstilos() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        for (elemento in programa.elementos) {
            RenderElemento(
                nodo = elemento,
                estilosPadre = EstilosResueltos.DEFAULT,
                resolverEstilos = resolverEstilos,
                estado = estado,
                esInteractivo = esInteractivo
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
