package com.example.proyecto1_compi1.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.lazy.items

data class Plantilla(
    val nombre: String,
    val codigo: String
)

// Lista de plantillas disponibles
val PLANTILLAS = listOf(

    Plantilla(
        nombre = "Sección básica",
        codigo = """
SECTION [
    width: 300,
    height: 200,
    pointX: 0,
    pointY: 0,
    orientation: VERTICAL,
    elements: {
        $ elementos aquí
    }
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Sección con estilos",
        codigo = """
SECTION [
    width: 300,
    height: 200,
    pointX: 0,
    pointY: 0,
    orientation: VERTICAL,
    styles [
        "color": BLACK,
        "background color": WHITE,
        "font family": SANS_SERIF,
        "text size": 14,
        "border": (1, LINE, BLACK)
    ],
    elements: {
        $ elementos aquí
    }
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Tabla",
        codigo = """
TABLE [
    width: 300,
    height: 100,
    pointX: 0,
    pointY: 0,
    elements: {
        [
            {
                TEXT [content: "Columna 1", width: 100, height: 30]
            },
            {
                TEXT [content: "Columna 2", width: 100, height: 30]
            }
        ],
        [
            {
                TEXT [content: "Dato 1", width: 100, height: 30]
            },
            {
                TEXT [content: "Dato 2", width: 100, height: 30]
            }
        ]
    }
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Texto",
        codigo = """
TEXT [
    content: "Tu texto aquí",
    width: 200,
    height: 30,
    styles [
        "color": BLACK,
        "text size": 14
    ]
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Pregunta abierta",
        codigo = """
OPEN_QUESTION [
    label: "Tu pregunta aquí",
    width: 200,
    height: 35
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Pregunta desplegable",
        codigo = """
DROP_QUESTION [
    label: "Tu pregunta aquí",
    width: 200,
    height: 35,
    options: {"Opción 1", "Opción 2", "Opción 3"},
    correct: 0
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Pregunta selección única",
        codigo = """
SELECT_QUESTION [
    label: "Tu pregunta aquí",
    width: 200,
    height: 40,
    options: {"Opción 1", "Opción 2", "Opción 3"},
    correct: 0
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Pregunta selección múltiple",
        codigo = """
MULTIPLE_QUESTION [
    label: "Tu pregunta aquí",
    width: 200,
    height: 50,
    options: {"Opción 1", "Opción 2", "Opción 3"},
    correct: {0, 2}
]
""".trimIndent()
    ),

    Plantilla(
        nombre = "Variable special + draw",
        codigo = """
special miPregunta = OPEN_QUESTION [
    label: "Pregunta dinámica",
    width: ?,
    height: ?
]
 
miPregunta.draw(200, 35)
""".trimIndent()
    ),

    Plantilla(
        nombre = "Ciclo FOR",
        codigo = """
FOR (i = 0 ; i < 5 ; i = i + 1) {
    $ instrucciones aquí
}
""".trimIndent()
    ),

    Plantilla(
        nombre = "Ciclo FOR rango",
        codigo = """
FOR (i in 1 .. 10) {
    $ instrucciones aquí
}
""".trimIndent()
    ),

    Plantilla(
        nombre = "Condicional IF / ELSE",
        codigo = """
number condicion = 10
 
IF (condicion > 5) {
    $ si se cumple
} ELSE {
    $ si no se cumple
}
""".trimIndent()
    ),

    Plantilla(
        nombre = "Ciclo WHILE",
        codigo = """
number contador = 0
 
WHILE (contador < 5) {
    contador = contador + 1
}
""".trimIndent()
    ),

    Plantilla(
        nombre = "Ciclo DO-WHILE",
        codigo = """
number contador = 0
 
DO {
    contador = contador + 1
} WHILE (contador < 5)
""".trimIndent()
    ),

    Plantilla(
        nombre = "Formulario completo",
        codigo = """
number ancho = 300
 
SECTION [
    width: ancho,
    height: 300,
    pointX: 0,
    pointY: 0,
    orientation: VERTICAL,
    styles [
        "color": BLACK,
        "background color": WHITE,
        "font family": SANS_SERIF,
        "text size": 14,
        "border": (1, LINE, BLUE)
    ],
    elements: {
        TEXT [
            content: "Mi Formulario @[:star:]",
            width: 280,
            height: 30,
            styles [
                "color": RED,
                "text size": 20
            ]
        ],
        OPEN_QUESTION [
            label: "Tu nombre",
            width: 260,
            height: 35
        ],
        DROP_QUESTION [
            label: "Tu opción favorita",
            width: 260,
            height: 35,
            options: {"Opción A", "Opción B", "Opción C"},
            correct: 0
        ],
        SELECT_QUESTION [
            label: "Tu pregunta aquí",
            width: 260,
            height: 40,
            options: {"Sí", "No", "Tal vez"},
            correct: 0
        ]
    }
]
""".trimIndent()
    )
)

@Composable
fun DialogoPlantillas(
    onSeleccionar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    text = "Insertar plantilla",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(PLANTILLAS) { plantilla ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSeleccionar(plantilla.codigo)
                                    onCerrar()
                                }
                        ) {
                            Text(
                                text = plantilla.nombre,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                HorizontalDivider()

                TextButton(
                    onClick = onCerrar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}