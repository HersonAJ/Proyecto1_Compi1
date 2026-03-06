package com.example.proyecto1_compi1.models

data class ErrorLexico(
    val lexema: String,
    val linea: Int,
    val columna: Int,
    val tipo: String,
    val descripcion: String
)
