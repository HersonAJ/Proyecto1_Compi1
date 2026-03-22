package com.example.proyecto1_compi1.Logic

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

object ColoreadorCodigo {

    // Colores
    private val COLOR_PALABRA_RESERVADA = Color(0xFFBB86FC)  // Morado
    private val COLOR_NUMERO = Color(0xFF80D8FF)              // Celeste
    private val COLOR_STRING = Color(0xFFFFB74D)              // Naranja
    private val COLOR_OPERADOR = Color(0xFF69F0AE)            // Verde
    private val COLOR_DELIMITADOR = Color(0xFF64B5F6)         // Azul
    private val COLOR_EMOJI = Color(0xFFFFEB3B)               // Amarillo
    private val COLOR_COMENTARIO = Color(0xFF757575)          // Gris
    private val COLOR_DEFAULT = Color.White                    // Blanco

    //Patrones basados en el Lexer1
    private val PALABRAS_RESERVADAS = setOf(
        "SECTION", "TABLE", "TEXT", "LINE",
        "OPEN_QUESTION", "DROP_QUESTION", "SELECT_QUESTION", "MULTIPLE_QUESTION",
        "number", "string", "special", "NUMBER",
        "VERTICAL", "HORIZONTAL",
        "styles", "width", "height", "pointX", "pointY",
        "orientation", "elements", "options", "correct", "content", "label",
        "IF", "ELSE", "WHILE", "DO", "FOR", "in",
        "draw", "who_is_that_pokemon",
        "MONO", "SANS_SERIF", "CURSIVE",
        "LINE", "DOTTED", "DOUBLE",
        "RED", "BLUE", "GREEN", "PURPLE", "SKY", "YELLOW", "BLACK", "WHITE"
    )

    //Regex para cada tipo de token
    private val REGEX_COMENTARIO_LINEA = Regex("""\$[^\n]*""")
    private val REGEX_COMENTARIO_BLOQUE = Regex("""/\*[\s\S]*?\*/""")
    private val REGEX_STRING = Regex(""""[^"]*"""")
    private val REGEX_NUMERO = Regex("""\b[0-9]+(\.[0-9]+)?\b""")
    private val REGEX_EMOJI = Regex("""@\[[:^()|<>3\w\-]+\]""")
    private val REGEX_HEX_COLOR = Regex("""#[0-9a-fA-F]{6}""")
    private val REGEX_HSL_COLOR = Regex("""<\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*\d{1,3}\s*>""")
    private val REGEX_OPERADOR = Regex("""[+\-*/%^~]|&&|\|\||!!|==|>=|<=|>|<""")
    private val REGEX_DELIMITADOR = Regex("""[{}\[\]()]""")
    private val REGEX_ESTILO_PROP = Regex(""""(?:color|background color|font family|text size|border)"""")
    private val REGEX_ID = Regex("""\b[a-zA-Z_][a-zA-Z0-9_]*\b""")

    /**
     * Recibe el texto del editor y retorna un AnnotatedString
     * con los colores aplicados a cada token.
     */
    fun colorear(texto: String): AnnotatedString {
        // Lista de rangos ya coloreados (para no pintar encima)
        val rangosOcupados = mutableListOf<IntRange>()

        return buildAnnotatedString {
            // Texto base en blanco
            append(texto)
            addStyle(SpanStyle(color = COLOR_DEFAULT), 0, texto.length)

            // Orden de prioridad: primero los que no deben ser sobreescritos

            // 1. Comentarios de bloque /* ... */
            aplicarPatron(texto, REGEX_COMENTARIO_BLOQUE, COLOR_COMENTARIO, rangosOcupados)

            // 2. Comentarios de línea $ ...
            aplicarPatron(texto, REGEX_COMENTARIO_LINEA, COLOR_COMENTARIO, rangosOcupados)

            // 3. Strings "..."
            aplicarPatron(texto, REGEX_STRING, COLOR_STRING, rangosOcupados)

            // 4. Propiedades de estilo entre comillas "color", "border", etc.
            aplicarPatron(texto, REGEX_ESTILO_PROP, COLOR_PALABRA_RESERVADA, rangosOcupados)

            // 5. Emojis @[:smile:] (dentro de strings pero se pintan amarillo)
            aplicarPatronSobreStrings(texto, REGEX_EMOJI, COLOR_EMOJI)

            // 6. Colores HEX #FFFFFF
            aplicarPatron(texto, REGEX_HEX_COLOR, COLOR_NUMERO, rangosOcupados)

            // 7. Colores HSL <180, 50, 50>
            aplicarPatron(texto, REGEX_HSL_COLOR, COLOR_NUMERO, rangosOcupados)

            // 8. Números
            aplicarPatron(texto, REGEX_NUMERO, COLOR_NUMERO, rangosOcupados)

            // 9. Operadores
            aplicarPatron(texto, REGEX_OPERADOR, COLOR_OPERADOR, rangosOcupados)

            // 10. Delimitadores { } [ ] ( )
            aplicarPatron(texto, REGEX_DELIMITADOR, COLOR_DELIMITADOR, rangosOcupados)

            // 11. Palabras reservadas e identificadores
            REGEX_ID.findAll(texto).forEach { match ->
                if (!estaOcupado(match.range, rangosOcupados)) {
                    val color = if (match.value in PALABRAS_RESERVADAS) {
                        COLOR_PALABRA_RESERVADA
                    } else {
                        COLOR_DEFAULT // Variables en blanco
                    }
                    addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
                    rangosOcupados.add(match.range)
                }
            }
        }
    }

    /**
     * Aplica un color a todas las coincidencias de un patron,
     * solo si no estan dentro de un rango ya coloreado.
     */
    private fun AnnotatedString.Builder.aplicarPatron(
        texto: String,
        patron: Regex,
        color: Color,
        rangosOcupados: MutableList<IntRange>
    ) {
        patron.findAll(texto).forEach { match ->
            if (!estaOcupado(match.range, rangosOcupados)) {
                addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
                rangosOcupados.add(match.range)
            }
        }
    }

    /**
     * Aplica un color dentro de strings (para emojis).
     * No respeta rangos ocupados porque pinta encima del naranja del string.
     */
    private fun AnnotatedString.Builder.aplicarPatronSobreStrings(
        texto: String,
        patron: Regex,
        color: Color
    ) {
        patron.findAll(texto).forEach { match ->
            addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
        }
    }

    /**
     * Verifica si un rango ya fue coloreado.
     */
    private fun estaOcupado(rango: IntRange, ocupados: List<IntRange>): Boolean {
        return ocupados.any { it.first <= rango.first && it.last >= rango.last }
    }
}
