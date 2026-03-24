package com.example.proyecto1_compi1.ui.render


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Seccion
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
@Composable
fun RenderSeccion(
    nodo: Nodo2Seccion,
    estilosPadre: EstilosResueltos,
    resolverEstilos: ResolverEstilos,
    estado: FormularioState?,
    esInteractivo: Boolean
) {
    val misEstilos = resolverEstilos.combinar(estilosPadre, nodo.estilos)
/* forma anterior que depende de la entrada regresar aqui si es necesario
    val modifier = Modifier
        .width(nodo.width.dp)*/
    val modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = nodo.height.dp)
        .heightIn(min = nodo.height.dp)
        .background(misEstilos.bgColor)
        .then(aplicarBorde(misEstilos))
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

fun aplicarBorde(estilos: EstilosResueltos): Modifier {
    if (estilos.bordeGrosor <= 0f) return Modifier

    return when (estilos.bordeTipo) {
        "DOTTED" -> Modifier.drawBehind {
            drawRect(
                color = estilos.bordeColor,
                style = Stroke(
                    width = estilos.bordeGrosor.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 0f
                    )
                )
            )
        }
        "DOUBLE" -> Modifier
            .border(estilos.bordeGrosor.dp, estilos.bordeColor)
            .padding((estilos.bordeGrosor + 2).dp)
            .border(estilos.bordeGrosor.dp, estilos.bordeColor)
        else -> Modifier.border(estilos.bordeGrosor.dp, estilos.bordeColor)
    }
}