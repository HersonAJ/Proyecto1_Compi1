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

    private val analizador = Analizador1()

    fun actualizarCodigo(nuevoCodigo: TextFieldValue) {
        _codigo.value = nuevoCodigo
    }

    fun analizarLexicamente() {
        viewModelScope.launch {

            _isAnalizando.value = true
            _erroresLexicos.value = emptyList()
            _erroresSintacticos.value = emptyList()
            _reporteErrores.value = emptyList()

            analizador.analizar(_codigo.value.text)

            val lexicos = analizador.getErroresLexicos()
            val sintacticos = analizador.getErroresSintacticos()

            _erroresLexicos.value = lexicos
            _erroresSintacticos.value = sintacticos

            val reporte = mutableListOf<ErrorReporte>()

            // errores lexicos
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

            // errores sintacticos
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

            _reporteErrores.value = reporte

            _isAnalizando.value = false
        }
    }

    fun limpiarResultados() {
        _erroresLexicos.value = emptyList()
        _erroresSintacticos.value = emptyList()
        _reporteErrores.value = emptyList()
    }
}