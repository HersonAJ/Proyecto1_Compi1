package com.example.proyecto1_compi1.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto1_compi1.Logic.Analizador1
import com.example.proyecto1_compi1.models.ErrorLexico
import com.example.proyecto1_compi1.models.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioViewModel: ViewModel() {

    //estado observable
    private val _codigo = MutableStateFlow("")
    val codigo: StateFlow<String> = _codigo.asStateFlow()

    private val _tokens = MutableStateFlow<List<Token>>(emptyList())
    val tokens: StateFlow<List<Token>> = _tokens.asStateFlow()

    private val _errores = MutableStateFlow<List<ErrorLexico>>(emptyList())
    val errores: StateFlow<List<ErrorLexico>> = _errores.asStateFlow()

    private val _isAnalizando = MutableStateFlow(false)
    val isAnalizando: StateFlow<Boolean> = _isAnalizando.asStateFlow()

    //dependencia
    private val analizador = Analizador1()

    fun actualizarCodigo(nuevoCodigo: String) {
        _codigo.value = nuevoCodigo
    }

    fun analizarLexicamente() {
        viewModelScope.launch {
            _isAnalizando.value = true
            _errores.value = emptyList()

            val tokens = analizador.analizar(_codigo.value)

            _tokens.value = tokens

            if (analizador.tieneErrores()) {
                _errores.value = analizador.getErrores()
            }

            _isAnalizando.value = false
        }
    }

    fun limpiarResultados() {
        _tokens.value = emptyList()
        _errores.value = emptyList()
    }
}