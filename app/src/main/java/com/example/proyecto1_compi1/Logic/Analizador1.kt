package com.example.proyecto1_compi1.Logic

import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.Analizadores.sym
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.Token
import java.io.StringReader
import java_cup.runtime.Symbol

class Analizador1 {

    private val errores = mutableListOf<ErrorLexico>()

    fun analizar(entrada: String): List<Token> {
        val tokens = mutableListOf<Token>()
        errores.clear()

        try {
            val lexer = Lexer(StringReader(entrada))
            lexer.errores.clear()

            var symbol: Symbol? = lexer.next_token()
            while (symbol != null) {
                if (symbol.sym == sym.EOF) break
                val token = Token(
                    lexema = symbol.value?.toString() ?: "",
                    tipo = sym.terminalNames[symbol.sym],
                    linea = symbol.left,
                    columna = symbol.right
                )
                tokens.add(token)
                symbol = lexer.next_token()
            }

            errores.addAll(lexer.errores)
        } catch (e: Exception) {
            errores.add(
                ErrorLexico(
                    lexema = "",
                    linea = 0,
                    columna = 0,
                    tipo = "lexico no reconocido",
                    descripcion = "Error inesperado: ${e.message}"
                )
            )
        }
        return tokens
    }

    fun getErrores(): List<ErrorLexico> = errores.toList()

    fun tieneErrores(): Boolean = errores.isNotEmpty()
}