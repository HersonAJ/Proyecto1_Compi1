package com.example.proyecto1_compi1.Logic.evaluacion

class OperacionesRelacionales (
    private val convertidor: ConvertidorTipos,
    private val errores: MutableList<String>
    ) {

    fun evaluar(operador: String, izq: Any, der: Any): Any? {
        return when (operador) {
            ">" -> comparar(izq, der) { a, b -> a > b }
            "<" -> comparar(izq, der) { a, b -> a < b }
            ">=" -> comparar(izq, der) { a, b -> a >= b }
            "<=" -> comparar(izq, der) { a, b -> a <= b }
            "==" -> igualdad(izq, der)
            "!!" -> desigualdad(izq, der)
            else -> null
        }
    }

    private fun comparar(izq: Any, der: Any, op: (Double, Double) -> Boolean): Any? {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer != null) return op(numIzq, numDer)
        errores.add("comparacion invalida entre '$izq' y '$der'")
        return null
    }

    private fun igualdad(izq: Any, der: Any): Boolean {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer != null) return numIzq == numDer
        if (izq is String && der is String) return izq == der
        return false
    }

    private fun desigualdad(izq: Any, der: Any): Boolean {
        return !igualdad(izq, der)
    }
}