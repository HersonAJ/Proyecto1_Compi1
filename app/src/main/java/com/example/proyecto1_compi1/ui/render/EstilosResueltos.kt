package com.example.proyecto1_compi1.ui.render

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class EstilosResueltos(
    val color: Color = Color.Black,
    val bgColor: Color = Color.White,
    val fontFamily: FontFamily = FontFamily.Default,
    val fontSize: TextUnit = 14.sp,
    val bordeGrosor: Float = 0f,
    val bordeTipo: String = "LINE",
    val bordeColor: Color = Color.Transparent
) {

    fun heredarA(hijo: EstilosResueltos?): EstilosResueltos {
        if (hijo == null)  return this

        return EstilosResueltos(
            color = if (hijo.color != VALOR_DEFAULT_COLOR) hijo.color else this.color,
            bgColor = if (hijo.bgColor != VALOR_DEFAULT_BG) hijo.bgColor else this.bgColor,
            fontFamily = if (hijo.fontFamily != FontFamily.Default) hijo.fontFamily else this.fontFamily,
            fontSize = if(hijo.fontSize != VALOR_DEFAULT_FONT_SIZE) hijo.fontSize else this.fontSize,
            bordeGrosor = if (hijo.bordeGrosor > 0f) hijo.bordeGrosor else this.bordeGrosor,
            bordeTipo = if (hijo.bordeGrosor > 0f)hijo.bordeTipo else this.bordeTipo,
            bordeColor = if (hijo.bordeGrosor > 0f) hijo.bordeColor else this.bordeColor
        )
    }

    companion object {
        //valores para los no definidos
        val VALOR_DEFAULT_COLOR = Color.Black
        val VALOR_DEFAULT_BG = Color.White
        val VALOR_DEFAULT_FONT_SIZE = 14.sp

        val DEFAULT = EstilosResueltos()
    }
}
