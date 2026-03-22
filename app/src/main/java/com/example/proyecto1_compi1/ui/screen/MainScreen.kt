package com.example.proyecto1_compi1.ui.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.proyecto1_compi1.Logic.ServicioServidor
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import com.example.proyecto1_compi1.ui.navigation.Screen
import com.example.proyecto1_compi1.ui.screen.FormAnswerScreen
import com.example.proyecto1_compi1.ui.screen.PantallaExplorar

@Composable
fun MainScreen(
    viewModel: FormularioViewModel
) {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.FORM_CREATOR) }
    val servicioServidor = remember { ServicioServidor() }

    when (currentScreen) {
     Screen.FORM_CREATOR -> {
         FormCreatorScreen(
             viewModel = viewModel,
             servicioServidor = servicioServidor,
             onNavigateToErrors = { currentScreen = Screen.ERROR_REPORT },
             onNavigateToAnswer = { currentScreen = Screen.FORM_ANSWER },
             onNavigateToExplorar = { currentScreen = Screen.EXPLORAR_SERVIDOR }
         )
     }
        Screen.ERROR_REPORT -> {
            ErrorReportScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.FORM_CREATOR }
            )
        }

        Screen.FORM_ANSWER -> {
            val ast2 by viewModel.ast2.collectAsState()
            if (ast2 != null) {
                FormAnswerScreen(
                    programa = ast2!!,
                    onBack = { currentScreen = Screen.FORM_CREATOR }
                )
            }
        }
        Screen.EXPLORAR_SERVIDOR -> {
         PantallaExplorar(
                 servicioServidor = servicioServidor,
                 onDescargar = { contenido ->
                     viewModel.cargarPkm(contenido)
                     currentScreen = Screen.FORM_ANSWER
                 },
                 onVolver = { currentScreen = Screen.FORM_CREATOR }
             )
         }
    }
}