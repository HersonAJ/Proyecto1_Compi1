package com.example.proyecto1_compi1.Logic

import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.Analizadores.Parser
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.Token
import java.io.StringReader
import java_cup.runtime.Symbol

class Analizador1 {

    private val erroresLexicos = mutableListOf<ErrorLexico>()
    private val erroresSintacticos = mutableListOf<ErrorSintactico>()

    fun analizar(entrada: String): List<Token> {
        val tokens = mutableListOf<Token>()
        erroresLexicos.clear()
        erroresSintacticos.clear()

        try {
            val lexer = Lexer(StringReader(entrada))
            lexer.errores.clear()

            val parser = Parser(lexer)
            val result: Symbol = parser.parse()

            // recolectar errores lexicos
            erroresLexicos.addAll(lexer.errores)

            // recolectar errores sintacticos
            erroresSintacticos.addAll(parser.getErroresSintacticos())
        } catch (e: Exception) {
            erroresSintacticos.add(
                ErrorSintactico(
                    linea = 0,
                    columna = 0,
                    descripcion = "Error inesperado: ${e.message}"
                )
            )
        }
        return tokens
    }

    fun getErroresLexicos(): List<ErrorLexico> = erroresLexicos.toList()
    fun getErroresSintacticos(): List<ErrorSintactico> = erroresSintacticos.toList()

    fun tieneErroresLexicos(): Boolean = erroresLexicos.isNotEmpty()
    fun tieneErroresSintacticos(): Boolean = erroresSintacticos.isNotEmpty()
}