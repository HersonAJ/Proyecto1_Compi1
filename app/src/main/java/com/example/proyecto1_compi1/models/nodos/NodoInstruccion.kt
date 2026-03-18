package com.example.proyecto1_compi1.models.nodos

sealed class NodoInstruccion

data class NodoDeclaracion(
    val tipo: String,
    val nombre: String,
    val valor: NodoExpresion?
) : NodoInstruccion()

data class NodoAsignacion(
    val nombre: String,
    val valor: NodoExpresion
) : NodoInstruccion()

data class NodoIf(
    val condicion: NodoExpresion,
    val cuerppo: List<NodoInstruccion>,
    val elseParte: NodoInstruccion?
) : NodoInstruccion()

data class NodoElse(
    val cuerppo: List<NodoInstruccion>
) : NodoInstruccion()

data class NodoWhile(
    val condicion: NodoExpresion,
    val cuerppo: List<NodoInstruccion>
) : NodoInstruccion()

data class NodoDoWhile(
    val cuerppo: List<NodoInstruccion>,
    val condicion: NodoExpresion
) : NodoInstruccion()

data class NodoFor(
    val inicializacion: NodoAsignacion,
    val condicion: NodoExpresion,
    val actualizacion: NodoAsignacion,
    val cuerpo: List<NodoInstruccion>
) : NodoInstruccion()

data class NodoForRango(
    val variable: String,
    val inicio: NodoExpresion,
    val fin: NodoExpresion,
    val cuerpo: List<NodoInstruccion>
) : NodoInstruccion()

data class NodoDraw(
    val nombre: String,
    val argumentos: List<NodoExpresion>
) : NodoInstruccion()