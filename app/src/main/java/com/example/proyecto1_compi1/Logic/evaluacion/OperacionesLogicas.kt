package com.example.proyecto1_compi1.Logic.evaluacion

class OperacionesLogicas(
    private val convertidor: ConvertidorTipos,
    private val errores: MutableList<String>
) {

    fun evaluar(operador: String, izq: Any, der: Any): Any? {
        return when (operador) {
            "&&" -> logica(izq, der) { a, b -> a && b }
            "||" -> logica(izq, der) { a, b -> a || b }
            else -> null
        }
    }

    fun negar(valor: Any): Any? {
        val bool = convertidor.aBoolean(valor)
        if (bool != null) return !bool
        errores.add("No se puede aplicar negación '~' a '$valor'.")
        return null
    }

    private fun logica(izq: Any, der: Any, op: (Boolean, Boolean) -> Boolean): Any? {
        val boolIzq = convertidor.aBoolean(izq)
        val boolDer = convertidor.aBoolean(der)
        if (boolIzq != null && boolDer != null) return op(boolIzq, boolDer)
        errores.add("Operación lógica inválida entre '$izq' y '$der'.")
        return null
    }
}