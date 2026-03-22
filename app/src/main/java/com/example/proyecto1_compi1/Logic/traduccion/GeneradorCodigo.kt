package com.example.proyecto1_compi1.Logic.traduccion

import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.models.TablaSimbolos
import com.example.proyecto1_compi1.models.nodos.*

class GeneradorCodigo(
    private val tabla: TablaSimbolos,
    private val evaluador: EvaluadorExpresiones
) {
    private val errores = mutableListOf<String>()
    private val genEstilos = GeneradorEstilos(evaluador, errores)
    private val genElementos = GeneradorElementos(evaluador, genEstilos, errores)
    private val ejecutor = EjecutorInstrucciones(tabla, evaluador, genElementos, errores)
    private val genMetadatos = GeneradorMetadatos()

    fun generar(programa: NodoPrograma): String {
        val contenido = StringBuilder()

        for (item in programa.items) {
            procesarItem(item, contenido)
        }

        val metadatos = genMetadatos.generar(genElementos)
        return metadatos + contenido.toString()
    }

    private fun procesarItem(item: Any, contenido: StringBuilder) {
        when (item) {
            is NodoDeclaracion -> procesarDeclaracion(item)

            is NodoAsignacion -> {
                val valor = evaluador.evaluar(item.valor)
                tabla.asignar(item.nombre, valor)
            }

            is NodoElemento -> {
                contenido.append(genElementos.generar(item))
            }

            is NodoIf, is NodoWhile, is NodoDoWhile,
            is NodoFor, is NodoForRango -> {
                val resultados = ejecutor.ejecutarBloque(listOf(item))
                for (elem in resultados) {
                    contenido.append(elem)
                }
            }

            is NodoDraw -> {
                val resultados = ejecutor.ejecutarBloque(listOf(item))
                for (elem in resultados) {
                    contenido.append(elem)
                }
            }
        }
    }

    fun getErrores(): List<String> {
        return errores + tabla.getErrores() + evaluador.getErrores()
    }

    private fun procesarDeclaracion(decl: NodoDeclaracion) {
        if (decl.tipo == "special") {
            if (tabla.existe(decl.nombre)) {
                tabla.asignar(decl.nombre, decl.valor)
            } else {
                tabla.declarar(decl.nombre, "special", decl.valor)
            }
        } else {
            val valor = if (decl.valor is NodoExpresion) {
                evaluador.evaluar(decl.valor as NodoExpresion)
            } else null

            if (tabla.existe(decl.nombre)) {
                // Ya existe: reasignar si es mismo tipo
                if (tabla.obtenerTipo(decl.nombre) == decl.tipo) {
                    tabla.asignar(decl.nombre, valor)
                } else {
                    errores.add("Variable '${decl.nombre}' ya fue declarada con tipo ${tabla.obtenerTipo(decl.nombre)}.")
                }
            } else {
                tabla.declarar(decl.nombre, decl.tipo, valor)
            }
        }
    }
}