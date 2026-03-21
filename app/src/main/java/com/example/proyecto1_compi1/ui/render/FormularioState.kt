package com.example.proyecto1_compi1.ui.render

import androidx.compose.runtime.mutableStateMapOf

class FormularioState {

    //respuestas del usuario id -> valor
    val respuestas = mutableStateMapOf<Int, Any>()

    //correctos definidos por el formulario id -> valor esperado
    private val correctos = mutableStateMapOf<Int, Any>()

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
        val actuales = (respuestas[id] as? MutableSet<Int>) ?: mutableSetOf()
        if (actuales.contains(indice)) {
            actuales.remove(indice)
        } else {
            actuales.add(indice)
        }
        respuestas[id] = actuales
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
}