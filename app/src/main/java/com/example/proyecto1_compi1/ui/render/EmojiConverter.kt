package com.example.proyecto1_compi1.ui.render

object EmojiConverter {

    // pagina de emojis https://emojikeyboard.top/es/
    private val reemplazos = mapOf(
        "@[:smile:]" to "\uD83D\uDE01\u200B", // 😀
        "@[:sad:]" to "\uD83E\uDD72\u200B", // 🥲
        "@[:serious:]" to "\uD83D\uDE10\u200B", // 😐
        "@[:heart:]" to " ❤\uFE0F ", //❤️
        "@[:star:]" to "⭐", // ⭐
        "@[:cat:]" to "\uD83D\uDC31\u200B" // 😺
    )

    fun convertir(texto: String): String {
        var resultado = texto
        for ((notacion, emoji) in reemplazos) {
            resultado = resultado.replace(notacion, emoji)
        }
        return resultado
    }
}