package com.example.proyecto1_compi1.ViewModel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto1_compi1.Logic.Analizador1
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.ErrorReporte
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

    //debung
    //salida del generador de código
    private val _codigoPkm = MutableStateFlow<String?>(null)
    val codigoPkm: StateFlow<String?> = _codigoPkm.asStateFlow()

    //controla si se muestra el debug dialog
    private val _mostrarDebug = MutableStateFlow(false)
    val mostrarDebug: StateFlow<Boolean> = _mostrarDebug.asStateFlow()

    private val analizador = Analizador1()

    fun actualizarCodigo(nuevoCodigo: TextFieldValue) {
        _codigo.value = nuevoCodigo
    }

    fun analizarCodigo() {
        viewModelScope.launch {

            _isAnalizando.value = true
            _erroresLexicos.value = emptyList()
            _erroresSintacticos.value = emptyList()
            _reporteErrores.value = emptyList()
            _codigoPkm.value = null

            val resultado = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                val pkm = analizador.analizar(_codigo.value.text)
                Triple(
                    analizador.getErroresLexicos(),
                    analizador.getErroresSintacticos(),
                    pkm
                )
            }

            val lexicos = resultado.first
            val sintacticos = resultado.second
            _codigoPkm.value = resultado.third

            _erroresLexicos.value = lexicos
            _erroresSintacticos.value = sintacticos

            val reporte = mutableListOf<ErrorReporte>()

            for (error in lexicos) {
                reporte.add(
                    ErrorReporte(
                        lexema = error.lexema,
                        linea = error.linea,
                        columna = error.columna,
                        tipo = "Lexico",
                        descripcion = error.descripcion
                    )
                )
            }

            for (error in sintacticos) {
                reporte.add(
                    ErrorReporte(
                        lexema = error.lexema,
                        linea = error.linea,
                        columna = error.columna,
                        tipo = "Sintactico",
                        descripcion = error.descripcion
                    )
                )
            }

            //errores semánticos al reporte
            for (error in analizador.getErroresSemanticos()) {
                reporte.add(
                    ErrorReporte(
                        lexema = "",
                        linea = 0,
                        columna = 0,
                        tipo = "Semantico",
                        descripcion = error
                    )
                )
            }

            _reporteErrores.value = reporte
            _isAnalizando.value = false

            // Mostrar debug automáticamente despues de compilar
            _mostrarDebug.value = true
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
    }
}