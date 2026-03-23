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
//analisis semantico para el rgb y hsl fuera del rango ( o a 255)
    fun resolverColor(color: NodoColor): String {
        return when (color) {
            is NodoColorHex -> color.valor
            is NodoColorHSL -> {
                validarHSL(color.valor)
                color.valor
            }
            is NodoColorRGB -> {
                val r = evalNum(color.r)
                val g = evalNum(color.g)
                val b = evalNum(color.b)
                validarRGB(r, g, b)
                "($r,$g,$b)"
            }
            is NodoColorNombre -> color.nombre
        }
    }

    private fun validarRGB(r: String, g: String, b: String) {
        val rv = r.toDoubleOrNull() ?: return
        val gv = g.toDoubleOrNull() ?: return
        val bv = b.toDoubleOrNull() ?: return
        if (rv < 0 || rv > 255) errores.add("RGB: valor R=$r fuera de rango (0-255).")
        if (gv < 0 || gv > 255) errores.add("RGB: valor G=$g fuera de rango (0-255).")
        if (bv < 0 || bv > 255) errores.add("RGB: valor B=$b fuera de rango (0-255).")
    }

    private fun validarHSL(valor: String) {
        val limpio = valor.removePrefix("<").removeSuffix(">").trim()
        val partes = limpio.split(",").mapNotNull { it.trim().toDoubleOrNull() }
        if (partes.size == 3) {
            if (partes[0] < 0 || partes[0] > 360) errores.add("HSL: valor H=${partes[0]} fuera de rango (0-360).")
            if (partes[1] < 0 || partes[1] > 100) errores.add("HSL: valor S=${partes[1]} fuera de rango (0-100).")
            if (partes[2] < 0 || partes[2] > 100) errores.add("HSL: valor L=${partes[2]} fuera de rango (0-100).")
        }
    }

    private fun evalNum(expr: NodoExpresion): String {
        val valor = evaluador.evaluar(expr)
        return evaluador.convertidor.valorAString(valor)
    }
}