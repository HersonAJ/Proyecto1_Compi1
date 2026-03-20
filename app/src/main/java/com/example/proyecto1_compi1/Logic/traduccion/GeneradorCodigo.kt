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

    /**
     Recibe el AST del Lenguaje 1 y produce un String en formato .pkm

     Orden de ejecucion:
     1. Declaraciones -> llena la tabla de simbolos
     2. Secciones -> genera .pkm (usa valores de la tabla)
     3. Codigo -> ejecuta logica, genera draws
     */
    fun generar(programa: NodoPrograma): String {
        //Paso 1: Registrar declaraciones en la tabla
        procesarDeclaraciones(programa.declaraciones)

        //Paso 2: Generar .pkm de las secciones del formulario
        val contenido = StringBuilder()
        for (elemento in programa.secciones) {
            @Suppress("SENSELESS_COMPARISON")
            if (elemento != null) {
                contenido.append(genElementos.generar(elemento))
            }
        }

        //Paso 3: Ejecutar bloques de codigo (if, for, while, draw)
        val elementosDraw = ejecutor.ejecutarBloque(programa.instrucciones)
        for (elem in elementosDraw) {
            contenido.append(elem)
        }

        //Paso 4: Generar metadatos con los contadores
        val metadatos = genMetadatos.generar(genElementos)

        return metadatos + contenido.toString()
    }

    fun getErrores(): List<String> {
        return errores + tabla.getErrores() + evaluador.getErrores()
    }

    private fun procesarDeclaraciones(declaraciones: List<NodoInstruccion>) {
        for (decl in declaraciones) {
            if (decl is NodoDeclaracion) {
                if (decl.tipo == "special") {
                    tabla.declarar(decl.nombre, "special", decl.valor)
                } else {
                    val valor = if (decl.valor is NodoExpresion) {
                        evaluador.evaluar(decl.valor as NodoExpresion)
                    } else null
                    tabla.declarar(decl.nombre, decl.tipo, valor)
                }
            }
        }
    }
}