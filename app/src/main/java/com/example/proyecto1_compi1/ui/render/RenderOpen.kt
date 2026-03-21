package com.example.proyecto1_compi1.ui.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.proyecto1_compi1.models.nodo2.Nodo2Open
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun RenderOpen(
    nodo: Nodo2Open,
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
        // Label / contenido de texto
        Text(
            text = nodo.contenido,
            color = misEstilos.color,
            fontFamily = misEstilos.fontFamily,
            fontSize = misEstilos.fontSize
        )

        if (esInteractivo && estado != null) {
            val id = remember { estado.siguienteId() }
            val respuesta = estado.getRespuestaTexto(id)

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = respuesta,
                onValueChange = { estado.setRespuestaTexto(id, it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Escribe tu respuesta...") },
                singleLine = true
            )
        } else if (!esInteractivo) {
            // Preview: campo visual no editable
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}
