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

        try {
            val lexer = Lexer(StringReader(entrada))

            // recolectar errores lexicos primero
            val parser = Parser(lexer)

            parser.parse()

            erroresLexicos.addAll(lexer.getErrores())
            erroresSintacticos.addAll(parser.getErroresSintacticos())

        } catch (e: Exception) {
            // solo agregar error generico si no hay nada registrado
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
        }
    }

    fun getErroresLexicos(): List<ErrorLexico> = erroresLexicos.toList()
    fun getErroresSintacticos(): List<ErrorSintactico> = erroresSintacticos.toList()

    fun tieneErroresLexicos(): Boolean = erroresLexicos.isNotEmpty()
    fun tieneErroresSintacticos(): Boolean = erroresSintacticos.isNotEmpty()
}