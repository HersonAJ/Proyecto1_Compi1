package com.example.proyecto1_compi1.models.nodos

sealed class NodoInstruccion

data class NodoDeclaracion(
    val tipo: String,
    val nombre: String,
    val valor: Any?
) : NodoInstruccion()

data class NodoAsignacion(
    val nombre: String,
    val valor: NodoExpresion
) : NodoInstruccion()

data class NodoIf(
    val condicion: NodoExpresion,
    val cuerppo: List<Any>,
    val elseParte: NodoInstruccion?
) : NodoInstruccion()

data class NodoElse(
    val cuerppo: List<Any>
) : NodoInstruccion()

data class NodoWhile(
    val condicion: NodoExpresion,
    val cuerppo: List<Any>
) : NodoInstruccion()

data class NodoDoWhile(
    val cuerppo: List<Any>,
    val condicion: NodoExpresion
) : NodoInstruccion()

data class NodoFor(
    val inicializacion: NodoAsignacion,
    val condicion: NodoExpresion,
    val actualizacion: NodoAsignacion,
    val cuerpo: List<Any>
) : NodoInstruccion()

data class NodoForRango(
    val variable: String,
    val inicio: NodoExpresion,
    val fin: NodoExpresion,
    val cuerpo: List<Any>
) : NodoInstruccion()

data class NodoDraw(
    val nombre: String,
    val argumentos: List<NodoExpresion>
) : NodoInstruccion()