package com.example.proyecto1_compi1.Logic.traduccion

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GeneradorMetadatos {

    fun generar(
        genElementos: GeneradorElementos,
        autor: String = "Herson Aguilar"
    ): String {
        val fechaFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val horaFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val ahora = Date()

        val totalPreguntas = genElementos.totalAbiertas +
                genElementos.totalDesplegables +
                genElementos.totalSeleccion +
                genElementos.totalMultiples

        val sb = StringBuilder()
        sb.append("###\n")
        sb.append("Author: $autor\n")
        sb.append("Fecha: ${fechaFormat.format(ahora)}\n")
        sb.append("Hora: ${horaFormat.format(ahora)}\n")
        sb.append("Description: Formulario generado\n")
        sb.append("Total de Secciones: ${genElementos.totalSecciones}\n")
        sb.append("Total de Preguntas: $totalPreguntas\n")
        sb.append("Abiertas: ${genElementos.totalAbiertas}\n")
        sb.append("Desplegables: ${genElementos.totalDesplegables}\n")
        sb.append("Selección: ${genElementos.totalSeleccion}\n")
        sb.append("Múltiples: ${genElementos.totalMultiples}\n")
        sb.append("###\n")
        return sb.toString()
    }
}