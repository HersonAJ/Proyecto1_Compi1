package com.example.proyecto1_compi1.Logic.traduccion

import com.example.proyecto1_compi1.Logic.evaluacion.EvaluadorExpresiones
import com.example.proyecto1_compi1.models.TablaSimbolos
import com.example.proyecto1_compi1.models.nodos.*


class EjecutorInstrucciones(
    private val tabla: TablaSimbolos,
    private val evaluador: EvaluadorExpresiones,
    private val genElementos: GeneradorElementos,
    private val errores: MutableList<String>
) {
    companion object {
        const val MAX_ITERACIONES = 100
    }

    // Ejecuta un bloque de instrucciones Retorna los strings .pkm generados por draw()
    fun ejecutarBloque(instrucciones: List<Any>): List<String> {
        val resultado = mutableListOf<String>()
        for (item in instrucciones) {
            when (item) {
                is NodoInstruccion -> resultado.addAll(ejecutar(item))
                is NodoElemento -> resultado.add(genElementos.generar(item))
            }
        }
        return resultado
    }
    private fun ejecutar(inst: NodoInstruccion): List<String> {
        return when (inst) {
            is NodoDeclaracion -> { procesarDeclaracion(inst); emptyList() }
            is NodoAsignacion -> { procesarAsignacion(inst); emptyList() }
            is NodoIf -> ejecutarIf(inst)
            is NodoElse -> ejecutarBloque(inst.cuerppo)
            is NodoWhile -> ejecutarWhile(inst)
            is NodoDoWhile -> ejecutarDoWhile(inst)
            is NodoFor -> ejecutarFor(inst)
            is NodoForRango -> ejecutarForRango(inst)
            is NodoDraw -> ejecutarDraw(inst)
        }
    }

    // ======================== ASIGNACION / DECLARACION ========================



    private fun procesarAsignacion(asig: NodoAsignacion) {
        val valor = evaluador.evaluar(asig.valor)
        tabla.asignar(asig.nombre, valor)
    }

    // ======================== CONDICIONES ========================

    private fun esVerdadero(valor: Any?): Boolean {
        return when (valor) {
            is Boolean -> valor
            is Double -> valor >= 1.0
            else -> false
        }
    }

    // ======================== IF / ELSE ========================

    private fun ejecutarIf(nodoIf: NodoIf): List<String> {
        val condicion = evaluador.evaluar(nodoIf.condicion)
        return if (esVerdadero(condicion)) {
            ejecutarBloque(nodoIf.cuerppo)
        } else if (nodoIf.elseParte != null) {
            ejecutar(nodoIf.elseParte)
        } else {
            emptyList()
        }
    }

    // ======================== WHILE ========================

    private fun ejecutarWhile(nodo: NodoWhile): List<String> {
        val resultado = mutableListOf<String>()
        var iteraciones = 0

        while (esVerdadero(evaluador.evaluar(nodo.condicion))) {
            if (++iteraciones > MAX_ITERACIONES) {
                errores.add("WHILE excedio el limite de $MAX_ITERACIONES iteraciones.")
                break
            }
            resultado.addAll(ejecutarBloque(nodo.cuerppo))
        }
        return resultado
    }

    // ======================== DO-WHILE ========================

    private fun ejecutarDoWhile(nodo: NodoDoWhile): List<String> {
        val resultado = mutableListOf<String>()
        var iteraciones = 0

        do {
            if (++iteraciones > MAX_ITERACIONES) {
                errores.add("DO-WHILE excedio el limite de $MAX_ITERACIONES iteraciones.")
                break
            }
            resultado.addAll(ejecutarBloque(nodo.cuerppo))
        } while (esVerdadero(evaluador.evaluar(nodo.condicion)))

        return resultado
    }

    // ======================== FOR CLASICO ========================

    private fun ejecutarFor(nodo: NodoFor): List<String> {
        val resultado = mutableListOf<String>()
        val nombreVar = nodo.inicializacion.nombre

        // Auto-declarar variable como number si no existe
        if (!tabla.existe(nombreVar)) {
            tabla.declarar(nombreVar, "number", 0.0)
        } else if (tabla.obtenerTipo(nombreVar) != "number") {
            errores.add("Variable '$nombreVar' en FOR debe ser tipo number.")
            return emptyList()
        }

        // Ejecutar inicializacion
        procesarAsignacion(nodo.inicializacion)

        var iteraciones = 0
        while (esVerdadero(evaluador.evaluar(nodo.condicion))) {
            if (++iteraciones > MAX_ITERACIONES) {
                errores.add("FOR excedio el limite de $MAX_ITERACIONES iteraciones.")
                break
            }
            resultado.addAll(ejecutarBloque(nodo.cuerpo))
            procesarAsignacion(nodo.actualizacion)
        }
        return resultado
    }

    // ======================== FOR RANGO ========================

    private fun ejecutarForRango(nodo: NodoForRango): List<String> {
        val resultado = mutableListOf<String>()

        // Auto-declarar variable como number si no existe
        if (!tabla.existe(nodo.variable)) {
            tabla.declarar(nodo.variable, "number", 0.0)
        } else if (tabla.obtenerTipo(nodo.variable) != "number") {
            errores.add("Variable '${nodo.variable}' en FOR debe ser tipo number.")
            return emptyList()
        }

        val inicio = evaluador.evaluar(nodo.inicio) as? Double
        val fin = evaluador.evaluar(nodo.fin) as? Double

        if (inicio == null || fin == null) {
            errores.add("Rango del FOR inválido.")
            return emptyList()
        }

        var i = inicio.toInt()
        val limite = fin.toInt()
        while (i <= limite) {
            tabla.asignar(nodo.variable, i.toDouble())
            resultado.addAll(ejecutarBloque(nodo.cuerpo))
            i++
        }
        return resultado
    }

    // ======================== DRAW ========================

    private fun ejecutarDraw(draw: NodoDraw): List<String> {
        val valor = tabla.obtener(draw.nombre)

        if (valor == null) {
            errores.add("Variable '${draw.nombre}' no está inicializada o no existe.")
            return emptyList()
        }

        if (valor !is NodoElemento) {
            errores.add("Variable '${draw.nombre}' no es de tipo special (no contiene un elemento).")
            return emptyList()
        }

        // Evaluar argumentos que reemplazaran los comodines (?)
        val args = draw.argumentos.map { evaluador.evaluar(it) }
        evaluador.setArgsComodin(args)

        // Generar .pkm del elemento almacenado
        val pkm = genElementos.generar(valor)

        evaluador.resetComodines()
        return listOf(pkm)
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