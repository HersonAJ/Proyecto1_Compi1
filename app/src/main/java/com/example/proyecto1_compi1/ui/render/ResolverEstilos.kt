package com.example.proyecto1_compi1.ui.render

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.proyecto1_compi1.models.nodo2.Nodo2Color
import com.example.proyecto1_compi1.models.nodo2.Nodo2ColorHSL
import com.example.proyecto1_compi1.models.nodo2.Nodo2ColorHex
import com.example.proyecto1_compi1.models.nodo2.Nodo2ColorNombre
import com.example.proyecto1_compi1.models.nodo2.Nodo2ColorRGB
import com.example.proyecto1_compi1.models.nodo2.Nodo2Estilos

class ResolverEstilos {

    fun resolver(nodo: Nodo2Estilos?): EstilosResueltos? {
        if (nodo == null) return null
        return EstilosResueltos(
            color = resolverColor(nodo.color) ?: EstilosResueltos.VALOR_DEFAULT_COLOR,
            bgColor = resolverColor(nodo.bgColor) ?: EstilosResueltos.VALOR_DEFAULT_BG,
            fontFamily = resolverFuente(nodo.fontFamily),
            fontSize = if (nodo.textSize != null) nodo.textSize!!.sp else EstilosResueltos.VALOR_DEFAULT_FONT_SIZE,
            bordeGrosor = nodo.borde?.grosor?.toFloat() ?: 0f,
            bordeTipo = nodo.borde?.tipo ?: "LINE",
            bordeColor = resolverColor(nodo.borde?.color) ?: Color.Transparent
        )
    }

    fun combinar(padre: EstilosResueltos, estilosHijo: Nodo2Estilos?): EstilosResueltos {
        val hijosResueltos = resolver(estilosHijo)
        return padre.heredarA(hijosResueltos)
    }

    fun resolverColor(color: Nodo2Color?): Color? {
        if (color == null) return null

        return when (color) {
            is Nodo2ColorNombre -> colorPorNombre(color.nombre)
            is Nodo2ColorHex -> colorDesdeHex(color.valor)
            is Nodo2ColorRGB -> colorDesdeRGB(color.r, color.g, color.b)
            is Nodo2ColorHSL -> colorDesdeHSL(color.valor)
        }
    }

    private fun colorPorNombre(nombre: String): Color {
        return when (nombre.uppercase()) {
            "RED" -> Color.Red
            "BLUE" -> Color.Blue
            "GREEN" -> Color.Green
            "YELLOW" -> Color.Yellow
            "BLACK" -> Color.Black
            "WHITE" -> Color.White
            "PURPLE" -> Color(0xFF800080)
            "SKY" -> Color(0xFF87CEEB)
            else -> Color.Black
        }
    }

    private fun colorDesdeHex(hex: String): Color {
        return try {
            val limpio = hex.removePrefix("#")
            val colorLong = limpio.toLong(16)
            Color(
                red = ((colorLong shr 16) and 0xFF).toInt(),
                green = ((colorLong shr 8) and 0xFF).toInt(),
                blue = (colorLong and 0xFF).toInt()
            )
        } catch (e: Exception) {
            Color.Black
        }
    }

    private fun colorDesdeRGB(r: Double, g: Double, b: Double): Color {
        return try {
            Color(
                red = r.toInt().coerceIn(0, 255),
                green = g.toInt().coerceIn(0, 255),
                blue = b.toInt().coerceIn(0, 255)
            )
        } catch (e: Exception) {
            Color.Black
        }
    }

    private fun colorDesdeHSL(valor: String): Color {
        return try {
            // El valor viene como "<H,S,L>" — hay que limpiar
            val limpio = valor.removePrefix("<").removeSuffix(">").trim()
            val partes = limpio.split(",").map { it.trim().toDouble() }
            if (partes.size == 3) {
                hslAColor(partes[0], partes[1], partes[2])
            } else {
                Color.Black
            }
        } catch (e: Exception) {
            Color.Black
        }
    }

    private fun hslAColor(h: Double, s: Double, l: Double): Color {
        val hNorm = h / 360.0
        val sNorm = s / 100.0
        val lNorm = l / 100.0

        if (sNorm == 0.0) {
            val gris = (lNorm * 255).toInt()
            return Color(gris, gris, gris)
        }

        val q = if (lNorm < 0.5) lNorm * (1 + sNorm) else lNorm + sNorm - lNorm * sNorm
        val p = 2 * lNorm - q

        val r = hueAComponente(p, q, hNorm + 1.0 / 3.0)
        val g = hueAComponente(p, q, hNorm)
        val b = hueAComponente(p, q, hNorm - 1.0 / 3.0)

        return Color(
            (r * 255).toInt().coerceIn(0, 255),
            (g * 255).toInt().coerceIn(0, 255),
            (b * 255).toInt().coerceIn(0, 255)
        )
    }

    private fun hueAComponente(p: Double, q: Double, t: Double): Double {
        var tAdj = t
        if (tAdj < 0) tAdj += 1.0
        if (tAdj > 1) tAdj -= 1.0
        return when {
            tAdj < 1.0 / 6.0 -> p + (q - p) * 6.0 * tAdj
            tAdj < 1.0 / 2.0 -> q
            tAdj < 2.0 / 3.0 -> p + (q - p) * (2.0 / 3.0 - tAdj) * 6.0
            else -> p
        }
    }

    private fun resolverFuente(fuente: String?): FontFamily {
        return when (fuente?.uppercase()) {
            "MONO" -> FontFamily.Monospace
            "SANS_SERIF" -> FontFamily.SansSerif
            "CURSIVE" -> FontFamily.Cursive
            else -> FontFamily.Default
        }
    }
}