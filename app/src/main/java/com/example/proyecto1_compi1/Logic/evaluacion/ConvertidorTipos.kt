package com.example.proyecto1_compi1.Logic.evaluacion

class ConvertidorTipos {

    fun aDouble(valor: Any): Double? {
        return when (valor) {
            is Double -> valor
            is Int -> valor.toDouble()
            is Boolean -> if (valor) 1.0 else 0.0
            else -> null
        }
    }

    fun aBoolean(valor: Any): Boolean? {
        return when (valor) {
            is Boolean -> valor
            is Double -> valor != 0.0
            else -> null
        }
    }

    fun valorAString(valor: Any?): String {
        return when (valor) {
            null -> "null"
            is Double -> {
                if (valor == valor.toLong().toDouble()) {
                    valor.toLong().toString()
                } else {
                    valor.toString()
                }
            }

            is Boolean -> if (valor) "true" else "false"
            else -> valor.toString()
        }
    }
}