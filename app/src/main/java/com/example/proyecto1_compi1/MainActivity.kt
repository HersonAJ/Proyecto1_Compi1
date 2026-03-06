package com.example.proyecto1_compi1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto1_compi1.ui.theme.Proyecto1_Compi1Theme
import com.example.proyecto1_compi1.Analizadores.Lexer
import com.example.proyecto1_compi1.models.Token
import java.io.StringReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val entrada = """
$ comentario de una sola linea
#

SECTION encuesta1 {

    TEXT titulo = "Encuesta de Programacion";

    TABLE tabla1 {
        width = 500;
        height = 300;
        pointX = 10;
        pointY = 20;
        orientation = VERTICAL;
    }

    OPEN_QUESTION pregunta1 {
        label = "¿Que lenguaje prefieres?";
        content = "Responde libremente";
    }

    DROP_QUESTION pregunta2 {
        label = "Selecciona tu lenguaje favorito";
        options = ["Java", "Python", "C++", "Rust"];
        correct = "Java";
    }

    SELECT_QUESTION pregunta3 {
        label = "Elige un color";
        options = [RED, BLUE, GREEN, PURPLE];
    }

    MULTIPLE_QUESTION pregunta4 {
        label = "Selecciona multiples tecnologias";
        options = ["Docker", "Kubernetes", "Git"];
    }

}
@

/* comentario
   multilinea
*/

styles {

    background color = #FFAA00;
    font family = MONO;
    text size = 16;

    border = LINE;
    color = RGB(120,200,255);
}

IF (10 >= 5 && 3 < 9) {
    draw figura1;
}

FOR i in 1..10 {
    draw punto;
}

resultado = (10 + 5) * 2 ^ 3 % 4;

condicion = 10 >= 5 || 3 <= 8;

valor = 10;
valor2 = 3.1416;

color1 = #FF0000;
color2 = (255,0,0);
color3 = <120,50,50>;

?

@[:)]
@[:(((]
@[:smile:]
@[:sad:]
@[:||||]
@[:serious:]
@[<3]
@[<<<333]
@[:heart:]
@[:star:]
@[:star:3]
@[:star-5]
@[:^^:]
@[:cat:]
@[:hola]

identificador1
variable_test
contador123

who_is_that_pokemon
""".trimIndent()

        val TAG = "LexerPrueba"
        val reader = StringReader(entrada)
        val lexer = Lexer(reader)

        var token: Token? = lexer.yylex()

        while (token != null) {
            Log.d(TAG, "Token: ${token.tipo} | Lexema: ${token.lexema} | Línea: ${token.linea} | Columna: ${token.columna}")
            token = lexer.yylex()
        }

        reader.close()

        val errores = lexer.errores

        if (errores.isNotEmpty()) {
            Log.d(TAG, "Errores lexicos")
            for (error in errores) {
                Log.d(
                    TAG, "Error: ${error.lexema} | linea: ${error.linea} | columna: ${error.columna} | ${error.descripcion}"
                )
            }
        } else {
            Log.d(TAG, "no se encontraron errores lexicos")
        }
    }
}

