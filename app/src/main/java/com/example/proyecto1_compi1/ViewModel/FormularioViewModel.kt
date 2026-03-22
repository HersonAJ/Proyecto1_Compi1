package com.example.proyecto1_compi1.ViewModel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto1_compi1.Logic.Analizador1
import com.example.proyecto1_compi1.Logic.Analizador2
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.ErrorReporte
import com.example.proyecto1_compi1.models.nodo2.Nodo2Programa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioViewModel: ViewModel() {

    private val _codigo = MutableStateFlow(TextFieldValue(""))
    val codigo: StateFlow<TextFieldValue> = _codigo.asStateFlow()

    private val _erroresLexicos = MutableStateFlow<List<ErrorLexico>>(emptyList())
    val erroresLexicos: StateFlow<List<ErrorLexico>> = _erroresLexicos.asStateFlow()

    private val _erroresSintacticos = MutableStateFlow<List<ErrorSintactico>>(emptyList())
    val erroresSintacticos: StateFlow<List<ErrorSintactico>> = _erroresSintacticos.asStateFlow()

    private val _reporteErrores = MutableStateFlow<List<ErrorReporte>>(emptyList())
    val reporteErrores: StateFlow<List<ErrorReporte>> = _reporteErrores.asStateFlow()

    private val _isAnalizando = MutableStateFlow(false)
    val isAnalizando: StateFlow<Boolean> = _isAnalizando.asStateFlow()

    private val _errorCargaPkm = MutableStateFlow<String?>(null)
    val errorCargaPkm: StateFlow<String?> = _errorCargaPkm.asStateFlow()

    // Debug
    private val _codigoPkm = MutableStateFlow<String?>(null)
    val codigoPkm: StateFlow<String?> = _codigoPkm.asStateFlow()

    private val _mostrarDebug = MutableStateFlow(false)
    val mostrarDebug: StateFlow<Boolean> = _mostrarDebug.asStateFlow()

    // AST2: resultado del analisis del .pkm (lo usara el Renderer)
    private val _ast2 = MutableStateFlow<Nodo2Programa?>(null)
    val ast2: StateFlow<Nodo2Programa?> = _ast2.asStateFlow()

    private val analizador1 = Analizador1()
    private val analizador2 = Analizador2()

    fun actualizarCodigo(nuevoCodigo: TextFieldValue) {
        _codigo.value = nuevoCodigo
    }

    /**
     * Flujo 1: Compilar desde el editor
     * código .form -> Analizador1 -> String .pkm -> Analizador2 -> AST2
     */
    fun analizarCodigo() {
        viewModelScope.launch {

            _isAnalizando.value = true
            _erroresLexicos.value = emptyList()
            _erroresSintacticos.value = emptyList()
            _reporteErrores.value = emptyList()
            _codigoPkm.value = null
            _ast2.value = null

            val resultado = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {

                //FASE 1: Lenguaje 1 -> String .pkm
                val pkm = analizador1.analizar(_codigo.value.text)

                // FASE 2: String .pkm -> AST2
                var ast2Result: Nodo2Programa? = null
                if (pkm != null) {
                    ast2Result = analizador2.analizar(pkm)
                }

                object {
                    val lexicos1 = analizador1.getErroresLexicos()
                    val sintacticos1 = analizador1.getErroresSintacticos()
                    val semanticos1 = analizador1.getErroresSemanticos()
                    val lexicos2 = analizador2.getErroresLexicos()
                    val sintacticos2 = analizador2.getErroresSintacticos()
                    val pkm = pkm
                    val ast2 = ast2Result
                }
            }

            _codigoPkm.value = resultado.pkm
            _ast2.value = resultado.ast2

            val reporte = mutableListOf<ErrorReporte>()

            // Errores del Lenguaje 1
            for (error in resultado.lexicos1) {
                reporte.add(ErrorReporte(
                    lexema = error.lexema, linea = error.linea,
                    columna = error.columna, tipo = "Lexico L1",
                    descripcion = error.descripcion
                ))
            }
            for (error in resultado.sintacticos1) {
                reporte.add(ErrorReporte(
                    lexema = error.lexema, linea = error.linea,
                    columna = error.columna, tipo = "Sintactico L1",
                    descripcion = error.descripcion
                ))
            }
            for (error in resultado.semanticos1) {
                reporte.add(ErrorReporte(
                    lexema = "", linea = 0, columna = 0,
                    tipo = "Semantico L1", descripcion = error
                ))
            }

            // Errores del Lenguaje 2 (si los hay)
            for (error in resultado.lexicos2) {
                reporte.add(ErrorReporte(
                    lexema = error.lexema, linea = error.linea,
                    columna = error.columna, tipo = "Lexico L2",
                    descripcion = error.descripcion
                ))
            }
            for (error in resultado.sintacticos2) {
                reporte.add(ErrorReporte(
                    lexema = error.lexema, linea = error.linea,
                    columna = error.columna, tipo = "Sintactico L2",
                    descripcion = error.descripcion
                ))
            }

            _reporteErrores.value = reporte
            _isAnalizando.value = false
            _mostrarDebug.value = true
        }
    }

    /**
     * Flujo 2: Cargar archivo .pkm directamente
     * String .pkm -> Analizador2 → AST2
     */
    fun cargarPkm(contenidoPkm: String) {
        viewModelScope.launch {
            _isAnalizando.value = true
            _reporteErrores.value = emptyList()
            _ast2.value = null

            val resultado = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                val ast = analizador2.analizar(contenidoPkm)
                object {
                    val ast2 = ast
                    val lexicos = analizador2.getErroresLexicos()
                    val sintacticos = analizador2.getErroresSintacticos()
                }
            }

            _ast2.value = resultado.ast2
            _codigoPkm.value = contenidoPkm
            _isAnalizando.value = false

            // Si hay errores, generar mensaje
            if (resultado.ast2 == null && (resultado.lexicos.isNotEmpty() || resultado.sintacticos.isNotEmpty())) {
                val sb = StringBuilder("El archivo .pkm tiene errores y no se puede mostrar:\n\n")
                for (error in resultado.lexicos) {
                    sb.append("Linea ${error.linea}, Col ${error.columna}: ${error.descripcion}\n")
                }
                for (error in resultado.sintacticos) {
                    sb.append("Linea ${error.linea}, Col ${error.columna}: ${error.descripcion}\n")
                }
                _errorCargaPkm.value = sb.toString()
            }
        }
    }

    fun cerrarDebug() {
        _mostrarDebug.value = false
    }

    fun limpiarResultados() {
        _erroresLexicos.value = emptyList()
        _erroresSintacticos.value = emptyList()
        _reporteErrores.value = emptyList()
        _codigoPkm.value = null
        _ast2.value = null
    }

    fun cerrarErrorPkm() {
        _errorCargaPkm.value = null
    }
}