package com.example.proyecto1_compi1.models

data class ErrorSintactico(
    val linea: Int,
    val columna: Int,
    val descripcion: String
)
