package com.example.proyecto1_compi1.Logic

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class FormularioRemoto(
    val nombre: String,
    val autor: String,
    val fecha: String,
    val hora: String
)

class ServicioServidor(
    private var ipServidor: String = "192.168.1.100",
    private var puerto: Int = 5000
) {
    private val errores = mutableListOf<String>()

    fun getErrores(): List<String> = errores.toList()
    fun limpiarErrores() { errores.clear() }

    fun setDireccion(ip: String, port: Int = 5000) {
        ipServidor = ip
        puerto = port
    }

    private fun baseUrl(): String = "http://$ipServidor:$puerto"

    /**
     * Sube un formulario .pkm al servidor.
     * Retorna true si fue exitoso.
     */
    fun subirFormulario(nombre: String, contenido: String, autor: String): Boolean {
        return try {
            val url = URL("${baseUrl()}/formularios")
            val conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "POST"
            conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conexion.doOutput = true
            conexion.connectTimeout = 5000
            conexion.readTimeout = 5000

            val json = JSONObject()
            json.put("nombre", nombre)
            json.put("contenido", contenido)
            json.put("autor", autor)

            conexion.outputStream.write(json.toString().toByteArray(Charsets.UTF_8))
            conexion.outputStream.flush()
            conexion.outputStream.close()

            val respuesta = conexion.responseCode == HttpURLConnection.HTTP_OK
            conexion.disconnect()
            respuesta
        } catch (e: Exception) {
            errores.add("Error al subir: ${e.message}")
            false
        }
    }

    /**
     * Lista todos los formularios disponibles en el servidor.
     */
    fun listarFormularios(): List<FormularioRemoto> {
        return try {
            val url = URL("${baseUrl()}/formularios")
            val conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = 5000
            conexion.readTimeout = 5000

            if (conexion.responseCode == HttpURLConnection.HTTP_OK) {
                val respuesta = conexion.inputStream.bufferedReader().readText()
                conexion.disconnect()

                val jsonArray = JSONArray(respuesta)
                val lista = mutableListOf<FormularioRemoto>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    lista.add(FormularioRemoto(
                        nombre = obj.getString("nombre"),
                        autor = obj.optString("autor", "Anónimo"),
                        fecha = obj.optString("fecha", ""),
                        hora = obj.optString("hora", "")
                    ))
                }
                lista
            } else {
                conexion.disconnect()
                errores.add("Error del servidor: código ${conexion.responseCode}")
                emptyList()
            }
        } catch (e: Exception) {
            errores.add("No se pudo conectar al servidor: ${e.message}")
            emptyList()
        }
    }

    /**
     * Descarga el contenido de un formulario .pkm del servidor.
     */
    fun descargarFormulario(nombre: String): String? {
        return try {
            val url = URL("${baseUrl()}/formularios/$nombre")
            val conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = 5000
            conexion.readTimeout = 5000

            if (conexion.responseCode == HttpURLConnection.HTTP_OK) {
                val respuesta = conexion.inputStream.bufferedReader().readText()
                conexion.disconnect()

                val json = JSONObject(respuesta)
                json.getString("contenido")
            } else {
                conexion.disconnect()
                errores.add("Formulario no encontrado: $nombre")
                null
            }
        } catch (e: Exception) {
            errores.add("Error al descargar: ${e.message}")
            null
        }
    }
}