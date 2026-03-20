package com.example.proyecto1_compi1.models.nodo2

sealed class Nodo2Color

data class Nodo2ColorHex(val valor: String) : Nodo2Color()
data class Nodo2ColorRGB(val r: Double, val g: Double, val b: Double) : Nodo2Color()
data class Nodo2ColorHSL(val valor: String) : Nodo2Color()
data class Nodo2ColorNombre(val nombre: String) : Nodo2Color()