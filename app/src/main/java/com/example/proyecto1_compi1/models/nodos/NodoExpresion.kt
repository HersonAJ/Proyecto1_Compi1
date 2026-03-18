package com.example.proyecto1_compi1.models.nodos

sealed class NodoExpresion

//literales
data class NodoNumero(val valor: Double) : NodoExpresion()
data class NodoCadena(val valor: String) : NodoExpresion()
data class NodoIdentificador(val valor: String) : NodoExpresion()
data object NodoComodin : NodoExpresion()

//operaciones
data class NodoOperacionBinaria(
    val izquierda: NodoExpresion,
    val operador: String,
    val derecha: NodoExpresion
) : NodoExpresion()

data class NodoOperacionUnaria(
    val operador: String,
    val expresion: NodoExpresion
) : NodoExpresion()