package com.example.proyecto1_compi1.models

class TablaSimbolos {

    private val simbolos = HashMap<String, Simbolo>()
    private val errores = mutableListOf<String>()

    //analisis semantico para cuando una variable ya fue declarada
    fun declarar(nombre: String, tipo: String, valor: Any?) : Boolean {
        if (simbolos.containsKey(nombre)) {
            errores.add("Variable '$nombre' ya fue declarada.")
            return false
        }
    //analisis semantico cuando una variable no fue asignada
        val valorFinal = valor ?: when (tipo) {
            "number" -> 0.0
            "string" -> ""
            else -> null
        }

        simbolos[nombre] = Simbolo(nombre, tipo, valorFinal)
        return true
    }

    //analisis semantico cuando una variable no fue declarada
    fun asignar(nombre: String, valor: Any?) : Boolean {
        val simbolo = simbolos[nombre]

        if (simbolo == null) {
            errores.add("Variable '$nombre' no ha sido declarada")
            return false
        }
    //analisis sintactico cuando se asigna un tipo incorrecto
        if (!tipoCompatible(simbolo.tipo, valor)) {
            errores.add("No se puede asignar ${tipoDeValor(valor)} a variable $nombre de tipo ${simbolo.tipo}.")
            return false
        }

        simbolo.valor = valor
        return true
    }

    fun obtener(nombre: String) : Any? {
        val simbolo = simbolos[nombre]
        if (simbolo == null) {
            errores.add("Variable '$nombre' no ha sido declarada.")
            return null
        }
        return simbolo.valor
    }

    fun existe(nombre: String) : Boolean = simbolos.containsKey((nombre))

    fun obtenerTipo(nombre: String) : String? = simbolos[nombre]?.tipo

    fun getErrores(): List<String> = errores.toList()

    fun limpiarErrores() {  errores.clear() }

    fun limpiar() {
        simbolos.clear()
        errores.clear()
    }

    //metodo para validar la compatibilidad de los tipos en las variables
    private fun tipoCompatible(tipo: String, valor: Any?): Boolean {
        if (valor == null) return true
        return when (tipo) {
            "number" -> valor is Double
            "string" -> valor is String
            "special" -> true
            else -> false
        }
    }

    private fun tipoDeValor(valor: Any?): String {
        return when (valor) {
            is Double -> "number"
            is String -> "string"
            null -> "null"
            else -> valor::class.simpleName ?: "desconocido"
        }
    }
}