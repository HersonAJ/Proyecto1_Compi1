// MainScreen.kt con enum
package com.example.proyecto1_compi1.ui.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.proyecto1_compi1.ViewModel.FormularioViewModel
import com.example.proyecto1_compi1.ui.navigation.Screen

@Composable
fun MainScreen(
    viewModel: FormularioViewModel
) {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.FORM_CREATOR) }

    when (currentScreen) {
        Screen.FORM_CREATOR -> {
            FormCreatorScreen(
                viewModel = viewModel,
                onNavigateToErrors = { currentScreen = Screen.ERROR_REPORT }
            )
        }
        Screen.ERROR_REPORT -> {
            ErrorReportScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.FORM_CREATOR }
            )
        }
    }
}