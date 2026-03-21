package com.example.proyecto1_compi1.ui.render

import androidx.compose.runtime.Composable
import com.example.proyecto1_compi1.models.nodo2.Nodo2Drop
import com.example.proyecto1_compi1.models.nodo2.Nodo2Elemento
import com.example.proyecto1_compi1.models.nodo2.Nodo2Multiple
import com.example.proyecto1_compi1.models.nodo2.Nodo2Open
import com.example.proyecto1_compi1.models.nodo2.Nodo2Seccion
import com.example.proyecto1_compi1.models.nodo2.Nodo2Select
import com.example.proyecto1_compi1.models.nodo2.Nodo2Tabla

@Composable
fun RenderElemento(
    nodo: Nodo2Elemento,
    estilosPadre: EstilosResueltos,
    resolverEstilos: ResolverEstilos,
    estado: FormularioState?,
    esInteractivo: Boolean
) {
    when (nodo) {
        is Nodo2Seccion -> RenderSeccion(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
        is Nodo2Tabla -> RenderTabla(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
        is Nodo2Open -> RenderOpen(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
        is Nodo2Drop -> RenderDesplegable(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
        is Nodo2Select -> RenderSeleccion(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
        is Nodo2Multiple -> RenderMultiple(nodo, estilosPadre, resolverEstilos, estado, esInteractivo)
    }
}