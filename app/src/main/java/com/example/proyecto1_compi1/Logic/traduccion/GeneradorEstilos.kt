package com.example.proyecto1_compi1.Logic.traduccion

import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.models.nodos.NodoColor
import com.example.proyecto1_compi1.models.nodos.NodoColorHSL
import com.example.proyecto1_compi1.models.nodos.NodoColorHex
import com.example.proyecto1_compi1.models.nodos.NodoColorNombre
import com.example.proyecto1_compi1.models.nodos.NodoColorRGB
import com.example.proyecto1_compi1.models.nodos.NodoEstilos
import com.example.proyecto1_compi1.models.nodos.NodoExpresion

class GeneradorEstilos (
    private val evaluador: EvaluadorExpresiones,
    private val errores: MutableList<String>
){

    fun generar(estilos: NodoEstilos?): String {
        if (estilos == null) return ""
        val sb = StringBuilder()
        sb.append("<style>\n")

        estilos.color?.let {
            sb.append("<color=${resolverColor(it)}/>\n")
        }
        estilos.backgroundColor?.let {
            sb.append("<background color=${resolverColor(it)}/>\n")
        }
        estilos.fontFamily?.let {
            sb.append("<font family=$it/>\n")
        }
        estilos.textSize?.let {
            sb.append("<text size=${evalNum(it)}/>\n")
        }
        estilos.borde?.let {
            val grosor = evalNum(it.grosor)
            val color = resolverColor(it.color)
            sb.append("<border,${grosor},${it.tipo},color=$color/>\n")
        }

        sb.append("</style>\n")
        return sb.toString()
    }

    fun resolverColor(color: NodoColor): String {
        return when (color) {
            is NodoColorHex -> color.valor
            is NodoColorHSL -> color.valor
            is NodoColorRGB -> {
                "(${evalNum(color.r)},${evalNum(color.g)},${evalNum(color.b)})"
            }
            is NodoColorNombre -> color.nombre
        }
    }

    private fun evalNum(expr: NodoExpresion): String {
        val valor = evaluador.evaluar(expr)
        return evaluador.convertidor.valorAString(valor)
    }
}