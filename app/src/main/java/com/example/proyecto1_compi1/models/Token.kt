package com.example.proyecto1_compi1.models

data class Token(
    val tipo: String,
    val lexema: String,
    val linea: Int,
    val columna: Int
)
