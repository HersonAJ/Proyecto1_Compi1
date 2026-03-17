package com.example.proyecto1_compi1.Logic

import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.Analizadores.Parser
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import java.io.StringReader

class Analizador1 {

    private val erroresLexicos = mutableListOf<ErrorLexico>()
    private val erroresSintacticos = mutableListOf<ErrorSintactico>()

    fun analizar(entrada: String) {
        erroresLexicos.clear()
        erroresSintacticos.clear()

        var lexer: Lexer? = null
        var parser: Parser? = null

        try {
            lexer = Lexer(StringReader(entrada))
            @Suppress("DEPRECATION")
            parser = Parser(lexer)
            parser.parse()

        } catch (e: Exception) {
            if (erroresLexicos.isEmpty() && erroresSintacticos.isEmpty()) {
                erroresSintacticos.add(
                    ErrorSintactico(
                        lexema = "",
                        linea = 0,
                        columna = 0,
                        descripcion = "Error inesperado: ${e.message}"
                    )
                )
            }
        } finally {
            lexer?.let { erroresLexicos.addAll(it.getErrores()) }
            parser?.let { erroresSintacticos.addAll(it.getErroresSintacticos()) }
        }
    }
    fun getErroresLexicos(): List<ErrorLexico> = erroresLexicos.toList()
    fun getErroresSintacticos(): List<ErrorSintactico> = erroresSintacticos.toList()

    fun tieneErroresLexicos(): Boolean = erroresLexicos.isNotEmpty()
    fun tieneErroresSintacticos(): Boolean = erroresSintacticos.isNotEmpty()
}