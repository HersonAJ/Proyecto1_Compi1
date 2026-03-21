package com.example.proyecto1_compi1.Logic.traduccion

import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.models.nodos.*

class GeneradorElementos(
    private val evaluador: EvaluadorExpresiones,
    private val genEstilos: GeneradorEstilos,
    private val errores: MutableList<String>
) {
    // Contadores para los metadatos
    var totalSecciones = 0; private set
    var totalAbiertas = 0; private set
    var totalDesplegables = 0; private set
    var totalSeleccion = 0; private set
    var totalMultiples = 0; private set

    fun generar(elemento: NodoElemento): String {
        return when (elemento) {
            is NodoSeccion -> generarSeccion(elemento)
            is NodoTabla -> generarTabla(elemento)
            is NodoTexto -> generarTexto(elemento)
            is NodoPreguntaAbierta -> generarAbierta(elemento)
            is NodoPreguntaDesplegable -> generarDesplegable(elemento)
            is NodoPreguntaSeleccion -> generarSeleccion(elemento)
            is NodoPreguntaMultiple -> generarMultiple(elemento)
        }
    }
    private fun generarSeccion(sec: NodoSeccion): String {
        totalSecciones++
        val w = evalAttr(sec.atributos["width"])
        val h = evalAttr(sec.atributos["height"])
        val px = evalAttr(sec.atributos["pointX"])
        val py = evalAttr(sec.atributos["pointY"])
        val ori = sec.atributos["orientation"] as? String ?: "VERTICAL"

        val sb = StringBuilder()
        sb.append("<section=$w,$h,$px,$py,$ori>\n")

        if (sec.estilos != null) {
            sb.append(genEstilos.generar(sec.estilos))
        }

        if (sec.elementos.isNotEmpty()) {
            sb.append("<content>\n")
            for (elem in sec.elementos) {
                sb.append(generar(elem))
            }
            sb.append("</content>\n")
        }

        sb.append("</section>\n")
        return sb.toString()
    }
    private fun generarTabla(tabla: NodoTabla): String {
        val w = evalAttr(tabla.atributos["width"])
        val h = evalAttr(tabla.atributos["height"])
        val sb = StringBuilder()
        sb.append("<table=$w,$h>\n")

        if (tabla.estilos != null) {
            sb.append(genEstilos.generar(tabla.estilos))
        }

        sb.append("<content>\n")
        for (fila in tabla.filas) {
            sb.append("<line>\n")
            for (celda in fila) {
                sb.append("<element>\n")
                sb.append(generar(celda))
                sb.append("</element>\n")
            }
            sb.append("</line>\n")
        }
        sb.append("</content>\n")
        sb.append("</table>\n")
        return sb.toString()
    }
    private fun generarTexto(texto: NodoTexto): String {
        val w = optNum(texto.ancho)
        val h = optNum(texto.alto)
        val content = evalStr(texto.contenido)

        return if (texto.estilos != null) {
            "<open=$w,$h,\"$content\">\n${genEstilos.generar(texto.estilos)}</open>\n"
        } else {
            "<open=$w,$h,\"$content\"/>\n"
        }
    }

    private fun generarAbierta(p: NodoPreguntaAbierta): String {
        totalAbiertas++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val label = evalStr(p.label)

        return if (p.estilos != null) {
            "<open=$w,$h,\"$label\">\n${genEstilos.generar(p.estilos)}</open>\n"
        } else {
            "<open=$w,$h,\"$label\"/>\n"
        }
    }

    private fun generarDesplegable(p: NodoPreguntaDesplegable): String {
        totalDesplegables++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val label = evalStr(p.label)
        val opts = formatOpciones(p.opciones)
        val correct = if (p.correcto != null) evalExpr(p.correcto) else "-1"

        return if (p.estilos != null) {
            "<drop=$w,$h,\"$label\",$opts,$correct>\n${genEstilos.generar(p.estilos)}</drop>\n"
        } else {
            "<drop=$w,$h,\"$label\",$opts,$correct/>\n"
        }
    }

    private fun generarSeleccion(p: NodoPreguntaSeleccion): String {
        totalSeleccion++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val opts = formatOpciones(p.opciones)
        val correct = if (p.correcto != null) evalExpr(p.correcto) else "-1"

        return if (p.estilos != null) {
            "<select=$w,$h,$opts,$correct>\n${genEstilos.generar(p.estilos)}</select>\n"
        } else {
            "<select=$w,$h,$opts,$correct/>\n"
        }
    }

    private fun generarMultiple(p: NodoPreguntaMultiple): String {
        totalMultiples++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val opts = formatOpciones(p.opciones)
        val corrects = formatCorrectos(p.correctos)

        return if (p.estilos != null) {
            "<multiple=$w,$h,$opts,$corrects>\n${genEstilos.generar(p.estilos)}</multiple>\n"
        } else {
            "<multiple=$w,$h,$opts,$corrects/>\n"
        }
    }

    private fun formatOpciones(opciones: List<NodoExpresion>?): String {
        if (opciones.isNullOrEmpty()) return "{}"
        val items = opciones.map { "\"${evalStr(it)}\"" }
        return "{${items.joinToString(",")}}"
    }

    private fun formatCorrectos(correctos: List<NodoExpresion>?): String {
        if (correctos.isNullOrEmpty()) return "{}"
        val items = correctos.map { evalExpr(it) }
        return "{${items.joinToString(",")}}"
    }

    private fun evalAttr(attr: Any?): String {
        if (attr is NodoExpresion) return evalExpr(attr)
        return attr?.toString() ?: "0"
    }

    private fun evalExpr(expr: NodoExpresion): String {
        val valor = evaluador.evaluar(expr)
        return evaluador.convertidor.valorAString(valor)
    }

    private fun evalStr(expr: NodoExpresion?): String {
        if (expr == null) return ""
        val valor = evaluador.evaluar(expr)
        return valor?.toString() ?: ""
    }

    private fun optNum(expr: NodoExpresion?): String {
        if (expr == null) return "0"
        return evalExpr(expr)
    }
}
