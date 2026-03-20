package com.example.proyecto1_compi1.Logic.evaluacion

class OperacionesAritmeticas (

    private val convertidor: ConvertidorTipos,
    private val errores: MutableList<String>
    ){

    fun evaluar(operador: String, izq: Any, der: Any): Any? {
        return when (operador) {
            "+" -> suma(izq, der)
            "-" -> aritmetica(izq, der) { a, b -> a - b }
            "*" -> aritmetica(izq, der) { a,b -> a * b }
            "/" -> division(izq, der)
            "^" -> aritmetica(izq, der) { a, b -> Math.pow(a, b) }
            "%" -> modulo (izq, der)
            else -> null
        }
    }

    fun negarNumero(valor: Any): Any? {
        val num = convertidor.aDouble(valor)

        if (num != null) return -num
        errores.add("no se puede negar '$valor', se esperaba un numero")
        return null
    }

    fun positivo(valor: Any): Any? {
        val num = convertidor.aDouble(valor)
        if (num != null) return num
        errores.add("Operador '+' unario solo aplica a numeros, se recibio '$valor")
        return null
    }

    private fun suma(izq: Any, der: Any): Any? {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer != null) return numIzq + numDer
        if (izq is String || der is String) {
            return convertidor.valorAString(izq) + convertidor.valorAString(der)
        }

        errores.add("no se puede sumar '$izq' + '$der'")
        return null
    }

    private fun aritmetica(izq: Any, der: Any, op: (Double, Double) -> Double): Any? {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer != null) return op(numIzq, numDer)
        errores.add("operacion aritmetica invalida entre '$izq' y '$der'" )
        return null
    }

    private fun division(izq: Any, der: Any): Any? {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer != null) {
            if (numDer == 0.0) {
                errores.add("Divison entre cero")
                return null
            }
            return  numIzq / numDer
        }
        errores.add("Division invalida entre '$izq' y '$der'")
        return null
    }

    private fun modulo(izq: Any, der: Any): Any? {
        val numIzq = convertidor.aDouble(izq)
        val numDer = convertidor.aDouble(der)

        if (numIzq != null && numDer!= null ) {
            if (numDer == 0.0) {
                errores.add("modulo entre cero")
                return null
            }
            return numIzq % numDer
        }
        errores.add("Nodulo invalido entre '$izq' y '$der'")
        return null
    }
}