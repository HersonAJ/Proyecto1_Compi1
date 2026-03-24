package com.example.proyecto1_compi1.ui.render

import androidx.compose.runtime.mutableStateMapOf

class FormularioState {

    //respuestas del usuario id -> valor
    val respuestas = mutableStateMapOf<Int, Any>()

    //correctos definidos por el formulario id -> valor esperado
    private val correctos = mutableStateMapOf<Int, Any>()


    private val labels = mutableStateMapOf<Int, String>()
    private val opcionesMap = mutableStateMapOf<Int, List<String>>()

    //contador pra asignar id unicos a cada pregunta
    private var contadorId = 0

    fun siguienteId(): Int = contadorId++

    fun resetear() {
        respuestas.clear()
        correctos.clear()
        contadorId = 0
    }


    fun setRespuestaTexto(id: Int, valor: String) {
        respuestas[id] = valor
    }

    fun getRespuestaTexto(id: Int): String {
        return respuestas[id] as? String ?: ""
    }

    fun setRespuestaIndice(id: Int, indice: Int) {
        respuestas[id] = indice
    }

    fun getRespuestaIndice(id: Int): Int {
        return respuestas[id] as? Int ?: 0
    }

    @Suppress("UNCHECKED_CAST")
    fun toggleMultiple(id: Int, indice: Int) {
        val actuales = (respuestas[id] as? Set<Int>) ?: emptySet()
        respuestas[id] = if (actuales.contains(indice)) {
            actuales - indice
        } else {
            actuales + indice
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getMultiples(id: Int): Set<Int> {
        return (respuestas[id] as? Set<Int>) ?: emptySet()
    }

    // ===== Correctos =====

    fun registrarCorrecto(id: Int, valor: Any) {
        correctos[id] = valor
    }

    fun tieneCorrectos(): Boolean = correctos.isNotEmpty()

    /**
     * Evalúa las respuestas y retorna (aciertos, total)
     */
    @Suppress("UNCHECKED_CAST")
    fun evaluar(): Pair<Int, Int> {
        var aciertos = 0
        val total = correctos.size

        for ((id, correcto) in correctos) {
            val respuesta = respuestas[id]
            val esCorrecta = when (correcto) {
                is Int -> respuesta == correcto
                is Set<*> -> respuesta == correcto
                else -> false
            }
            if (esCorrecta) aciertos++
        }

        return Pair(aciertos, total)
    }

    fun registrarLabel(id: Int, label: String) { labels[id] = label }
    fun registrarOpciones(id: Int, opciones: List<String>) { opcionesMap[id] = opciones }

    @Suppress("UNCHECKED_CAST")
    fun obtenerResumen(): String {
        val sb = StringBuilder()
        val (aciertos, total) = evaluar()
        sb.append("Resultado: $aciertos / $total\n\n")
        for ((id, correcto) in correctos) {
            val label = labels[id] ?: "Pregunta $id"
            val opciones = opcionesMap[id] ?: emptyList()
            val respuesta = respuestas[id]
            val esCorrecta = when (correcto) {
                is Int -> respuesta == correcto
                is Set<*> -> respuesta == correcto
                else -> false
            }
            val marca = if (esCorrecta) "OK" else "MAL"
            val correctaTexto = when (correcto) {
                is Int -> opciones.getOrElse(correcto) { "$correcto" }
                is Set<*> -> (correcto as Set<Int>).mapNotNull { opciones.getOrNull(it) }.joinToString(", ")
                else -> "$correcto"
            }
            sb.append("[$marca] $label\n  Correcta: $correctaTexto\n\n")
        }
        return sb.toString()
    }
}