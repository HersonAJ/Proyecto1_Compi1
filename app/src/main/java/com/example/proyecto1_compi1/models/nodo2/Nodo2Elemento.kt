package com.example.proyecto1_compi1.models.nodo2

sealed class Nodo2Elemento

// <section=w,h,px,py,ori> ... </section>
data class Nodo2Seccion(
    val width: Double,
    val height: Double,
    val pointX: Double,
    val pointY: Double,
    val orientacion: String,
    val estilos: Nodo2Estilos?,
    val contenido: List<Nodo2Elemento>
) : Nodo2Elemento()

// <table> ... </table>
data class Nodo2Tabla(
    val estilos: Nodo2Estilos?,
    val filas: List<List<Nodo2Elemento>>
) : Nodo2Elemento()

// <open=w,h,"content"/> — sirve para TEXT y OPEN_QUESTION
data class Nodo2Open(
    val width: Double,
    val height: Double,
    val contenido: String,
    val estilos: Nodo2Estilos?
) : Nodo2Elemento()

// <drop=w,h,"label",{opciones},correct/>
data class Nodo2Drop(
    val width: Double,
    val height: Double,
    val label: String,
    val opciones: List<String>,
    val correcto: Double,
    val estilos: Nodo2Estilos?
) : Nodo2Elemento()

// <select=w,h,{opciones},correct/>
data class Nodo2Select(
    val width: Double,
    val height: Double,
    val opciones: List<String>,
    val correcto: Double,
    val estilos: Nodo2Estilos?
) : Nodo2Elemento()

// <multiple=w,h,{opciones},{correctos}/>
data class Nodo2Multiple(
    val width: Double,
    val height: Double,
    val opciones: List<String>,
    val correctos: List<Double>,
    val estilos: Nodo2Estilos?
) : Nodo2Elemento()
