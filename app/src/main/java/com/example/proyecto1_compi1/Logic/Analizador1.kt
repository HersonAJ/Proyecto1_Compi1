package com.example.proyecto1_compi1.Logic

import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.Analizadores.Parser
import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.Logic.traduccion.GeneradorCodigo
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.TablaSimbolos
import com.example.proyecto1_compi1.models.nodos.NodoPrograma
import java.io.StringReader

class Analizador1 {

    private val erroresLexicos = mutableListOf<ErrorLexico>()
    private val erroresSintacticos = mutableListOf<ErrorSintactico>()
    private val erroresSemanticos = mutableListOf<String>()

    //enalizar el primer lenguaje
    fun analizar(entrada: String): String? {
        erroresLexicos.clear()
        erroresSintacticos.clear()
        erroresSemanticos.clear()

        var lexer: Lexer? = null
        var parser: Parser? = null
        var ast: NodoPrograma? = null

        //paso 1 Lexico + Sintactico
        try {
            lexer = Lexer(StringReader(entrada))
            @Suppress("DEPRECATION")
            parser = Parser(lexer)
            val resultado = parser.parse()
            ast = resultado.value as? NodoPrograma

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

        // Si no se pudo construir el ast entonces se detiene
        if (ast == null) return null

        //paso 2: semantico + Generacion de codigo .pkm
        val tabla = TablaSimbolos()
        val evaluador = EvaluadorExpresiones(tabla)
        val generador = GeneradorCodigo(tabla, evaluador)

        val codigoPkm = generador.generar(ast)

        // Recolectar errores semanticos
        erroresSemanticos.addAll(generador.getErrores())

        return codigoPkm
    }

    // getters
    fun getErroresLexicos(): List<ErrorLexico> = erroresLexicos.toList()
    fun getErroresSintacticos(): List<ErrorSintactico> = erroresSintacticos.toList()
    fun getErroresSemanticos(): List<String> = erroresSemanticos.toList()

    fun tieneErroresLexicos(): Boolean = erroresLexicos.isNotEmpty()
    fun tieneErroresSintacticos(): Boolean = erroresSintacticos.isNotEmpty()
    fun tieneErroresSemanticos(): Boolean = erroresSemanticos.isNotEmpty()
    fun tieneErrores(): Boolean = tieneErroresLexicos() || tieneErroresSintacticos() || tieneErroresSemanticos()
}