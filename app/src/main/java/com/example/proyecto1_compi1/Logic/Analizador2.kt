package com.example.proyecto1_compi1.Logic

import com.example.proyecto1_compi1.Analizadores.Lexer2
import com.example.proyecto1_compi1.Analizadores.Parser2
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.nodo2.Nodo2Programa
import java.io.StringReader

class Analizador2 {

    private val erroresLexicos = mutableListOf<ErrorLexico>()
    private val erroresSintacticos = mutableListOf<ErrorSintactico>()

    //analizar la entrada de .pkm y retornar el ast2

    fun analizar(entrada: String) : Nodo2Programa? {
        erroresLexicos.clear()
        erroresSintacticos.clear()

        if (entrada.isBlank()) return null

        var lexer: Lexer2? = null
        var parser: Parser2? = null
        var ast: Nodo2Programa? = null

        try {
            lexer = Lexer2(StringReader(entrada))
            @Suppress("DEPRECATION")
            parser = Parser2(lexer)
            val resultado = parser.parse()
            ast = resultado.value as? Nodo2Programa

        } catch (e: Exception) {
            if (erroresLexicos.isEmpty() && erroresLexicos.isEmpty()) {
                erroresSintacticos.add(
                    ErrorSintactico(
                        lexema = "",
                        linea = 0,
                        columna = 0,
                        descripcion = "Error al analizar .pkm ${e.message}"
                    )
                )
            }
        } finally {
            lexer?.let { erroresLexicos.addAll(it.getErrores()) }
            parser?.let { erroresSintacticos.addAll(it.getErroresSintacticos()) }
        }
        return ast
    }

    // ===== Getters de errores =====
    fun getErroresLexicos(): List<ErrorLexico> = erroresLexicos.toList()
    fun getErroresSintacticos(): List<ErrorSintactico> = erroresSintacticos.toList()

    fun tieneErroresLexicos(): Boolean = erroresLexicos.isNotEmpty()
    fun tieneErroresSintacticos(): Boolean = erroresSintacticos.isNotEmpty()
    fun tieneErrores(): Boolean = tieneErroresLexicos() || tieneErroresSintacticos()
}