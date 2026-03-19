package com.example.proyecto1_compi1.Logic.evaluacion

import com.example.proyecto1_compi1.models.TablaSimbolos
import com.example.proyecto1_compi1.models.nodos.NodoCadena
import com.example.proyecto1_compi1.models.nodos.NodoComodin
import com.example.proyecto1_compi1.models.nodos.NodoExpresion
import com.example.proyecto1_compi1.models.nodos.NodoIdentificador
import com.example.proyecto1_compi1.models.nodos.NodoNumero
import com.example.proyecto1_compi1.models.nodos.NodoOperacionBinaria
import com.example.proyecto1_compi1.models.nodos.NodoOperacionUnaria

class EvaluadorExpresiones(
    private val tabla: TablaSimbolos
) {

    private val errores = mutableListOf<String>()
    val convertidor =  ConvertidorTipos()

    private val aritmeticas = OperacionesAritmeticas(convertidor, errores)
    private val relacionales = OperacionesRelacionales(convertidor, errores)
    private val logicas = OperacionesLogicas(convertidor, errores)

    //remplazo para los comodines en el metodo draw
    private var argsComodin: List<Any?> = emptyList()
    private var indiceComodin = 0

    fun getErrores(): List<String> = errores.toList()
    fun limpiarErrores() { errores.clear() }

    fun setArgsComodin(args: List<Any?>) {
        argsComodin = args
        indiceComodin = 0
    }

    fun resetComodines() {
        argsComodin = emptyList()
        indiceComodin = 0
    }

    fun evaluar (nodo: NodoExpresion): Any? {
        return when (nodo) {
            is NodoNumero -> nodo.valor
            is NodoCadena -> nodo.valor
            is NodoIdentificador -> evaluarIdentificador(nodo)
            is NodoComodin -> evaluarComodin()
            is NodoOperacionBinaria -> evaluarBinaria(nodo)
            is NodoOperacionUnaria -> evaluarUnaria(nodo)
        }
    }

    private fun evaluarIdentificador(nodo: NodoIdentificador): Any? {
        if (!tabla.existe((nodo.valor))) {
            errores.add("Variable '${nodo.valor}' no ha sido declarada")
            return null
        }
        return tabla.obtener(nodo.valor)
    }

    private fun evaluarComodin(): Any? {
        if (indiceComodin >= argsComodin.size) {
            errores.add("No hay suficientes elementos para remplazar el comodin '?' ")
            return null
        }
        val valor = argsComodin[indiceComodin]
        indiceComodin++
        return valor
    }

    private fun evaluarBinaria(nodo: NodoOperacionBinaria): Any? {
        val izq = evaluar(nodo.izquierda) ?: return null
        val der = evaluar(nodo.derecha) ?: return null

        return when (nodo.operador) {
            "+", "-", "*", "/", "^", "%" -> aritmeticas.evaluar(nodo.operador, izq, der)
            ">", "<", ">=", "<=", "==", "!!" -> relacionales.evaluar(nodo.operador, izq, der)
            "&&", "||" -> logicas.evaluar(nodo.operador, izq, der )
            else -> {
                errores.add("operador desconocido: '${nodo.operador}'")
                null
            }
        }
    }

    private fun evaluarUnaria(nodo: NodoOperacionUnaria): Any? {
        val valor = evaluar(nodo.expresion) ?: return null

        return when (nodo.operador) {
            "-" -> aritmeticas.negarNumero(valor)
            "+" -> aritmeticas.positivo(valor)
            "~" -> logicas.negar(valor)
            else -> {
                errores.add("Operador unario desconocido: '${nodo.operador}'")
                null
            }
        }
    }
}