package com.example.proyecto1_compi1.Logic

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ManejadorArchivos {

    companion object {
        const val TAMANO_MAXIMO = 1024_000 // 1024KB maximo
        val EXTENSIONES_FORM = listOf("form", "txt")
        val EXTENSIONES_PKM = listOf("pkm", "txt")
    }

    fun leerArchivo(context: Context, uri: Uri): String? {
        return try {
            // Validar tamaño
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            val tamano = cursor?.use {
                val idx = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
                it.moveToFirst()
                if (idx >= 0) it.getLong(idx) else 0L
            } ?: 0L

            if (tamano > TAMANO_MAXIMO) return null

            val inputStream = context.contentResolver.openInputStream(uri)
            val lector = BufferedReader(InputStreamReader(inputStream))
            val contenido = lector.readText()
            lector.close()
            inputStream?.close()
            contenido
        } catch (e: Exception) {
            null
        }
    }

    fun obtenerExtension(context: Context, uri: Uri): String {
        val nombre = obtenerNombre(context, uri)
        return nombre.substringAfterLast(".", "")
    }

    fun obtenerNombre(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            if (idx >= 0) it.getString(idx) ?: "desconocido" else "desconocido"
        } ?: "desconocido"
    }

    fun guardarArchivo(context: Context, uri: Uri, contenido: String): Boolean {
        return try {
            val outputStream = context.contentResolver.openOutputStream(uri)
            outputStream?.write(contenido.toByteArray())
            outputStream?.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}