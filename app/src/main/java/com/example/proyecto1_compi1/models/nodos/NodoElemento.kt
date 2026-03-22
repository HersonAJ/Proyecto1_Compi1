package com.example.proyecto1_compi1.models.nodos


// Nodo raiz del programa completo
data class NodoPrograma(
    val items: List<Any>
)

sealed class NodoElemento

// SECTION [ ... ]
data class NodoSeccion(
    val atributos: Map<String, Any>,
    val elementos: List<NodoElemento>,
    val estilos: NodoEstilos?
) : NodoElemento()

// TABLE [ ... ]
data class NodoTabla(
    val atributos: Map<String, Any>,
    val filas: List<List<NodoElemento>>,
    val estilos: NodoEstilos?
) : NodoElemento()

// TEXT [ ... ]
data class NodoTexto(
    val contenido: NodoExpresion,
    val ancho: NodoExpresion?,
    val alto: NodoExpresion?,
    val estilos: NodoEstilos?
) : NodoElemento()

// OPEN_QUESTION [ ... ]
data class NodoPreguntaAbierta(
    val label: NodoExpresion,
    val ancho: NodoExpresion?,
    val alto: NodoExpresion?,
    val estilos: NodoEstilos?
) : NodoElemento()

// DROP_QUESTION [ ... ]
data class NodoPreguntaDesplegable(
    val label: NodoExpresion,
    val ancho: NodoExpresion?,
    val alto: NodoExpresion?,
    val opciones: List<NodoExpresion>,
    val correcto: NodoExpresion?,
    val estilos: NodoEstilos?
) : NodoElemento()

// SELECT_QUESTION [ ... ]
data class NodoPreguntaSeleccion(
    val ancho: NodoExpresion?,
    val alto: NodoExpresion?,
    val opciones: List<NodoExpresion>,
    val correcto: NodoExpresion?,
    val estilos: NodoEstilos?
) : NodoElemento()

// MULTIPLE_QUESTION [ ... ]
data class NodoPreguntaMultiple(
    val ancho: NodoExpresion?,
    val alto: NodoExpresion?,
    val opciones: List<NodoExpresion>,
    val correctos: List<NodoExpresion>,
    val estilos: NodoEstilos?
) : NodoElemento()

// Estilos
data class NodoEstilos(
    val color: NodoColor?,
    val backgroundColor: NodoColor?,
    val fontFamily: String?,
    val textSize: NodoExpresion?,
    val borde: NodoBorde?
)

// Color en cualquier formato
sealed class NodoColor
data class NodoColorHex(val valor: String) : NodoColor()
data class NodoColorRGB(val r: NodoExpresion, val g: NodoExpresion, val b: NodoExpresion) : NodoColor()
data class NodoColorHSL(val valor: String) : NodoColor()
data class NodoColorNombre(val nombre: String) : NodoColor()

// Border
data class NodoBorde(
    val grosor: NodoExpresion,
    val tipo: String,
    val color: NodoColor
)