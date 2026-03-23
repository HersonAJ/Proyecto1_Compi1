package com.example.proyecto1_compi1.Logic.traduccion

import com.example.proyecto1_compi1.Logic.ServicioPokeApi
import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.models.nodos.*

class GeneradorElementos(
    private val evaluador: EvaluadorExpresiones,
    private val genEstilos: GeneradorEstilos,
    private val errores: MutableList<String>,
    private val servicioPokeApi: ServicioPokeApi = ServicioPokeApi()
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
        if (!validarDimensiones(sec.atributos, "SECTION")) {
            return ""  // no genera nada
        }
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
        if (!validarDimensiones(tabla.atributos, "SECTION")) {
            return "" // no genera nada
        }
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
        if (!validarDimensionDirecta(texto.ancho, "width", "TEXT") ||
            !validarDimensionDirecta(texto.alto, "height", "TEXT")) {
            return ""
        }
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
        if (!validarDimensionDirecta(p.ancho, "width", "OPEN_QUESTION") ||
            !validarDimensionDirecta(p.alto, "height", "OPEN_QUESTION")) {
            return ""
        }
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
        if (!validarDimensionDirecta(p.ancho, "width", "DROP_QUESTION") ||
            !validarDimensionDirecta(p.alto, "height", "DROP_QUESTION")) {
            return ""
        }
        totalDesplegables++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val label = evalStr(p.label)
        val correct = if (p.correcto != null) validarCorrect(evalExpr(p.correcto)) ?: "-1" else "-1"

        // Resolver opciones: pokemon o normales
        val opts = if (p.pokemonDesde != null && p.pokemonHasta != null) {
            // Consultar PokeAPI
            val desde = (evaluador.evaluar(p.pokemonDesde) as? Double)?.toInt() ?: 1
            val hasta = (evaluador.evaluar(p.pokemonHasta) as? Double)?.toInt() ?: 10
            val nombres = servicioPokeApi.obtenerPokemones(desde, hasta)
            errores.addAll(servicioPokeApi.getErrores())
            servicioPokeApi.limpiarErrores()
            // Formatear como lista de strings
            "{${nombres.joinToString(",") { "\"$it\"" }}}"
        } else {
            formatOpciones(p.opciones)
        }

        return if (p.estilos != null) {
            "<drop=$w,$h,\"$label\",$opts,$correct>\n${genEstilos.generar(p.estilos)}</drop>\n"
        } else {
            "<drop=$w,$h,\"$label\",$opts,$correct/>\n"
        }
    }

    private fun generarSeleccion(p: NodoPreguntaSeleccion): String {
        if (!validarDimensionDirecta(p.ancho, "width", "SELECT_QUESTION") ||
            !validarDimensionDirecta(p.alto, "height", "SELECT_QUESTION")) {
            return ""
        }
        totalSeleccion++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val label = evalStr(p.label)
        val correct = if (p.correcto != null) validarCorrect(evalExpr(p.correcto)) ?: "-1" else "-1"

        // Resolver opciones: pokemon o normales
        val opts = if (p.pokemonDesde != null && p.pokemonHasta != null) {
            val desde = (evaluador.evaluar(p.pokemonDesde) as? Double)?.toInt() ?: 1
            val hasta = (evaluador.evaluar(p.pokemonHasta) as? Double)?.toInt() ?: 10
            val nombres = servicioPokeApi.obtenerPokemones(desde, hasta)
            errores.addAll(servicioPokeApi.getErrores())
            servicioPokeApi.limpiarErrores()
            "{${nombres.joinToString(",") { "\"$it\"" }}}"
        } else {
            formatOpciones(p.opciones)
        }

        return if (p.estilos != null) {
            "<select=$w,$h,\"$label\",$opts,$correct>\n${genEstilos.generar(p.estilos)}</select>\n"
        } else {
            "<select=$w,$h,\"$label\",$opts,$correct/>\n"
        }
    }

    private fun generarMultiple(p: NodoPreguntaMultiple): String {
        if (!validarDimensionDirecta(p.ancho, "width", "MULTIPLE_QUESTION") ||
            !validarDimensionDirecta(p.alto, "height", "MULTIPLE_QUESTION")) {
            return ""
        }
        totalMultiples++
        val w = optNum(p.ancho)
        val h = optNum(p.alto)
        val corrects = formatCorrectos(p.correctos)

        // Resolver opciones: pokemon o normales
        val opts = if (p.pokemonDesde != null && p.pokemonHasta != null) {
            val desde = (evaluador.evaluar(p.pokemonDesde) as? Double)?.toInt() ?: 1
            val hasta = (evaluador.evaluar(p.pokemonHasta) as? Double)?.toInt() ?: 10
            val nombres = servicioPokeApi.obtenerPokemones(desde, hasta)
            errores.addAll(servicioPokeApi.getErrores())
            servicioPokeApi.limpiarErrores()
            "{${nombres.joinToString(",") { "\"$it\"" }}}"
        } else {
            formatOpciones(p.opciones)
        }

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
        val items = correctos.mapNotNull { validarCorrect(evalExpr(it)) }
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

    //analis semantico evalua que el valor de correct sea entero o .0
    private fun validarCorrect(valor: String): String? {
        val num = valor.toDoubleOrNull() ?: return valor
        if (num == num.toLong().toDouble()) {
            // 1.0, 2.0 -> truncar a 1, 2
            return num.toLong().toString()
        } else {
            // 1.5, 2.3 -> error, no se acepta
            errores.add("El indice de respuesta correcta debe ser entero, se recibio $valor.")
            return null
        }
    }

    // analisis semantico Valida que width, height, pointX, pointY no sean negativos. Retorna true si son validos, false si hay error.
    private fun validarDimensiones(atributos: Map<String, Any>, contexto: String): Boolean {
        var valido = true
        val campos = listOf("width", "height", "pointX", "pointY")

        for (campo in campos) {
            val valor = atributos[campo]
            if (valor is NodoExpresion) {
                val resultado = evaluador.evaluar(valor)
                if (resultado is Double && resultado < 0) {
                    errores.add("$contexto: el valor de '$campo' no puede ser negativo ($resultado).")
                    valido = false
                }
            }
        }
        return valido
    }

    private fun validarDimensionDirecta(valor: NodoExpresion?, campo: String, contexto: String): Boolean {
        if (valor == null) return true
        val resultado = evaluador.evaluar(valor)
        if (resultado is Double && resultado < 0) {
            errores.add("$contexto: el valor de '$campo' no puede ser negativo ($resultado).")
            return false
        }
        return true
    }
}
