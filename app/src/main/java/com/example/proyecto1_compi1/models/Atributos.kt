package com.example.proyecto1_compi1.models

import com.example.proyecto1_compi1.models.nodos.NodoColor
import com.example.proyecto1_compi1.models.nodos.NodoElemento
import com.example.proyecto1_compi1.models.nodos.NodoExpresion

class Atributos {
    private val map = HashMap<String, Any>()

    fun put(key: String, value: Any) { map[key] = value }
    fun get(key: String): Any? = map[key]
    fun getExpresion(key: String): NodoExpresion? = map[key] as? NodoExpresion
    fun getString(key: String): String? = map[key] as? String
    fun getColor(key: String): NodoColor? = map[key] as? NodoColor
    fun getEstilosMap(key: String): Atributos? = map[key] as? Atributos
    fun merge(other: Atributos) { map.putAll(other.map) }
    fun toMap(): Map<String, Any> = HashMap(map)
    override fun toString(): String = map.toString()

    @Suppress("UNCHECKED_CAST")
    fun getListaExpresiones(key: String): List<NodoExpresion> =
        (map[key] as? List<NodoExpresion>) ?: emptyList()

    @Suppress("UNCHECKED_CAST")
    fun getListaElementos(key: String): List<NodoElemento> =
        (map[key] as? List<NodoElemento>) ?: emptyList()

    @Suppress("UNCHECKED_CAST")
    fun getListaFilas(key: String): List<List<NodoElemento>> =
        (map[key] as? List<List<NodoElemento>>) ?: emptyList()
}