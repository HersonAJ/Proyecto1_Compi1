package com.example.proyecto1_compi1.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto1_compi1.Logic.Analizador1
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.ErrorSintactico
import com.example.proyecto1_compi1.models.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioViewModel: ViewModel() {

    private val _codigo = MutableStateFlow("")
    val codigo: StateFlow<String> = _codigo.asStateFlow()

    private val _tokens = MutableStateFlow<List<Token>>(emptyList())
    val tokens: StateFlow<List<Token>> = _tokens.asStateFlow()

    private val _erroresLexicos = MutableStateFlow<List<ErrorLexico>>(emptyList())
    val erroresLexicos: StateFlow<List<ErrorLexico>> = _erroresLexicos.asStateFlow()

    private val _erroresSintacticos = MutableStateFlow<List<ErrorSintactico>>(emptyList())
    val erroresSintacticos: StateFlow<List<ErrorSintactico>> = _erroresSintacticos.asStateFlow()

    private val _isAnalizando = MutableStateFlow(false)
    val isAnalizando: StateFlow<Boolean> = _isAnalizando.asStateFlow()

    private val analizador = Analizador1()

    fun actualizarCodigo(nuevoCodigo: String) {
        _codigo.value = nuevoCodigo
    }

    fun analizarLexicamente() {
        viewModelScope.launch {
            _isAnalizando.value = true
            _erroresLexicos.value = emptyList()
            _erroresSintacticos.value = emptyList()

            val tokens = analizador.analizar(_codigo.value)

            _tokens.value = tokens

            if (analizador.tieneErroresLexicos()) {
                _erroresLexicos.value = analizador.getErroresLexicos()
            }
            if (analizador.tieneErroresSintacticos()) {
                _erroresSintacticos.value = analizador.getErroresSintacticos()
            }

            _isAnalizando.value = false
        }
    }

    fun limpiarResultados() {
        _tokens.value = emptyList()
        _erroresLexicos.value = emptyList()
        _erroresSintacticos.value = emptyList()
    }
}