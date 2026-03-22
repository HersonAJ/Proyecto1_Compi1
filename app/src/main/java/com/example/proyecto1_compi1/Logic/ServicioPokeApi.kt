package com.example.proyecto1_compi1.Logic

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ServicioPokeApi {

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/pokemon/"
        private const val TIMEOUT = 5000 //espera de 5 segundos
        private const val RANGO_MAXIMO = 50 //rango de pokemones a consultar
    }

    private val errores = mutableListOf<String>()

    fun getErrores(): List<String> = errores.toList()
    fun limpiarErrores() { errores.clear() }

    /* obtiene solo los nombres de los pokemones en el rango indicado
    retorna la lista de estos y si algo falla lo agrega a la lista de errores
    */

    fun obtenerPokemones(desde: Int, hasta: Int): List<String> {
        val nombres = mutableListOf<String>()

        //validaciones
        if (desde < 1) {
            errores.add("PokeAPI: el rango debe de ser mayor a 0, se recibio ${desde}")
            return nombres
        }
        if (hasta < desde) {
            errores.add("PokeAPI: el rango final ($hasta) debe de ser mayor o igual al inicial($desde)")
            return nombres
        }
        if (hasta - desde + 1 > RANGO_MAXIMO) {
            errores.add("PokeAPI: el rango es demasiado grande ")
            return nombres
        }

        for (id in desde..hasta) {
            val nombre = obtenerNombrePokemon(id)
            if (nombre != null) {
                nombres.add(nombre)
            } else {
                errores.add("PokeAPI: no se pudo obtener el pokemon con el ID $id")
                nombres.add("Pokemon #$id")
            }
        }
        return nombres
    }

    //consulta a la api para obtener los nombres

    private fun obtenerNombrePokemon(id: Int): String? {
        return try {
            val url = URL("$BASE_URL$id")
            val conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = TIMEOUT
            conexion.readTimeout = TIMEOUT

            if (conexion.responseCode == HttpURLConnection.HTTP_OK) {
                val respuesta = conexion.inputStream.bufferedReader().readText()
                conexion.disconnect()

                val json = JSONObject(respuesta)
                val nombre = json.getString("name")
                // Capitalizar primera letra
                nombre.replaceFirstChar { it.uppercase() }
            } else {
                conexion.disconnect()
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}